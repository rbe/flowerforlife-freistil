class BootStrap {
    
    def grailsApplication
    
    def init = { servletContext ->
        /*
        for (dc in grailsApplication.domainClasses) {
            println "Injecting: ${dc}"
            dc.clazz.metaClass.getGrailsApplication = { -> grailsApplication }
            dc.clazz.metaClass.static.getGrailsApplication = { -> grailsApplication }
        }
        */
    }
    
    def destroy = {
    }
}
