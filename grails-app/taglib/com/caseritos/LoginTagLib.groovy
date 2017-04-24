package com.caseritos


class LoginTagLib {
	
    static defaultEncodeAs = [taglib:'text']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
	def springSecurityService
	def loginControl = {
		out << "<fieldset class=\"buttons\" align=\"right\">"
		if(springSecurityService.getCurrentUser() != null){
		
//		out << "<g:set var='x' value='"+${springSecurityService.getCurrentUser()}+"'/>"
		out << "<strong>Bienvenido:</strong> <u>${link(action:"edit", controller:"Usuario", id:"${springSecurityService.getCurrentUser().id}"){springSecurityService.getCurrentUser().username}}</u>"
		out << """${link(action:"index", controller:"logout"){"Cerrar Sesión"}}"""
		out << "<input type='hidden' value='${springSecurityService.getCurrentUser().id}' id='id_conectado'/>"
		out << "<input type='hidden' value='${springSecurityService.getCurrentUser().username}' id='username_conectado'/>"
		out << "<input type='hidden' value='${springSecurityService.getCurrentUser().email}' id='email_conectado'/>"
		out << "<input type='hidden' value='${springSecurityService.getCurrentUser().nombre}' id='nombre_conectado'/>"
		out << "<input type='hidden' value='${springSecurityService.getCurrentUser().apellido}' id='apellido_conectado'/>"
		out << " "
		}
		out << "</fieldset>"
	  }
}

