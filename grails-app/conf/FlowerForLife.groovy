/*
 * DAK Flower for Life
 * Copyright (C) 2011 art of coding UG (haftungsbeschränkt).
 *
 * Nutzungslizenz siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
flowerforlife {
    freistil {
        actualContest = 'smiling_faces'
    }
    upload {
        tempdir = new File('ffltemp')
        allowed {
            fileTypes = ['gif', 'jpg', 'jpeg', 'png']
            fileSize = 15 * 1024 * 1024
            dimensions = [x: 1920, y: 1280]
        }
    }
}
