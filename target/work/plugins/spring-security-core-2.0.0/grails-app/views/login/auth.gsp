<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
	<style type='text/css' media='screen'>
	#login {
		margin: 15px 0px;
		padding: 0px;
		text-align: center;
	}

	#login .inner {
		width: 540px;
		padding-bottom: 6px;
		margin: 60px auto;
		text-align: left;
		border: 1px solid #aab;
		background-color: #f0f0fa;
		-moz-box-shadow: 2px 2px 2px #eee;
		-webkit-box-shadow: 2px 2px 2px #eee;
		-khtml-box-shadow: 2px 2px 2px #eee;
		box-shadow: 2px 2px 2px #eee;
	}

	#login .inner .fheader {
		padding: 18px 26px 14px 26px;
		background-color: #f7f7ff;
		margin: 0px 0 14px 0;
		color: #2e3741;
		font-size: 18px;
		font-weight: bold;
	}

	#login .inner .cssform p {
		clear: left;
		margin: 0;
		padding: 4px 0 3px 0;
		padding-left: 105px;
		margin-bottom: 20px;
		height: 1%;
	}

	#login .inner .cssform input[type='text'] {
		width: 140px;
	}

	#login .inner .cssform label {
		font-weight: bold;
		float: left;
		text-align: right;
		margin-left: -105px;
		width: 200px;
		padding-top: 3px;
		padding-right: 10px;
	}

	#login #remember_me_holder {
		padding-left: 120px;
	}

	#login #submit {
		margin-left: 15px;
	}

	#login #remember_me_holder label {
		float: none;
		margin-left: 0;
		text-align: left;
		width: 200px
	}

	#login .inner .login_message {
		padding: 6px 25px 20px 25px;
		color: #c33;
	}

	#login .inner .text_ {
		width: 140px;
	}

	#login .inner .chk {
		height: 12px;
	}
	</style>
</head>

<body>
<div id='login'>
	<div class='inner'>
	<g:if test="${flash.params.info == 'Creacion de usuario exitoso.'}">
		<div class='fheader'>Creacion de Usuario:</div>
	</g:if>
	<g:else>
		<div class='fheader'><g:message code="springSecurity.login.header"/></div>
	</g:else>
		
		<g:if test='${flash.params.error}'>
			<div class='login_message'>${flash.params.error}</div>
		</g:if>
		<g:if test='${flash.params.info && flash.params.info != 'Creacion de usuario exitoso.'}'>
			<div class='message'>${flash.params.info}</div>
		</g:if>
		<g:if test='${flash.params.nuevoCodigo}'>
			<div class='message'><g:link controller="player" action="crearNuevoCodigo">Enviar Nuevo Codigo</g:link></div>
		</g:if>
		<g:if test='${flash.params.nuevaClave}'>
			<div class='message'><g:link controller="cuenta" action="inicioGenerarCodigoDesbloqueo">Cambiar Clave</g:link></div>
		</g:if>
		
		<g:if test="${flash.params.info == 'Creacion de usuario exitoso.'}">
			<div class='message'>${flash.params.info}. Verifique su EMAIL para poder activar su cuenta.</div>
		</g:if>
		<g:else>
			<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
			<p>
				<label for='username'><g:message code="springSecurity.login.username.label"/>:</label>
				<input type='text' class='text_' name='j_username' id='username' required="required"/>
			</p>

			<p>
				<label for='password'><g:message code="springSecurity.login.password.label"/>:</label>
				<input type='password' class='text_' name='j_password' id='password' required="required"/>
				<center><input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}'/></center>
				<br/>
				<center><g:link controller="player" action="create">Registrar nueva Cuenta</g:link></center>
				<center><g:link controller="cuenta" action="inicioGenerarCodigoDesbloqueo">¿Olvidaste tu Contraseña?</g:link></center>
			</p>
		</form>
		</g:else>
	</div>
</div>
<script type='text/javascript'>
(function() {
	document.forms['loginForm'].elements['j_username'].focus();
})();
</script>
</body>
</html>