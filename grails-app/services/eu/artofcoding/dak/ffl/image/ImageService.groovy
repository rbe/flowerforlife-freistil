/**
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten, siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * All rights reserved. Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl.image

import java.io.File
import java.text.SimpleDateFormat
import java.util.List
import java.util.Map

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsHttpSession
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.multipart.MultipartFile

import eu.artofcoding.dak.ffl.ConfigService
import eu.artofcoding.grails.helper.FileHelper

/**
 * Service for dealing with images.
 * @author rbe
 */
class ImageService {

    /**
     * Instance to parse birthdays.
     */
    static SimpleDateFormat sdf

    static {
        sdf = new SimpleDateFormat("dd.MM.yyyy")
        sdf.setLenient(false)
    }

    /**
     * Our configuration service.
     */
    ConfigService configService

    /**
     * Get HTTP session.
     * @return GrailsHttpSession
     */
    private def getSession() {
        GrailsWebRequest gwr = RequestContextHolder.currentRequestAttributes()
        GrailsHttpSession ghs = gwr.session
    }

    /**
     * Return directory name of upload directory. If contestId is given, return directory for certain image.
     * @param contestId Contest ID of certain Image.
     * @return String Name of directory.
     */
    public String getUploadDirectory(String contestId) {
        "${configService.fflConfig.uploadDirectory}/${configService.fflConfig.actualContest}/${contestId}"
    }

    /**
     * Save an image: determine actual contest, move the image into corresponding directory
     * and put some metadata into database.
     * @param multipartFile From MultipartHttpServletRequest.
     * @see org.springframework.web.multipart.MultipartFile
     * @see org.springframework.web.multipart.MultipartHttpServletRequest
     */
    public Map saveImage(MultipartFile multipartFile, Map params) {
        // Return value
        Map result = [:]
        // Decomponse original file name
        Map decomposedImageFilename = FileHelper.decomposeFilename(multipartFile.originalFilename)
        // Do we accept this image type?
        //if (log.traceEnabled) log.trace "ImageService.saveImage: Checking file type ${ext} in ${configService.actualContest.fileTypes*.name}"
        if (decomposedImageFilename.ext.toLowerCase() in configService.actualContest.fileTypes*.name) {
            // Where to put uploaded file?
            File tempFile = File.createTempFile('ffl_', ".${decomposedImageFilename.ext}")
            tempFile.delete()
            // Extract generated ID from temporary filename
            String contestId = tempFile.name - 'ffl_' - ".${decomposedImageFilename.ext}"
            // Create directory for new file
            File file = new File(getUploadDirectory(contestId), multipartFile.originalFilename)
            file.parentFile.mkdirs()
            // Move file
            multipartFile.transferTo(file)
            // Save image metadata in database
            Image img = new Image(
                    sessionId: session.id,
                    contestId: contestId,
                    contest: configService.fflConfig.actualContest,
                    username: params.username,
                    email: params.email instanceof List ? params.email[0] : params.email,
                    birthday: params.birthday,
                    tag: params.tag,
                    name: multipartFile.originalFilename,
                    extension: decomposedImageFilename.ext,
                    fileExists: true,
                    approved: false,
                    terms: params.terms ?: false,
                    contactMe: params.contactMe ?: false, // FFL-1
                    zipcode: params.zipcode // FFL-1
            )
            img.save(flush: true)
            // TODO Send email on request, implement separate service
            //
            result = [success: [
                    message: "Successfully uploaded image ${multipartFile.originalFilename}",
                    contestId: contestId
            ]
            ]
        } else {
            throw new IllegalStateException("File type ${decomposedImageFilename.ext} of ${multipartFile.originalFilename} not supported. Supported types are ${configService.actualContest.fileTypes*.name.join(', ')}")
        }
        // Return
        result
    }

    /**
     * Update image(s) with data.
     * @param contestId
     * @param data
     * @return Possibliy updated images.
     */
    public List updateImage(List contestId, Map params) {
        // Email address
        // TODO Verify via SMTP, cache result, implement as separate service
        if (params.email) params.email = params.email[0]
        // Birthday
        if (params.birthday) {
            params.birthday = sdf.parse("${params.remove('birthday.day')}.${String.format("%02d", params.remove('birthday.month') as Integer)}.${params.remove('birthday.year')}")
        }
        // Tag
        String paramsTag = params.remove('tag')
        String[] tags = paramsTag?.split(',')
        Tag tag = null
        contestId.collect { cid ->
            if (log.traceEnabled) log.trace "Updating image ${cid} with ${params}"
            Image image = Image.findByContestId(cid)
            if (paramsTag) {
                tags.each { t ->
                    t = t.trim()
                    tag = Tag.findByName(t)
                    if (!tag) tag = new Tag(name: t)
                    image.addToTag(tag)
                }
            }
            // Merge data
            image.properties += params
            image.merge()
        }
    }

    /**
     *
     * @param contestId
     */
    public void removeImage(String contestId) {
        Image image = Image.findByContestId(contestId)
        if (image) {
            File imageDir = getDirectoryForImage(image)
            if (imageDir.exists()) {
                imageDir.eachFile { f ->
                    log.trace("Deleting image ${image.contestId}, file ${f.name}: ${f.delete()}")
                }
                log.trace("Deleting directory ${image.contestId}, file ${imageDir.absolutePath}: ${imageDir.delete()}")
            }
            // Remove image from database
            image.delete()
        }
    }

    /**
     * Approve or reject an Image.
     * Default: approve an image (set approved attribute to true).
     * @param contestId Contest ID of Image.
     * @param approve boolean Default is true.
     */
    public Map approveImage(String contestId, boolean approve = true) {
        Map result = [:]
        Image image = Image.findByContestId(contestId)
        if (image) {
            image.approved = approve
            image.merge(flush: true)
            result = [success: [message: "Image ${contestId} approved."]]
        } else {
            result = [error: [message: "Could NOT approve image ${contestId}."]]
        }
        result
    }

    /**
     * Find images by search criteria.
     * @param search
     * @return List < Image >  List with found images.
     */
    public List<Image> find(Map search) {
        def c = Image.createCriteria()
        def result = c {
            // Fill search values
            search.criteria.each { k, v ->
                if (log.traceEnabled) log.trace "eq: $k = $v/${v.getClass()}"
                switch (k) {
                    case 'approved':
                        eq(k, Boolean.valueOf(v))
                        break
                    case 'tag':
                        tag { ilike('tag.name', v) }
                        break
                    default:
                        eq(k, v)
                }
            }
            // TODO approved = false when admin logged in
            // eq('approved', true)
            // Offset
            firstResult(search.meta?.offset ?: 0)
            // Maximum result count
            maxResults(search.meta?.max ?: 10)
            // Order by
            search.meta?.order.each { k, v ->
                order(k, v)
            }
        }
        // Return
        result
    }

    /**
     * Get image by its contest ID.
     * @param id String with contest ID.
     * @return Image
     */
    public Image getImage(String contestId) {
        // Get metadata from database
        Image image = Image.findByContestId(contestId)
        if (log.traceEnabled) log.trace "ImageService.getImage: Image found by contest id ${contestId}=${image}"
        image
    }

    /**
     * Get reference to File object for image.
     * @param image The image.
     * @return java.io.File.
     */
    public File getFileForImage(Image image) {
        // Where is the image?
        File file = new File(getDirectoryForImage(image), image.name)
        // Image found, but corresponding file is missing...
        if (!file.exists()) {
            image.fileExists = false
            image.merge(flush: true)
            file = null
        }
        if (log.traceEnabled) log.trace "ImageService.getFileForImage: Found file ${file} for ${image.contestId}"
        file
    }

    /**
     * Get directory where image is stored.
     * @param image
     * @return
     */
    private File getDirectoryForImage(Image image) {
        String parentDirectory = "${configService.fflConfig.uploadDirectory}/${image.contest}/${image.contestId}"
        new File(parentDirectory)
    }

}