package com.torneo.fifa

import static org.springframework.http.HttpStatus.*
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.logging.*
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import java.text.SimpleDateFormat

@Transactional(readOnly = true)

class PlayerController {
	
	private static Log log = LogFactory.getLog("torneo."+PlayerController.class.getName())
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def mailService
	def springSecurityService
	
	@Secured(['ROLE_ADMIN'])
    def index(Integer max) {
		println "el admin"
        params.max = Math.min(max ?: 10, 100)
        respond Player.list(params), model:[playerInstanceCount: Player.count()]
    }
	
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def indexPlayer(){
		if(!flash.params){
			def mensaje = ""
			flash.params = [error:mensaje, info:mensaje]
		}
	}

	@Secured(['permitAll'])
    def show(Player playerInstance) {
		if(playerInstance.id == springSecurityService.getCurrentUser().id){
			respond playerInstance
		}else{
			flash.params = [error:"No te pases de vivo... no puedes VER el usuario de otro."]
			redirect action:"indexPlayer"
		}
    }

	@Secured(['permitAll'])
    def create() {
        respond new Player(params)
    }

	@Secured(['permitAll'])
    @Transactional
    def save(Player playerInstance) {
        if (playerInstance == null) {
			log.error "[save] Error Instancia null"
            notFound()
            return
        }

        if (playerInstance.hasErrors()) {
			playerInstance.errors.allErrors.each {
				println log.error "[save] Error Instancia con errores: "+it
			}
            respond playerInstance.errors, view:'create'
            return
        }
		
        playerInstance.save flush:true
		def userRole = Role.findOrSaveWhere(authority:"ROLE_USER")
		UserRole.create(playerInstance, userRole, true)
		enviarCodigo(playerInstance)
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'player.label', default: 'Player'), playerInstance.id])
				flash.params = [info:"Creacion de usuario exitoso."]
                redirect controller:"login", action:"auth"
            }
            '*' { respond playerInstance, [status: CREATED] }
        }
		log.info "[save] Creacion de Player y Rol exitoso: Username: "+playerInstance.username+". Email: "+playerInstance.email+". Rol: "+userRole.authority
    }

	@Secured(['permitAll'])
    def edit(Player playerInstance) {
		println "EDITAR"
		if(playerInstance.id == springSecurityService.getCurrentUser().id){
			respond playerInstance
		}else{
			log.error "[edit] El usuario: "+springSecurityService.getCurrentUser().username+" intento editar a otro usuario: "+ playerInstance.id
			flash.params = [error:"No te pases de vivo... no puedes editar el usuario de otro."]
			redirect action:"indexPlayer"  
		}
		
        
    }

	@Secured(['permitAll'])
    @Transactional
    def update(Player playerInstance) {
        if (playerInstance == null) {
            notFound()
            return
        }

        if (playerInstance.hasErrors()) {
			playerInstance.errors.allErrors.each {
				log.error "[update] Error Instancia con errores: "+it
			}
            respond playerInstance.errors, view:'edit'
            return
        }

        playerInstance.save flush:true
		
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Player.label', default: 'Player'), playerInstance.id])
                redirect playerInstance
            }
            '*'{ respond playerInstance, [status: OK] }
        }
		log.info "[update] El usuario: "+springSecurityService.getCurrentUser().username+" Se edito correctamente."
    }

	@Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Player playerInstance) {

        if (playerInstance == null) {
            notFound()
            return
        }

        playerInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Player.label', default: 'Player'), playerInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

	@Secured(['permitAll'])
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'player.label', default: 'Player'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	@Secured(['permitAll'])
	def enviarCodigo(Player instancePlayer) {
		mailService.sendMail {
			multipart true
			to instancePlayer.email
			subject "TORNEO MELI - Codigo de Confirmacion de Cuenta"			
			html g.render(template:'/mail/template', model:[instancePlayer:instancePlayer])
			inline 'springsourceInlineImage', 'image/jpg', new File('./web-app/images/baner_torneo.png')
		}
	
		log.info "[enviarCodigo] Codigo de Confirmacion enviado correctamente: "+ instancePlayer.email+". Token: "+instancePlayer.confirmCode
	}
	
	@Transactional
	@Secured(['permitAll'])
	def confirm(String id){
//		log.info "Cuenta activada correctamente: "+instancePlayer.username
		session.invalidate()	
		Player instancePlayer = Player.findByConfirmCode(id)

		if(!instancePlayer){
			log.error "[confirm] El usuario se encuentra deshabilitado, ID: "+id
			def mensaje = 'El usuario se encuentra desabilitado, Verifique su email para activar su cuenta o Solicite un nuevo Codigo'
			flash.params = [error:mensaje, nuevoCodigo:"si"]
			redirect action:"auth", controller:"login"
			return
		}

		if(instancePlayer.enabled){
			def mensaje = 'La cuenta ya se encuentra habilitada.'
			flash.params = [error:mensaje]
			redirect action:"auth", controller:"login"
			log.error "[confirm] El usuario ya se encontraba habilitado, ID: "+id
			return
		}
		
		if(!instancePlayer.enabled){
			instancePlayer.enabled = true;
			instancePlayer.confirmCode = ""
			if (!instancePlayer.save(flush: true)) {
				def mensaje = 'Contactese con un administrador, error al Confirmar su cuenta.'
				flash.params = [error:mensaje]
				log.error "[confirm] ERROR critico al intentar habilitar el usuario, ID: "+id
				redirect action:"auth", controller:"login"
				return
			} else{
				def mensaje = 'Usuario Activado exitosamente.'
				flash.params = [info:mensaje]
				redirect action:"auth", controller:"login"
				log.info "[confirm] Usuario Habilitado exitosamente, ID: "+id
				return
			}
		}
	}
	
	@Secured(['permitAll'])
	def crearNuevoCodigo(){
		if(!flash.params){
			def mensaje = ""
			flash.params = [error:mensaje, info:mensaje]
		}
	}
	
	@Secured(['permitAll'])
	@Transactional
	def nuevoCodigo() {
		println "Busqueda de usuario con EMAIL"
		Player instancePlayer = Player.findByEmail(params.email)
		if(!instancePlayer){
			def mensaje = "El email ingresado se encuentra registrado."
			log.error "[nuevoCodigo] El Email ingresado no se encuentra registrado: "+params.email
			flash.params = [error:mensaje]
			redirect action:"auth", controller:"login"
			return
		} 
		if(!instancePlayer.enabled){
			instancePlayer.confirmCode = UUID.randomUUID().toString()
			if (!instancePlayer.save(flush: true)) {
				log.error "[nuevoCodigo] ERROR critico al intentar guardar el nuevo codigo. Email: "+params.email+". Usuario: "+instancePlayer.username
				def mensaje = 'Contactese con un administrador, error al Confirmar su cuenta.'
				flash.params = [error:mensaje]
				redirect action:"auth", controller:"login"
				return
			} else {
				enviarCodigo(instancePlayer)
				log.info "[nuevoCodigo] Nuevo cofigo generado y enviado exitosamente. Email: "+params.email+". Usuario: "+instancePlayer.username
				def mensaje = 'Nuevo cofigo de confirmacion Enviado correctamente.'
				flash.params = [info:mensaje]
				redirect action:"auth", controller:"login"
				return
			}
		}else{
			log.error "[nuevoCodigo] El usuario ya se encuentra registrado. Usuario: "+instancePlayer.username
			def mensaje = 'El usuario ya se encuentra Activado no es necesario el codigo.'
			flash.params = [info:mensaje]
			redirect action:"auth", controller:"login"
			return
		}
	}
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def datosUsuario(){
		Player playerInstance = Player.findById(springSecurityService.getCurrentUser().id)
		[playerInstance:playerInstance]
	}
}
