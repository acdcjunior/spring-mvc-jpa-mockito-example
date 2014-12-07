<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>$titulo</title>

	<link rel="stylesheet" href="#springUrl('/static/css/')layout/layout.css" />
	<!--[if !IE 7]>
	<style type="text/css"> #wrap {display:table;height:100%} </style>
	<![endif]-->
	<link rel="stylesheet" href="#springUrl('/static/css/')layout/header.css" />

	#foreach ($arquivoCss in $css)
		<link rel="stylesheet" href="#springUrl('/static/css/')$viewName/$arquivoCss" />
	#end
</head>
<body>
	<div class="stickyFooterWrapper">
		<div class="portal-header">#parse('layout/header.jsp')</div>
		<div class="portal-conteudo">$screen_content</div>
	</div>
	<div class="portal-footer">#parse('layout/footer.jsp')</div>

	<!-- Ver: http://developer.yahoo.com/performance/rules.html#js_bottom  -->
	<script type="text/javascript" src="#springUrl('/static/js/lib/jquery/jquery-1.10.2.min.js')"></script>
	<script type="text/javascript" src="#springUrl('/static/js/layout/layout.js')"></script>
	$script ## trecho de JavaScript definido por cada view
</body>
</html>