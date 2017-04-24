package com.caseritos

import static org.springframework.http.HttpStatus.*
import org.apache.commons.logging.*
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)

class UsuarioController {
	
	private static Log log = LogFactory.getLog("caseritos."+UsuarioController.class.getName())
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def mailService
	def springSecurityService
	
	@Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Usuario.list(params), model:[usuarioInstanceCount: Usuario.count()]
    }
	
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def indexUsuario(){
		if(!flash.params){
			def mensaje = ""
			flash.params = [error:mensaje, info:mensaje]
		}
	}

	@Secured(['permitAll'])
    def show(Usuario usuarioInstance) {
		if(usuarioInstance.id == springSecurityService.getCurrentUser().id){
			respond usuarioInstance
		}else{
			flash.params = [error:"No tienes permisos suficientes para modificar la cuenta de otro usuario."]
			redirect action:"indexUsuario"
		}
    }

	@Secured(['permitAll'])
    def create() {
        respond new Usuario(params)
    }

	@Secured(['permitAll'])
    @Transactional
    def save(Usuario usuarioInstance) {
        if (usuarioInstance == null) {
			log.error "[save] Error Instancia null"
            notFound()
            return
        }

        if (usuarioInstance.hasErrors()) {
			usuarioInstance.errors.allErrors.each {
				log.error "[save] Error Instancia con errores: "+it
			}
            respond usuarioInstance.errors, view:'create'
            return
        }

		usuarioInstance.save flush:true
		def userRole = Role.findOrSaveWhere(authority:"ROLE_USER")
		UserRole.create(usuarioInstance, userRole, true)
		enviarCodigo(usuarioInstance)
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
				flash.params = [info:"Creacion de usuario exitoso."]
                redirect controller:"login", action:"auth"
            }
            '*' { respond usuarioInstance, [status: CREATED] }
        }
		log.info "[save] Creacion de Usuario y Rol exitoso: Username: "+usuarioInstance.username+". Email: "+usuarioInstance.email+". Rol: "+userRole.authority
    }

	@Secured(['permitAll'])
    def edit(Usuario usuarioInstance) {
		if(usuarioInstance.id == springSecurityService.getCurrentUser().id){
			respond usuarioInstance
		}else{
			log.error "[edit] El usuario: "+springSecurityService.getCurrentUser().username+" intento editar a otro usuario: "+ usuarioInstance.id
			flash.params = [error:"No te pases de vivo... no puedes editar el usuario de otro."]
			redirect action:"indexUsuario"
		}
		
        
    }

	@Secured(['permitAll'])
    @Transactional
    def update(Usuario usuarioInstance) {
        if (usuarioInstance == null) {
            notFound()
            return
        }

        if (usuarioInstance.hasErrors()) {
			usuarioInstance.errors.allErrors.each {
				log.error "[update] Error Instancia con errores: "+it
			}
            respond usuarioInstance.errors, view:'edit'
            return
        }

		usuarioInstance.save flush:true
		
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Usuario.label', default: 'Usuario'), usuarioInstance.id])
                redirect usuarioInstance
            }
            '*'{ respond usuarioInstance, [status: OK] }
        }
		log.info "[update] El usuario: "+springSecurityService.getCurrentUser().username+" Se edito correctamente."
    }

	@Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Usuario usuarioInstance) {

        if (usuarioInstance == null) {
            notFound()
            return
        }

		usuarioInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

	@Secured(['permitAll'])
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	@Secured(['permitAll'])
	def enviarCodigo(Usuario instanceUsuario) {
		mailService.sendMail {
			multipart true
			to instanceUsuario.email.toString()
			from "torneo.meli.academy@gmail.com"
			subject "SANWICHES CASERITOS - Codigo de Confirmacion de Cuenta"
			html g.render(template:'/mail/template', model:[instanceUsuario:instanceUsuario])
			inline 'springsourceInlineImage', 'image/jpg', new File('./grails-app/assets/images/banner_caseritos.jpg')
		}
	
		log.info "[enviarCodigo] Codigo de Confirmacion enviado correctamente: "+ instanceUsuario.email+". Token: "+instanceUsuario.confirmCode
	}
	
	@Transactional
	@Secured(['permitAll'])
	def confirm(String id){
//		log.info "Cuenta activada correctamente: "+instanceUsuario.username
		session.invalidate()	
		Usuario instanceUsuario = Usuario.findByConfirmCode(id)

		if(!instanceUsuario){
			log.error "[confirm] El usuario se encuentra deshabilitado, ID: "+id
			def mensaje = 'El usuario se encuentra desabilitado, Verifique su email para activar su cuenta o Solicite un nuevo Codigo'
			flash.params = [error:mensaje, nuevoCodigo:"si"]

			redirect action:"auth", controller:"login"
		}

		if(instanceUsuario.enabled){
			def mensaje = 'La cuenta ya se encuentra habilitada.'
			flash.params = [error:mensaje]
			log.error "[confirm] El usuario ya se encontraba habilitado, ID: "+id
			redirect action:"auth", controller:"login"
		}
		
		if(!instanceUsuario.enabled){
			instanceUsuario.enabled = true;
			instanceUsuario.confirmCode = ""
			if (!instanceUsuario.save(flush: true)) {
				def mensaje = 'Contactese con un administrador, error al Confirmar su cuenta.'
				flash.params = [error:mensaje]
				log.error "[confirm] ERROR critico al intentar habilitar el usuario, ID: "+id

				redirect action:"auth", controller:"login"
			} else{
				def mensaje = 'Usuario Activado exitosamente.'
				flash.params = [info:mensaje]
				log.info "[confirm] Usuario Habilitado exitosamente, ID: "+id

				redirect action:"auth", controller:"login"
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
		Usuario instanceUsuario = Usuario.findByEmail(params.email)
		if(!instanceUsuario){
			def mensaje = "El email ingresado se encuentra registrado."
			log.error "[nuevoCodigo] El Email ingresado no se encuentra registrado: "+params.email
			flash.params = [error:mensaje]

				redirect action:"auth", controller:"login"
		}
		if(!instanceUsuario.enabled){
			instanceUsuario.confirmCode = UUID.randomUUID().toString()
			if (!instanceUsuario.save(flush: true)) {
				log.error "[nuevoCodigo] ERROR critico al intentar guardar el nuevo codigo. Email: "+params.email+". Usuario: "+instanceUsuario.username
				def mensaje = 'Contactese con un administrador, error al Confirmar su cuenta.'
				flash.params = [error:mensaje]

				redirect action:"auth", controller:"login"
			} else {
				enviarCodigo(instanceUsuario)
				log.info "[nuevoCodigo] Nuevo cofigo generado y enviado exitosamente. Email: "+params.email+". Usuario: "+instanceUsuario.username
				def mensaje = 'Nuevo cofigo de confirmacion Enviado correctamente.'
				flash.params = [info:mensaje]

				redirect action:"auth", controller:"login"
			}
		}else{
			log.error "[nuevoCodigo] El usuario ya se encuentra registrado. Usuario: "+instanceUsuario.username
			def mensaje = 'El usuario ya se encuentra Activado no es necesario el codigo.'
			flash.params = [info:mensaje]

			redirect action:"auth", controller:"login"
		}
	}
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def datosUsuario(){
		Usuario usuarioInstance = Usuario.findById(springSecurityService.getCurrentUser().id)
		[usuarioInstance:usuarioInstance]
	}
}
