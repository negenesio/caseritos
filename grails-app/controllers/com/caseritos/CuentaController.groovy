package com.caseritos

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

class CuentaController {
	def mailService
	private static Log log = LogFactory.getLog("caseritos."+CuentaController.class.getName())
	def index() { }

	//Solicita usuario y Email. envia los datos a : recuperarClave
	@Secured(['permitAll'])
	def inicioGenerarCodigoDesbloqueo(){
		if(!flash.params){
			def mensaje = ""
			flash.params = [error:mensaje, info:mensaje]
		}
	}

	//Verifica Usuario y Email ingresados, genera CODIGO DE DESBLOQUEO. Envia el Email con el CODIGO GENERADO.
	@Secured(['permitAll'])
	@Transactional
	def generarCodigoDesbloqueo(){
		Usuario instanceUsuario = Usuario.findByEmail(params.email)
		if(!instanceUsuario){
			log.error "[generarCodigoDesbloqueo] El Email ingresado no se encuentra registrado: "+params.email
			def mensaje = "El E-MAIL no se encuentra registrado."
			flash.params = [error:mensaje]

			redirect action:"inicioGenerarCodigoDesbloqueo", controller:"cuenta"
		}
		if(instanceUsuario){
			char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			StringBuilder sb = new StringBuilder()
			Random random = new Random()
			for (int i = 0; i < 6; i++) {
				char c = chars[random.nextInt(chars.length)]
				sb.append(c)
			}
			String output = sb.toString();
			instanceUsuario.codigoDesbloqueo = output
			instanceUsuario.passwordExpired = true
			if(!instanceUsuario.save(flush:true)){
				log.error "[generarCodigoDesbloqueo] ERROR critico, no se puedo guardar el Nuevo codigo generado para el usuario: "+instanceUsuario.username+"."
				def mensaje = "Error en la configuracion de su cuenta. Comuniquese con un Administrador"					
				flash.params = [error:mensaje]

				redirect action:"auth", controller:"login"
			}else{
				log.info "[generarCodigoDesbloqueo] Nuevo cofigo generado para el usuario: "+instanceUsuario.username+". Se envio por email correctamente."
				enviarNuevaCodigo(instanceUsuario, output)
				def mensaje = "Nuevo Codigo de Cambio generado correctamente, Verifique su Email"
				flash.params = [info:mensaje, usuario:instanceUsuario.username]

				redirect action:"confirmarCodigoDesbloqueo", controller:"cuenta"
			}
		}
	}

	//Enviar email con CODIGO DE DESBLOQUEO para acceder al cambio de contraseña.
	@Secured(['permitAll'])
	def enviarNuevaCodigo(Usuario instanceUsuario, String codigo){
		mailService.sendMail {
			multipart true
			to instanceUsuario.email.toString()
			from "torneo.meli.academy@gmail.com"
			subject "CASERITOS SANWICHES - RECUPERAR PASSWORD"
			html g.render(template:'/mail/recuperarClave', model:[instanceUsuario:instanceUsuario, password:instanceUsuario.password, codigo:codigo])
			inline 'springsourceInlineImage', 'image/jpg', new File('./grails-app/assets/images/banner_caseritos.jpg')
		}
		log.info "[enviarNuevaCodigo] Nuevo codigo enviado al usuario:"+instanceUsuario.username+". Email: "+instanceUsuario.email
	}

	//Vista donde solicita CODIGO DE DESBLOQUEO y usuario. (El codigo es enviado por email)
	@Secured(['permitAll'])
	def confirmarCodigoDesbloqueo(){
		if(!flash.params){
			def mensaje = ""
			flash.params = [error:mensaje, info:mensaje, usuario:mensaje]
		}
	}

	@Secured(['permitAll'])
	def cambiarContraseña(){
		Usuario instanceUsuario = Usuario.findByUsername(params.usuario)
		if(!instanceUsuario){
			log.error "[cambiarContraseña] El usuario ingresado: "+params.usuario+" no existe. Usuario Actual: "+instanceUsuario.username
			def mensaje = "Error en la configuracion de su cuenta. Comuniquese con un Administrador"
			flash.params = [error:mensaje]

			redirect action:"auth", controller:"login"
		}
		if(instanceUsuario){
			if(!instanceUsuario.passwordExpired){
				log.error "[cambiarContraseña] El cambio de clave para el usuario: "+params.usuario+", El usuario actual: "+instanceUsuario.username+". Podria estar intentando hackear la cuenta."
				def mensaje = "Su contraseña ya fue modificada."
				flash.params = [error:mensaje]

				redirect action:"auth", controller:"login"
			}
			if(instanceUsuario.passwordExpired){
				if(instanceUsuario.codigoDesbloqueo == params.codigo){
					instanceUsuario.passwordExpired = false
					instanceUsuario.password = params.password
					instanceUsuario.codigoDesbloqueo = ""
					if(!instanceUsuario.save(flush:true)){
						log.error "[cambiarContraseña] ERROR critico al intentar guardar el cambio de contraseña para el usuario: "+instanceUsuario.username
						def mensaje = "Error en la configuracion de su cuenta. Comuniquese con un Administrador"
						flash.params = [error:mensaje]

						redirect action:"auth", controller:"login"
					}else{
						log.info "[cambiarContraseña] El cambio de clave se realizo exitosamente para el usuario: "+instanceUsuario.username
						def mensaje = "Contraseña actualizada correctamente."
						flash.params = [info:mensaje]

						redirect action:"auth", controller:"login"
					}
				}else{
					log.error "[cambiarContraseña] El codigo de desbloqueo ingresado no corresponde con el usuario actual. usuario ingresado: "+params.username+". Usuario Actual: "+instanceUsuario.username
					def mensaje = "Verifique el codigo de desbloqueo."
					flash.params = [error:mensaje, usuario:instanceUsuario.username, codigo:params.codigo]

					redirect action:"confirmarCodigoDesbloqueo", controller:"cuenta"
				}
			}
		}
	}
}
