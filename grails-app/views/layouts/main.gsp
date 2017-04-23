<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
  		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<g:layoutHead/>
	</head>
	<body>
		<div align="right" id="loginHeader">
			<g:loginControl />
		</div>
		<div id="grailsLogo" role="banner">
			<!-- Static navbar -->
			<nav class="navbar navbar-default">
				<div class="container-fluid">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle collapsed navbar-left" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
							<span class="sr-only">Toggle navigation</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
							<a href="#"><asset:image style="width: 80px; height: 80px" src="logo_caseritos.jpg" alt="La Fabrica Caseros"/></a>
					</div>
					<div id="navbar" class="navbar-collapse collapse">
						<ul class="nav navbar-nav">
							<g:if test="${sec.loggedInUserInfo(field:'username')}">
								%{--<li>
									<g:link controller="player" action="edit" id="${sec.loggedInUserInfo(field:'id')}">Mi Perfil</g:link>
									<g:link mapping="usuario_perfil" id="${sec.loggedInUserInfo(field:'id')}">Mi Perfil</g:link>
									<a href="/player/edit/${sec.loggedInUserInfo(field:'id')}">Modificar Mis Datos</a>
								</li>--}%
								<li><g:link>Mis Sanwiches</g:link></li>
								<li><g:link>Proveedores</g:link></li>
								<li><g:link>Usuarios</g:link></li>
								<li><g:link>Facturacion</g:link></li>
								<li><g:link>Ingredientes</g:link></li>
							</g:if>
						</ul>
					</div><!--/.nav-collapse -->
				</div><!--/.container-fluid -->
			</nav>
		</div>
		<g:layoutBody/>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
	</body>
</html>
