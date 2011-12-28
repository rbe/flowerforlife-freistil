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
class Tag {
    
    Long id
    
    String name
    
    static belongsTo = [Image]
    
    static constraints = {
        name nullable: false
    }
    
}
