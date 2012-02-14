/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */

import eu.artofcoding.dak.ffl.ContestConfig
import eu.artofcoding.dak.ffl.ContestFileType
import eu.artofcoding.dak.ffl.FflConfig

/**
 *
 * @author rbe
 */
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
        // Common file types for images
        ['gif', 'jpg', 'jpeg', 'png', 'tif', 'tiff'].each {
            ContestFileType ft = ContestFileType.findByName(it)
            if (!ft) {
                ft = new ContestFileType(name: it)
                ft.save()
            }
        }
        // Add a contest for testing
        ['smiling_faces', 'dumb_ears'].each {
            ContestConfig cc = ContestConfig.findByContest(it)
            if (!cc) {
                cc = new ContestConfig(contest: it)
                ContestFileType.list().each { ft -> cc.addToFileTypes(ft) }
                cc.save()
                println "Created config: ${cc.dump()}"
            }
        }
        // Set actual contest
        FflConfig fc = FflConfig.findById(1)
        if (!fc) {
            fc = new FflConfig(/*FFL-2 actualContest: 'smiling_faces',*/
                    imageMagickHome: '/Users/rbe/app/ImageMagick/6.7.4',
                    uploadDirectory: 'ffltemp/smiling_faces')
            fc.save()
        }
        // Ready.
        println "Flower for Life - STARTED."
    }

    def destroy = {
        // Ready.
        println "Flower for Life - STOPPED."
    }

}
