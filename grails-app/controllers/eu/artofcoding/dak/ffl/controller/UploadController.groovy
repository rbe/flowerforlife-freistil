/*
 * DAK Flower for Life
 * Copyright (C) 2011 art of coding UG (haftungsbeschränkt).
 *
 * Nutzungslizenz siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl.controller

import eu.artofcoding.dak.ffl.domain.Image
import eu.artofcoding.dak.ffl.helper.ControllerBase
import grails.converters.JSON
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

/**
 * 
 */
class UploadController extends ControllerBase {
    
    /**
     * 
     */
    def index = {
    }
    
    /**
     * 
     */
    def file = {
        // Accept multipart requests
        if (request instanceof MultipartHttpServletRequest) {
            // Email address
            String email = params['email'] // TODO Verify via SMTP, cache result, implement as separate service
            // The image
            MultipartFile file = request.getFile('bild')
            if (!file.empty) {
                // Get extension
                String ext = file.originalFilename.split('[.]')[-1]
                if (ext in fflConfig.upload.allowed.fileTypes) {
                    // Where to put uploaded file?
                    File uploadedFile = new File("${fflConfig.upload.tempdir}/${fflConfig.freistil.actualContest}", File.createTempFile('ffl_', ".${ext}").name)
                    uploadedFile.mkdirs()
                    file.transferTo(uploadedFile)
                    //
                    def img = new Image(
                        name: file.originalFilename,
                        display: true,
                        contest: fflConfig.freistil.actualContest
                    )
                    img.save(flush: true)
                    println img
                    // Return information about upload process
                    Map answer = [a: 1, file: file.originalFilename] // Save information, email on request, implement separate service
                    render answer as JSON
                } else {
                    render "File type ${ext} of ${file.originalFilename} not supported. Supported types are ${fflConfig.allowed.fileTypes}"
                }
            } else {
                render "File empty"
            }
        } else {
            render "No multipart request."
        }
    }
    
}
