<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
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
			<div class='message'><g:link controller="usuario" action="crearNuevoCodigo">Enviar Nuevo Codigo</g:link></div>
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
				<center><input type='submit' id="submit" class="btn btn-default" value='Iniciar Sesion' onclick="checkInput();"/></center>
				<br/>
				<center><g:link controller="usuario" action="create" onclick="waitingDialog.show('Cargando...', {dialogSize: 'sm', progressType: 'success'});">Registrar nueva Cuenta</g:link></center>
				<center><g:link controller="cuenta" action="inicioGenerarCodigoDesbloqueo" onclick="waitingDialog.show('Cargando...', {dialogSize: 'sm', progressType: 'success'});">¿Olvidaste tu Contraseña?</g:link></center>
			</p>
		</form>
		</g:else>
	</div>
</div>
<script type='text/javascript'>
function checkInput(){
	if(document.querySelector('#username').validity.valid && document.querySelector('#password').validity.valid){
		waitingDialog.show('Cargando...', {dialogSize: 'sm', progressType: 'success'});
	}
}
(function() {
	document.forms['loginForm'].elements['j_username'].focus();
})();
</script>
</body>
</html>
