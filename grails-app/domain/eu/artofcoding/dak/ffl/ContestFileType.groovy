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
class ContestFileType {
    
    Long id
    Long version
    Date dateCreated
    Date lastUpdated
    
    String name

    static constraints = {
        name nullable: false, unique: true
    }
}
