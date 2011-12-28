/*
 * DAK Flower for Life
 * Copyright (C) 2011 art of coding UG (haftungsbeschränkt).
 *
 * Nutzungslizenz siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl.helper

import org.springframework.beans.factory.InitializingBean

/**
 * 
 */
class ControllerBase implements InitializingBean {
    
    def grailsApplication
    def fflConfig
    
    /**
     * InitializingBean.
     */
    void afterPropertiesSet() {
        fflConfig = grailsApplication.config.flowerforlife
    }
    
    def dumpconfig = {
        StringBuilder sb = new StringBuilder()
        Map c = grailsApplication.config
        c.each { k, v ->
            sb << "${k} = ${v}<br/>\n"
        }
        render sb.toString()
    }
    
}
