import com.caseritos.Player
import com.caseritos.Role
import com.caseritos.UserRole

class BootStrap {

    def init = { servletContext ->
		
		def adminRole = Role.findOrSaveWhere(authority:"ROLE_ADMIN")
		def userAdmin = Player.findOrSaveWhere(username:"admin", password:"admin", nombre:"admin", apellido:"admin", email:"nicolas.genesio@gmail.com", fechaNacimiento:new Date(), fechaCreacion:new Date(), enabled:true, codigoDesbloqueo:"123")
		def userRole = Role.findOrSaveWhere(authority:"ROLE_USER")
		def userUser = Player.findOrSaveWhere(username:"nicogenesio", password:"nicogenesio", nombre:"Nicolas", apellido:"Genesio", email:"nicolas.genesio@mercadolibre.com", fechaNacimiento:new Date(), fechaCreacion:new Date(), codigoDesbloqueo:"123", enabled:true)
		
		if(!userAdmin.authorities.contains(adminRole)){
			UserRole.create(userAdmin, adminRole, true)
		}
		if(!userUser.authorities.contains(userRole)){
			UserRole.create(userUser, userRole, true)
		}
		
    }
    def destroy = {
    }
}
