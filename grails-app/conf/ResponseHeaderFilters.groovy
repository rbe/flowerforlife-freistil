/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */

/**
 * 
 * @author rbe
 */
class ResponseHeaderFilters {
    
    def filters = {
        /*
        // Filter for JavaScript resources
        imageFilter(uri: '/image/**') {
            before = {
                println log.name
                println "imageFilter.before: Setting reponse header"
                response.setHeader('Cache-Control', 'max-age=3600, must-revalidate')
            }
            after = { Map model ->
                println "imageFilter.after: Setting reponse header"
                response.setHeader('Cache-Control', 'max-age=3600, must-revalidate')
            }
            afterView = { Exception e ->
                println "imageFilter.afterView: Setting reponse header"
                response.setHeader('Cache-Control', 'max-age=3600, must-revalidate')
            }
        }
        */
        /*
        // Filter for JavaScript resources
        cssFilter(regex: '**.js') {
            before = {
                println "cssFilter.before: Setting reponse header, ${request.uri}"
                response.setHeader('Cache-Control', 'max-age=3600, must-revalidate')
            }
            after = { Map model ->
                println "cssFilter.after: Setting reponse header, ${request.uri}"
                response.setHeader('Cache-Control', 'max-age=3600, must-revalidate')
            }
            afterView = { Exception e ->
                println "cssFilter.afterView: Setting reponse header, ${request.uri}"
                response.setHeader('Cache-Control', 'max-age=3600, must-revalidate')
            }
        }
        */
    }
    
}
