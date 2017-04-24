<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
</head>

<body>
<div id='login'>
	<div class='inner'>
		<div class='fheader'>Confirmar Codigo de Cambio:</div>
		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>
		<g:if test='${flash.params.error}'>
			<div class='login_message'>${flash.params.error}</div>
		</g:if>
		<g:if test='${flash.params.info}'>
			<div class='message'>${flash.params.info}</div>
		</g:if>
		
		<form action=cambiarContraseña controller="cuenta" method='POST' class='cssform' autocomplete='off'>
			<p>
				<label for='usuario'>Ingrese su Usuario:</label>
				<g:if test='${flash.params.usuario}'>
					<input type='text' class='text_' name='usuario' id='usuario' required="required" value="${flash.params.usuario}"/>
				</g:if>
				<g:else>
					<input type='text' class='text_' name='usuario' id='usuario' required="required" value="${params.usuario}"/>
				</g:else>

			</p>
			<p>
				<label for='codigo'>Ingrese Codigo de Cambio:</label>
				<g:if test='${flash.params.usuario}'>
					<input type='text' class='text_' name='codigo' id='codigo' required="required" value="${flash.params.codigo}"/>
				</g:if>
				<g:else>
					<input type='text' class='text_' name='codigo' id='codigo' required="required" value="${params.codigo}"/>
				</g:else>
				
			</p>
			<p>
				<label for='password'>Contraseña Nueva:</label>
				<input type='password' class='text_' name='password' id='password' required="required" value=""/>
			</p>
			<p>
				<label for='passwordConfirm'>Confirmar Contraseña:</label>
				<input type='password' class='text_' name='passwordConfirm' id='passwordConfirm' required="required" value=""/>
			</p>
			<p>
			<p>
				<a href="/caseritosV3/" class="btn btn-default" id="volver">Volver</a>
				<input type='submit' class='btn btn-default' id="submit" value='Recuperar Cuenta' onclick="checkInput();"/>
			</p>
		</form>
	</div>
</div>
<script>

if(document.querySelector('#passwordConfirm').validity.valid && document.querySelector('#password').validity.valid && document.querySelector('#codigo').validity.valid &&  document.querySelector('#usuario').validity.valid){
	waitingDialog.show('Cargando...', {dialogSize: 'sm', progressType: 'success'});
}
var password = document.getElementById("password");
var confirm_password = document.getElementById("passwordConfirm");

function validatePassword(){
	if(password.value != confirm_password.value) {
		confirm_password.setCustomValidity("Las contraseñas no coinciden.");
	} else {
  		confirm_password.setCustomValidity('');
	}
}
password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;
</script>
</body>
</html>
