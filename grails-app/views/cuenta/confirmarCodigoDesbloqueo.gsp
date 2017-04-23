<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
	<style type='text/css' media='screen'>
	#login {
		margin: 10px;
		padding: 0px;
		text-align: center;
	}

	#login .inner {
		width: 470px;
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
		color: #e6ecff;
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
		width: 120px;
	}
	#login .inner .cssform input[type='email'] {
		width: 210px;
	}

	#login .inner .cssform label {
		font-weight: bold;
		float: left;
		text-align: right;
		margin-left: -105px;
		width: 250px;
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
		width: 120px;
	}

	#login .inner .chk {
		height: 12px;
	}
	
	.informativo {
		font-size: 12px;
		color: blue;
	}
	
	
	</style>
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
				<g:if test='${params.usuario}'>
					<input type='text' class='text_' name='usuario' id='usuario' required="required" value="${params.usuario}"/>
				</g:if>

			</p>
			<p>
				<label for='codigo'>Ingrese Codigo de Cambio:</label>
				<g:if test='${flash.params.usuario}'>
					<input type='text' class='text_' name='codigo' id='codigo' required="required" value="${flash.params.codigo}"/>
				</g:if>
				<g:if test='${params.usuario}'>
					<input type='text' class='text_' name='codigo' id='codigo' required="required" value="${params.codigo}"/>
				</g:if>
				
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
				<input type='submit' id="submit" value='Recuperar Cuenta' onclick="checkInput();"/>
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

alert("${flash.params}")
</script>
</body>
</html>
