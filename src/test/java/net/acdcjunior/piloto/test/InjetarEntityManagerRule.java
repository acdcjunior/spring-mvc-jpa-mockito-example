package net.acdcjunior.piloto.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.naming.java.javaURLContextFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

public class InjetarEntityManagerRule implements MethodRule {
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface InjetarEntityManager { }
	
	private static final String URL_DATA_SOURCE = "java:/comp/env/jdbc/pilotoDataSource";
	private static final String NOME_PERSISTENCE_UNIT = "pilotoPersistenceUnit";
	private static final String NOME_CAMPO_ENTITYMANAGER = "em";
	private static final String URL_H2_EM_MEMORIA = "jdbc:h2:mem:bancoDeTestesEmMemoria;INIT=RUNSCRIPT FROM 'classpath:sql/esquema.sql'\\;RUNSCRIPT FROM 'classpath:sql/dados.sql'\\;";
	
	private static EntityManagerFactory entityManagerFactory;
	
	private EntityManager em;
	
	public InjetarEntityManagerRule() {
		inicializarEntityManagerFactory();
	}
	
	public EntityManager getEntityManagerInjetado() {
		return em;
	}
	
	@Override
	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		this.em = entityManagerFactory.createEntityManager();
		InjetorDeEntityManager.injetarCamposAnotados(target, NOME_CAMPO_ENTITYMANAGER, this.em);
		return base;
	}
	
	private void inicializarEntityManagerFactory() {
		try {
			if (entityManagerFactory == null) {
				realizarBindJndiDoDataSourceDeTestes();
				entityManagerFactory = Persistence.createEntityManagerFactory(NOME_PERSISTENCE_UNIT);
				desfazerBindJndiDoDataSourceDeTestes();
			}
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	private static void realizarBindJndiDoDataSourceDeTestes() throws NamingException {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, javaURLContextFactory.class.getName());
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		
		InitialContext ic = new InitialContext();
		ic.createSubcontext("java:");
		ic.createSubcontext("java:/comp");
		ic.createSubcontext("java:/comp/env");
		ic.createSubcontext("java:/comp/env/jdbc");
		
		ic.bind(URL_DATA_SOURCE, criarDS());
	}

	private static JdbcDataSource criarDS() {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL(URL_H2_EM_MEMORIA);
		ds.setUser("sa");
		ds.setPassword("");
		return ds;
	}
	
	private static void desfazerBindJndiDoDataSourceDeTestes() throws NamingException {
		InitialContext ic = new InitialContext();
		ic.unbind(URL_DATA_SOURCE);
	}

}

class InjetorDeEntityManager {
	
	public static void injetarCamposAnotados(Object instanciaDaClasseDeTestes,
											 String nomeCampo,
											 EntityManager entityManagerAInjetar) {
		
		Class<?> clazz = instanciaDaClasseDeTestes.getClass();
		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(InjetarEntityManagerRule.InjetarEntityManager.class)) {
				injetar(instanciaDaClasseDeTestes, f, nomeCampo, entityManagerAInjetar);
			}
		}
		
	}

	private static void injetar(final Object instanciaDaClasseDeTestes,
								final Field f,
								final String nomeCampo,
								final EntityManager entityManagerAInjetar) {
		
		final Object instanciaDaClasseSobTestes = getFieldValue(f, instanciaDaClasseDeTestes);
		Class<?> classeSobTestes = instanciaDaClasseSobTestes.getClass();
			
		ReflectionUtils.doWithFields(classeSobTestes,
			new FieldCallback() {
				@Override
				public void doWith(Field f) throws IllegalArgumentException, IllegalAccessException {
					setFieldValue(f, instanciaDaClasseSobTestes, entityManagerAInjetar);
				}
			}, new FieldFilter() {
				@Override public boolean matches(Field field) {
					boolean campoNaoEhStatic = !Modifier.isStatic(field.getModifiers());
					boolean campoTemNomeEsperado = field.getName().equals(nomeCampo);
					return campoNaoEhStatic && campoTemNomeEsperado;
				}
			}
		);
	}

	private static void setFieldValue(Field f, Object instancia, Object valor) {
		try {
			boolean acessibilidadeAnterior = f.isAccessible();
			f.setAccessible(true);
			f.set(instancia, valor);
			f.setAccessible(acessibilidadeAnterior);
		} catch (IllegalAccessException e) { throw new RuntimeException(e); }
	}

	private static Object getFieldValue(Field f, Object instancia) {
		try {
			boolean acessibilidadeAnterior = f.isAccessible();
			f.setAccessible(true);
			Object fieldValue = f.get(instancia);
			f.setAccessible(acessibilidadeAnterior);
			return fieldValue;
		} catch (IllegalAccessException e) { throw new RuntimeException(e); }
		
	}
	
}