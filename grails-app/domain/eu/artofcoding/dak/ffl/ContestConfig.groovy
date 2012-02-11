/**
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten, siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * All rights reserved. Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl

import java.util.Date

/**
 * 
 * @author rbe
 */
class ContestConfig {
    
    Long id
    Long version
    Date dateCreated
    Date lastUpdated

    String contest
    Long maximumFileSize
    Integer maximumWidth
    Integer maximumHeight
    Integer standardThumbnailHeight
    
    static hasMany = [fileTypes: ContestFileType]
    
    static constraints = {
        contest nullable: false
        //fileTypes nullable: false
        maximumFileSize nullable: true
        maximumWidth nullable: true
        maximumHeight nullable: true
        standardThumbnailHeight nullable: true
    }
    
    def beforeInsert() {
        if (!maximumFileSize) maximumFileSize = 30 * 1024 * 1024
        if (!maximumWidth) maximumWidth = 1920
        if (!maximumHeight) maximumHeight = 1280
        if (!standardThumbnailHeight) standardThumbnailHeight = 90
    }
    
}
