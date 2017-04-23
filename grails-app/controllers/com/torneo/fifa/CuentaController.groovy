package com.torneo.fifa

import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import com.torneo.fifa.Player

class CuentaController {
	def mailService
	def springSecurityService

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
		Player instancePlayer = Player.findByEmail(params.email)
		if(!instancePlayer){
			log.error "[generarCodigoDesbloqueo] El Email ingresado no se encuentra registrado: "+params.email
			def mensaje = "El E-MAIL no se encuentra registrado."
			flash.params = [error:mensaje]
			redirect action:"inicioGenerarCodigoDesbloqueo", controller:"cuenta"
			return
		}
		if(instancePlayer){
			char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			StringBuilder sb = new StringBuilder()
			Random random = new Random()
			for (int i = 0; i < 6; i++) {
				char c = chars[random.nextInt(chars.length)]
				sb.append(c)
			}
			String output = sb.toString();
			instancePlayer.codigoDesbloqueo = output
			instancePlayer.passwordExpired = true
			if(!instancePlayer.save(flush:true)){
				log.error "[generarCodigoDesbloqueo] ERROR critico, no se puedo guardar el Nuevo codigo generado para el usuario: "+instancePlayer.username+"."
				def mensaje = "Error en la configuracion de su cuenta. Comuniquese con un Administrador"					
				flash.params = [error:mensaje]
				redirect action:"auth", controller:"login"
				return
			}else{
				
				log.info "[generarCodigoDesbloqueo] Nuevo cofigo generado para el usuario: "+instancePlayer.username+". Se envio por email correctamente."
				enviarNuevaCodigo(instancePlayer, output)
				def mensaje = "Nuevo Codigo de Cambio generado correctamente, Verifique su Email"
				flash.params = [info:mensaje, usuario:instancePlayer.username]
				redirect action:"confirmarCodigoDesbloqueo", controller:"cuenta"
				return
			}
		}
	}

	//Enviar email con CODIGO DE DESBLOQUEO para acceder al cambio de contraseña.
	@Secured(['permitAll'])
	def enviarNuevaCodigo(Player instancePlayer, String codigo){
		mailService.sendMail {
			multipart true
			to instancePlayer.email
			subject "TORNEO MELI - RECUPERAR PASSWORD"
			html g.render(template:'/mail/recuperarClave', model:[instancePlayer:instancePlayer, password:instancePlayer.password, codigo:codigo])
			inline 'springsourceInlineImage', 'image/jpg', new File('./web-app/images/banner_caseritos.jpg')
		}
		log.info "[enviarNuevaCodigo] Nuevo codigo enviado al usuario:"+instancePlayer.username+". Email: "+instancePlayer.email
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
		Player instancePlayer = Player.findByUsername(params.usuario)
		if(!instancePlayer){
			log.error "[cambiarContraseña] El usuario ingresado: "+params.usuario+" no existe. Usuario Actual: "+instancePlayer.username
			def mensaje = "Error en la configuracion de su cuenta. Comuniquese con un Administrador"
			flash.params = [error:mensaje]
			redirect action:"auth", controller:"login"
			return
		}
		if(instancePlayer){
			if(!instancePlayer.passwordExpired){
				log.error "[cambiarContraseña] El cambio de clave para el usuario: "+params.usuario+", El usuario actual: "+instancePlayer.username+". Podria estar intentando hackear la cuenta."
				def mensaje = "Su contraseña ya fue modificada."
				flash.params = [error:mensaje]
				redirect action:"auth", controller:"login"
				return
			}
			if(instancePlayer.passwordExpired){
				if(instancePlayer.codigoDesbloqueo == params.codigo){
					instancePlayer.passwordExpired = false
					instancePlayer.password = params.password
					instancePlayer.codigoDesbloqueo = ""
					if(!instancePlayer.save(flush:true)){
						log.error "[cambiarContraseña] ERROR critico al intentar guardar el cambio de contraseña para el usuario: "+instancePlayer.username
						def mensaje = "Error en la configuracion de su cuenta. Comuniquese con un Administrador"
						flash.params = [error:mensaje]
						redirect action:"auth", controller:"login"
						return
					}else{
						log.info "[cambiarContraseña] El cambio de clave se realizo exitosamente para el usuario: "+instancePlayer.username
						def mensaje = "Contraseña actualizada correctamente."
						flash.params = [info:mensaje]
						redirect action:"auth", controller:"login"
						return
					}
				}else{
					log.error "[cambiarContraseña] El codigo de desbloqueo ingresado no corresponde con el usuario actual. usuario ingresado: "+params.username+". Usuario Actual: "+instancePlayer.username
					def mensaje = "Verifique el codigo de desbloqueo."
					flash.params = [error:mensaje, usuario:instancePlayer.username, codigo:params.codigo]
					redirect action:"confirmarCodigoDesbloqueo", controller:"cuenta"
					return
				}
			}
		}
	}
}
