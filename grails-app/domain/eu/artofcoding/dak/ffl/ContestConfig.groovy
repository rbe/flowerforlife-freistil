/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.dak.ffl

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
    String uploadDirectory // FFL-2

    static hasMany = [fileTypes: ContestFileType]

    static constraints = {
        contest nullable: false
        //fileTypes nullable: false
        maximumFileSize nullable: true
        maximumWidth nullable: true
        maximumHeight nullable: true
        standardThumbnailHeight nullable: true
        uploadDirectory nullable: true // FFL-2
    }

    def beforeInsert() {
        if (!maximumFileSize) maximumFileSize = 30 * 1024 * 1024
        if (!maximumWidth) maximumWidth = 1920
        if (!maximumHeight) maximumHeight = 1280
        if (!standardThumbnailHeight) standardThumbnailHeight = 90
        if (!uploadDirectory) uploadDirectory = "fflimages/${contest}" // FFL-2
    }

}
