<%@ page import="com.caseritos.Usuario" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>Principal</title>
	</head>
	<body>
		<div id="edit-usuario" class="content scaffold-edit" role="main">
			<h1>Mis Acciones</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:if test='${flash.params.error}'>
				<div class='message'>${flash.params.error}</div>
			</g:if>
			<g:if test="${flash.params.info}">
				<div class="message" role="status">${flash.params.info}</div>
			</g:if>
			
		<div class="fieldcontain ${hasErrors(bean: usuarioInstance, field: 'username', 'error')} required">
			<label>Mis Sanwiches</label>
			<br/>
			<label>Proveedores</label>
			<br/>
			<label>Usuarios</label>
			<br/>
			<label>Facturacion</label>
			<br/>
			<label>Ingredientes</label>
		</div>
		</div>
	</body>
</html>
