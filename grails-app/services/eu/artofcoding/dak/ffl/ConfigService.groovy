/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten, siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * All rights reserved. Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
package eu.artofcoding.dak.ffl

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.FetchMode
import org.springframework.beans.factory.InitializingBean

/**
 * This service provides access to our configuration.
 * @author rbe
 */
class ConfigService implements InitializingBean {

    GrailsApplication grailsApplication

    FflConfig fflConfig

    List<ContestConfig> contestConfigs

    /**
     * @see InitializingBean#afterPropertiesSet
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        /*
         // Load Groovy configuration file
         fflConfig = grailsApplication.config.flowerforlife
         */
        // FflConfig
        fflConfig = FflConfig.withCriteria(uniqueResult: true) {
            eq('id', 1L)
            fetchMode('fileTypes', FetchMode.EAGER)
        }
        // Load configuration for contests from database
        contestConfigs = ContestConfig.list()
    }

    /**
     *
     * @param what
     * @return
     */
    public Object get(String what) {
        switch (what) {
            case 'fflConfig':
                fflConfig
                break
            /* FFL-2 Es gibt nicht mehr "den" aktuellen Contest
            case 'actualContest':
                ContestConfig cc = contestConfigs.find { it.contest == fflConfig.actualContest }
                ContestConfig.findById(cc.id)
                break
            */
            default:
                for (cc in contestConfigs) {
                    def found = cc.contest.find { it == what }
                    if (found) {
                        //println "found=${found}=${cc}"
                        return cc
                    }
                }

        }
    }

}
