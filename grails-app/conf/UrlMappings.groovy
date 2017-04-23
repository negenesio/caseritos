class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        name usuario_perfil: "/usuario/perfil/$id"(controller: "player", parseRequest: true){
            action = [GET: "show"]
        }

        "/"(controller:'login', action:'auth')
        "500"(view:'/error')
	}
}
