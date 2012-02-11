/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten, siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * All rights reserved. Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl.image

import java.util.Date

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
    String plz
    // FFL-1
    boolean contactMe
    
    def beforeInsert() { check() }
    def beforeUpdate() { check() }

    /**
     * FFL-1 Check input.
     */
    def check() {
        
    }
    
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
    }
    
}
