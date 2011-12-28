/*
 * DAK Flower for Life
 * Copyright (C) 2011 art of coding UG (haftungsbeschränkt).
 *
 * Nutzungslizenz siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl.controller

import eu.artofcoding.dak.ffl.helper.ControllerBase
import grails.converters.JSON

/**
 * 
 */
class StreamController extends ControllerBase {
    
    /**
     * 
     */
    def index = {
    }
    
    /**
     * 
     */
    def file = {
        if (params.id) {
            File file = new File(fflConfig.upload.tempdir, "ffl_${params.id}.png")
            if (file.exists()) {
                def ext = file.name.split('[.]')[-1]
                response.contentType = "image/${ext}"
                //response.setHeader("Content-Disposition", "attachment; filename=${file.name}")
                response.outputStream << file.bytes
                response.outputStream.flush()
            }
        }
    }
    
}
