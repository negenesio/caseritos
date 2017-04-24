<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
</head>

<body>
<div id='login'>
	<div class='inner'>
		<div class='fheader'>Recuperar/Cambiar Contraseña
		<br/>
		<label class="informativo">Se enviara un "CODIGO DE CAMBIO" a su cuenta de email.</label>
		</div>
		<g:if test='${flash.message}'>
			<div class='login_message'><strong>${flash.message}</strong></div>
		</g:if>
		<g:if test="${flash.params.error}">
			<div class='login_message'><strong>${flash.params.error}</strong></div>
		</g:if>
		<g:if test="${flash.params.info}">
			<div class='message'><strong>${flash.params.info}</strong></div>
		</g:if>
		
		<form name="formulario" id="formulario" action='generarCodigoDesbloqueo' controller="cuenta" method='POST' class='cssform' autocomplete='off'>
			<p>
				<label for='email'>Ingrese su email:</label>
				<input type='email' class='text_' name='email' id='email' required="required"/>
			</p>
			<%--<p>
				<label for='usuario'>Ingrese su usuario:</label>
				<input type='text' class='text_' name='usuario' id='usuario' required="required"/>
			</p>--%>
			<p>
				<a href="/caseritosV3/" class="btn btn-default">Volver</a>
				<input type='submit' id="submit" class="btn btn-default" value='Enviar Codigo' onclick="checkInput();"/>
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
