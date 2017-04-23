<div>
    <h1> RECUPERACION DE CLAVE - Registro CASERITOS SANWICHES</h1>
    <br/>
    <p><h2>Usuario: ${instancePlayer.username}</h2></p>
    <p><h2>Codigo de Cambio: ${codigo}</h2></p>
    <p><g:link absolute="true" controller="cuenta" action="confirmarCodigoDesbloqueo" params="['usuario': instancePlayer.username, 'codigo':instancePlayer.codigoDesbloqueo]"><h3>Haga click aqui para cambiar su contraseña</h3></g:link></p>
    <br/>
    <img src="cid:springsourceInlineImage" />
</div>