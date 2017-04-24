<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
</head>

<body>
<div id='login'>
	<div class='inner'>
		<div class='fheader'>Generar Invitaciones a Torneo
		<br/>
		<label class="informativo">Invitacion a TORNEO.</label>
		</div>
		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>
		<g:if test='${flash.params.error}'>
			<div class='login_message'>${flash.params.error}</div>
		</g:if>
		<g:if test='${flash.params.info}'>
			<div class='message'>${params.info}</div>
		</g:if>

		<form action='nuevoCodigo' controller="usuario" method='POST' class='cssform' autocomplete='off'>
		
			<p>
				<label for='email'>Ingrese su email:</label>
				<input type='text' class='text_' name='email' id='email' required="required"/>
			</p>
			<p>
				<g:link class="btn btn-default" action="index" controller="logout">Cancelar</g:link>
				<input type='submit' id="submit" class="btn btn-default" value='Enviar Codigo' onclick="waitingDialog.show('Cargando...', {dialogSize: 'sm', progressType: 'success'});"/>
			</p>
		</form>
	</div>
</div>
</body>
</html>
