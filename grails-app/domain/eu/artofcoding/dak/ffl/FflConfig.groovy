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
class FflConfig {

    Long id
    Long version
    Date dateCreated
    Date lastUpdated

    // FFL-2 String actualContest
    String imageMagickHome
    String uploadDirectory

    static constraints = {
        // FFL-2 actualContest nullable: false
        imageMagickHome nullable: false
        uploadDirectory nullable: false
    }

}
