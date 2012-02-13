/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.dak.ffl.image

/**
 *
 */
class Image {

    Long id
    Long version
    Date dateCreated
    Date lastUpdated

    String sessionId
    String contestId
    String contest
    String username
    String email
    String name // of file/image
    String extension
    boolean fileExists
    Set<Tag> tag
    boolean approved
    Date birthday
    String knownBy
    boolean terms
    String shopUrl
    boolean bookmark
    // FFL-1
    String zipcode
    // FFL-1
    boolean contactMe

    def beforeInsert() {}

    def beforeUpdate() {}

    static hasMany = [
            tag: Tag
    ]

    static constraints = {
        sessionId nullable: true
        contestId nullable: false
        contest nullable: false
        username nullable: true
        email nullable: true
        name nullable: true
        extension nullable: true
        fileExists nullable: true
        tag nullable: true
        approved nullable: true
        birthday nullable: true
        knownBy nullable: true
        terms nullable: true
        shopUrl nullable: true
        bookmark nullable: true
        zipcode nullable: true
        contactMe nullable: true
    }

}
