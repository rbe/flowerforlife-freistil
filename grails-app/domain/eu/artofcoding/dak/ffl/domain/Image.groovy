/*
 * DAK Flower for Life
 * Copyright (C) 2011 art of coding UG (haftungsbeschränkt).
 *
 * Nutzungslizenz siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl.domain

/**
 * 
 */
class Image {
    
    Long id
    
    String name
    String contest
    Set<Tag> tag
    boolean display
    String shopUrl
    
    def beforeInsert() {}
    def beforeUpdate() {}
    
    static hasMany = [
        tag: Tag
    ]
    
    static constraints = {
        name nullable: false
        contest nullable: false
        tag nullable: true
        display nullable: false
        shopUrl nullable: true
    }
    
}
