package com.caseritos

class Player extends User{
	
	String nombre
	String apellido
	String email
	String codigoDesbloqueo
	Date fechaCreacion = new Date()
	Date fechaNacimiento = new Date()
	String confirmCode = UUID.randomUUID().toString()
	
    static constraints = {
		email email: true, blank: false, unique: true		
    }
}
