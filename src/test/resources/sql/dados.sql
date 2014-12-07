-- DADOS USADOS NO AMBIENTE DE TESTES

INSERT INTO `TB_USUARIO` (`ID`, `NOME`) VALUES (1, 'Charles Francis "Professor" Xavier');
INSERT INTO `TB_USUARIO` (`ID`, `NOME`) VALUES (2, 'John "Wolverine" Logan');
INSERT INTO `TB_USUARIO` (`ID`, `NOME`) VALUES (3, 'Papai Smurf');

INSERT INTO `TB_FUNCAO` (`ID`, `NOME`) VALUES (1, 'Lider');
INSERT INTO `TB_FUNCAO` (`ID`, `NOME`) VALUES (2, 'Mutante');
INSERT INTO `TB_FUNCAO` (`ID`, `NOME`) VALUES (3, 'Barbudo');

INSERT INTO `TB_USUARIO_FUNCAO` (`USUARIO_ID`, `FUNCAO_ID`) VALUES (1, 1);
INSERT INTO `TB_USUARIO_FUNCAO` (`USUARIO_ID`, `FUNCAO_ID`) VALUES (1, 2);
INSERT INTO `TB_USUARIO_FUNCAO` (`USUARIO_ID`, `FUNCAO_ID`) VALUES (2, 2);
INSERT INTO `TB_USUARIO_FUNCAO` (`USUARIO_ID`, `FUNCAO_ID`) VALUES (2, 3);
INSERT INTO `TB_USUARIO_FUNCAO` (`USUARIO_ID`, `FUNCAO_ID`) VALUES (3, 1);
INSERT INTO `TB_USUARIO_FUNCAO` (`USUARIO_ID`, `FUNCAO_ID`) VALUES (3, 3);