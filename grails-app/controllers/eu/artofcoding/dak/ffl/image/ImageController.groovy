/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.dak.ffl.image

import eu.artofcoding.flux.helper.ControllerBase
import eu.artofcoding.flux.helper.FileHelper
import eu.artofcoding.flux.image.imagemagick.ImageMagickService
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

/**
 * Controller for images.
 * @author rbe
 */
class ImageController extends ControllerBase {

    /**
     * Allowed methods for actions.
     */
    static allowedMethods = [
            'find': ['POST'],
            'upload': ['GET', 'POST'],
            'stream': ['GET'],
            'info': ['GET']
    ]

    /**
     * The image service.
     */
    ImageService imageService

    /**
     * The ImageMagick service.
     */
    ImageMagickService imageMagickService

    /**
     * Default index action.
     */
    def index() {
        /*
         println "session.id=${session.id}"
         println "log.name=${log.name}"
         request.each { k, v ->
         if (log.traceEnabled) log.trace "request: k=${k} v=${v}"
         }
         request.headerNames.each { n -> if (log.traceEnabled) log.trace "header: n=${n} v=${request.getHeader(n)}" }
         */
        println session
        renderAnswerAsJSON(response, [success: [message: 'Hello, this is a image service.']])
    }

    /**
     * Upload a new image.
     */
    def upload() {
        if (log.traceEnabled) log.trace "ImageController.upload: request=${request} params=${params}"
        // Create lists for contestIds
        if (!session.contestId) {
            session.contestId = []
        }
        if (!session.updatedContestId) {
            session.updatedContestId = []
        }
        if (params.contestId) session.contestId << params.contestId
        // Map for JSON answer
        Map answer = null
        // Got form data for image?
        if (session.contestId && params.image) {
            try {
                // Find image and update it with data
                def result = imageService.updateImage(session.contestId, params.image)
                if (log.traceEnabled) log.trace result
                // Remember update to contestId
                session.updatedContestId += session.contestId
                answer = [success: [message: "Updated image(s): ${session.contestId.join(', ')}"]]
                // FFL-4 Forget contestId, remove it from session as it will be removed when another image is uploaded, see below
                session.contestId.clear()
            } catch (e) {
                log.error("Cannot save image data for ${session.contestId}", e)
                answer = [error: [message: e.message]]
            }
        } else if (params.image) {
            answer = [error: [message: "No contestId, no update: params=${params.image}"]]
            log.error answer
        }
        // File upload: a multipart request
        if (request instanceof MultipartHttpServletRequest) {
            try {
                // The image
                MultipartFile file = request.getFile('file.bild')
                if (file) {
                    if (file.empty) {
                        answer = [error: [message: 'File is empty, sorry.']]
                    } else {
                        try {
                            // FFL-4 Cleanup?
                            // Remove files which were uploaded previously (but do not have data)
                            // This is related to image preview: user uploaded a file and then decided to upload another.
                            if (session.contestId.size() > 0) {
                                session.contestId.each { cid -> imageService.removeImage(cid) }
                                session.contestId = []
                            }
                            // Return information about upload process
                            answer = imageService.saveImage(file, params)
                            if (answer.success) {
                                session.contestId << answer.success.contestId
                                if (log.traceEnabled) log.trace "${session}"
                            } else {
                                if (log.traceEnabled) log.trace "ImageController.upload: answer=${answer}"
                            }
                            // Don't render JSON answer
                            answer = null
                        } catch (e) {
                            log.error(null, e)
                            answer = [error: [message: e.message]]
                        }
                    }
                }
            } catch (e) {
                log.error(null, e)
                answer = [error: [message: e.message]]
            }
        }
        // Finally render JSON answer
        if (log.traceEnabled) log.trace "ImageController.upload: answer=${answer}"
        if (answer) {
            renderAnswerAsJSON(response, answer, 'application/x-javascript')
        } // else render upload.gsp
    }

    /**
     *
     * @return
     */
    def lastContestId() {
        if (log.traceEnabled) log.trace "ImageController.lastContestId: session=${session}"
        renderAnswerAsJSON(response, [success: [contestId: session.contestId]])
    }

    /**
     * Approve an image.
     * This set attribute 'approved' of Image to true.
     */
    def approve() {
        if (params.id) {
            Map result = imageService.approveImage(params.id)
            if (log.traceEnabled) log.trace "ImageController.approve: ${result}"
            renderAnswerAsJSON(response, result)
        } else {
            renderAnswerAsJSON(response, message: 'Incorrect parameters.')
        }
    }

    /**
     * Reject an image.
     * This set attribute 'approved' of Image to false.
     */
    def reject() {
        if (params.id) {
            Map result = imageService.approveImage(params.id, false)
            renderAnswerAsJSON(response, result)
        } else {
            renderAnswerAsJSON(response, [error: [message: 'Incorrect parameters.']])
        }
    }

    /**
     * Delete an image from database and filesystem.
     */
    def remove() {
        if (params.id) {
            try {
                imageService.removeImage(params.id)
                renderAnswerAsJSON(response, [success: [message: "Image ${params.id} deleted"]])
            } catch (e) {
                renderAnswerAsJSON(response, [error: [message: "Image not deleted: ${e}"]])
            }
        } else {
            renderAnswerAsJSON(response, [error: [message: 'Incorrect parameters.']])
        }
    }

    /**
     * List all images in a contest.
     */
    def find() {
        try {
            if (log.traceEnabled) log.trace "ImageController.find: JSON=${request.JSON}"
            // Get images from database
            List<Image> images = imageService.find(request.JSON)
            // Check if files exist
            def result = images.findAll { Image img ->
                imageService.getFileForImage(img)
            }
            // Render found images as JSON
            def m = [success: [count: result.size(), result: result]]
            if (log.traceEnabled) log.trace "ImageController.find: m=${m}"
            renderAnswerAsJSON(response, m)
        } catch (ConverterException e) {
            if (log.traceEnabled) log.trace "ImageController.find: ConverterException: ${e}"
            renderAnswerAsJSON(response, [error: [message: "I talk JSON, sorry: ${e.message}"]])
        }
    }

    /**
     * Return information about image as JSON.
     */
    def info() {
        Map m = null
        if (params.id && params.ext) {
            // Get image from service
            Image image = imageService.getImage(params.id)
            if (image) {
                // Check if corresponding original file exists
                if (image?.fileExists) {
                    // Convert image??
                    File converted = convert(image)
                    // Send information back to browser
                    m = [success: [result: imageMagickService.identify(converted)]]
                } else {
                    m = [error: [message: "Image ${params.id} found in database, but original file not in place."]]
                }
            } else {
                m = [error: [message: 'Image ${params.id}.${params.ext} not found.']]
            }
        } else {
            m = [error: [message: 'Incorrect parameters.']]
        }
        renderAnswerAsJSON(response, m)
    }

    /**
     * Stream image, parameter id is used to construct filename.
     * TODO Render "error message" as image
     */
    def stream() {
        if (params.id && params.ext) {
            // Get image from service
            Image image = imageService.getImage(params.id)
            if (image) {
                // Check if corresponding original file exists
                if (image.fileExists) {
                    // Send image back to browser
                    renderImage(response, convert(image))
                } else {
                    log.error "Image ${params.id} found in database, but original file not in place."
                }
            } else {
                log.error "Image ${params.id} not found."
            }
        } else {
            log.error 'Incorrect parameters.'
        }
    }

    /**
     * Stream a thumbnail of an image, parameter id is used to construct filename.
     * TODO Render "error message" as image
     */
    def thumbnail() {
        if (params.id && params.ext) {
            // Get image from service
            Image image = imageService.getImage(params.id)
            if (image) {
                // Check if corresponding original file exists
                if (image.fileExists) {
                    // File object for original file
                    File file = imageService.getFileForImage(image)
                    // Decompose original image filename
                    Map decomposedOriginalFilename = FileHelper.decomposeFilename(file)
                    File thumbnail = new File(file.parentFile, "${decomposedOriginalFilename.name}_thumb.${params.ext}")
                    thumbnail = imageMagickService.thumbnail(file, params.width, params.height, thumbnail)
                    // Send image back to browser
                    renderImage(response, thumbnail)
                } else {
                    log.error "Image ${params.id} found in database, but original file not in place."
                }
            } else {
                log.error "Image ${params.id} not found."
            }
        } else {
            log.error 'Incorrect parameters.'
        }
    }

    /**
     *
     * @param image
     * @return
     */
    private File convert(Image image) {
        // File object for original file
        File file = imageService.getFileForImage(image)
        // Decompose original image filename
        Map decomposedOriginalFilename = FileHelper.decomposeFilename(file)
        // File object for converted file, file format is params.ext
        File converted = new File(file.parentFile, "${decomposedOriginalFilename.name}.${params.ext}")
        // Check if different file format was requested
        boolean differentFormatRequested = imageMagickService.isDifferentFileFormat(file, converted)
        // Stream image with certain dimension?
        String width = checkRequestParameterChars(params.width)
        String height = checkRequestParameterChars(params.height)
        // Do nothing, format is same as original file, no resizing requested
        if (!differentFormatRequested && !params.width && !params.height) {
            // Use original file
            if (log.traceEnabled) log.trace "ImageController.convert: Streaming original file ${converted}"
        } else if (params.width || params.height) {
            // Resize image
            // This will also convert to different format, requested through params.ext
            converted = imageMagickService.resize(file, width, height, converted)
            if (log.traceEnabled) log.trace "ImageController.convert: Streaming resized file ${converted}, height=${height} width=${width}"
        } else if (differentFormatRequested) {
            // Decompose converted image filename
            Map decomposedConvertedFilename = FileHelper.decomposeFilename(converted)
            // Stream file in original or other format?
            File to = new File(file.parentFile, "${decomposedConvertedFilename.name}.${params.ext}")
            if (log.traceEnabled) log.trace "ImageController.convert: Converting file ${file} to ${to}"
            converted = imageMagickService.convert(file, to)
        }
        // Return converted file.
        return converted
    }

}
