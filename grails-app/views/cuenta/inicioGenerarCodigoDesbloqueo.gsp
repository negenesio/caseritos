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
		width: 450px;
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
		<div class='fheader'>Recuperar/Cambiar Contraseña
		<br/>
		<label class="informativo">Se enviara un "CODIGO DE CAMBIO" a su cuenta de email.</label>
		</div>
		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>
		<g:if test="${flash.params.error}">
			<div class='login_message'>${flash.params.error}</div>
		</g:if>
		<g:if test="${flash.params.info}">
			<div class='message'>${flash.params.info}</div>
		</g:if>
		
		<form name="formulario" id="formulario" action='generarCodigoDesbloqueo' controller="cuenta" method='POST' class='cssform' autocomplete='off'>
			<p>
				<label for='email'>Ingrese su email:</label>
				<input type='email' class='text_' name='email' id='email' required="required"/>
			</p>
<%--			<p>--%>
<%--				<label for='usuario'>Ingrese su usuario:</label>--%>
<%--				<input type='text' class='text_' name='usuario' id='usuario' required="required"/>--%>
<%--			</p>--%>
			<p>
				<input type='submit' id="submit" value='Enviar Codigo' onclick="checkInput();"/>
			</p>
		</form>
	</div>
</div>
<script>
function checkInput(){
	
	if(document.querySelector('#email').validity.valid){
		waitingDialog.show('Cargando...', {dialogSize: 'sm', progressType: 'success'});
	}
}
</script>
</body>
</html>
