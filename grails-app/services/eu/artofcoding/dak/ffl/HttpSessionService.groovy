/**
 * Flux
 * Copyright (C) 2009-2010 Informationsysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten, siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * All rights reserved. Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl

import eu.artofcoding.dak.ffl.image.ImageService

import javax.servlet.http.HttpSessionEvent

/**
 * 
 * @author rbe
 */
class HttpSessionService {
    
    /**
     * The image service.
     */
    ImageService imageService
    
    /**
     * Get disjunction (opposite of intersection) from two lists.
     * @param a List
     * @param b List
     * @return List
     */
    private List disjunct(List a, List b) {
        (a + b) - a.intersect(b)
    }
    
    /**
     * 
     * @param event
     * @return
     */
    def sessionCreated(HttpSessionEvent event) {
        //println "${this}.sessionCreated(${event})"
    }
    
    /**
     * 
     * @param event
     * @return
     */
    def sessionDestroyed(HttpSessionEvent event) {
        if (event.session.contestId) {
            //println "${this}.sessionDestroyed(${event}): ${event.session.contestId}"
            // Remove all images, which are in the session and have no data
            // session.contestId contains contestIds of all uploaded images
            // session.updatedContestId contains contestIds of all images which were updated with data
            if (event.session.contestId) {
                def sect
                if (event.session.updatedContestId) {
                    sect = disjunct(event.session.contestId, event.session.updatedContestId)
                } else {
                    sect = event.session.contestId
                }
                sect.each { cid -> imageService.removeImage(cid) }
            }
        }
    }
    
}
