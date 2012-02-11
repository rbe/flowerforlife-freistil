/*
 * Flux
 * Copyright (C) 2009-2010 Informationsysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.flux.helper

import grails.converters.JSON
import javax.servlet.http.HttpServletResponse

/**
 * Base class for our controllers.
 * @author rbe
 */
abstract class ControllerBase {

    /**
     * Characters unwanted in request parameters.
     */
    String[] requestParameterChars = ('a'..'z').toList() + ('A'..'Z').toList() + (0..9).toList() + '%'

    /**
     *
     */
    void dumpconfig() {
        StringBuilder sb = new StringBuilder()
        Map c = grailsApplication.config
        c.each { k, v ->
            sb << "${k} = ${v}<br/>\n"
        }
        render sb.toString()
    }

    /**
     * Check characters of input against list of eligible characters.
     * @param input
     * @return
     */
    String checkRequestParameterChars(String input) {
        String output = input.collect { c ->
            if (c in requestParameterChars) {
                c
            } else {
                ''
            }
        }.join('')
        output
    }

    /**
     *
     * @param data
     */
    def renderAnswerAsJSON(HttpServletResponse response, data, contentType = 'text/json') {
        // Default map
        Map json = [success: [number: 0, message: 'No message.'], error: [number: 0, message: 'No message.']]
        // Success
        if (data.success) {
            json.success += data.success
        } else {
            json.remove('success')
        }
        // Error
        if (data.error) {
            json.error += data.error
        } else {
            json.remove('error')
        }
        // Write answer to stream
        response.contentType = contentType
        /*
         new StringWriter().with { writer ->
         StreamingJsonBuilder jsonBuilder = new StreamingJsonBuilder(writer, json)
         response.contentLength = writer.buffer.length()
         response.writer.write(writer.toString())
         }
         */
        /*
         JsonBuilder jsonBuilder = new JsonBuilder(json)
         response.contentLength = jsonBuilder.toString().length()
         response.writer.write(jsonBuilder.toString())
         */
        render json as JSON
    }

    /**
     *
     * @param response
     * @param image
     */
    void renderImage(HttpServletResponse response, File file) {
        try {
            if (file.exists() && file.canRead()) {
                // Decompose filename
                Map decomposedFilename = FileHelper.decomposeFilename(file)
                // Set content type
                String contentType = null
                switch (decomposedFilename.ext) {
                    case 'tif':
                        contentType = "tiff"
                        break
                    case 'jpg':
                        contentType = "jpeg"
                        break
                    default:
                        contentType = decomposedFilename.ext.toLowerCase()
                }
                response.contentType = "image/${contentType}"
                log.info "ControllerBase.renderImage: Streaming image ${file.absolutePath} as ${response.contentType}"
                // Just stream 'inline'
                // BUG Chrome 16.0.912.75: No comma in filename, http://www.google.com/support/forum/p/Chrome/thread?tid=1e87c7cc5c85b8b1&hl=en
                String fname = decomposedFilename.name.replaceAll(',', '')
                response.setHeader('Content-Disposition', "inline; filename=${fname}.${contentType}")
                // Push bytes to stream
                response.contentLength = file.size()
                response.outputStream << file.bytes
            } else {
                // TODO Stream error image
                //response.outputStream << new File().bytes
            }
        } catch (Exception e) {
            e.printStackTrace()
            // TODO Stream error image
            //response.outputStream << new File().bytes
        } finally {
            // Flush stream
            response.outputStream.flush()
        }
    }

}
