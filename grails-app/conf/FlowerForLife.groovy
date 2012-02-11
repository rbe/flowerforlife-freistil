/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten, siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * All rights reserved. Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 *
 */
flowerforlife {
    imagemagick {
        home = '/Users/rbe/app/ImageMagick/6.7.4'
    }
    freistil {
        actualContest = 'smiling_faces'
    }
    upload {
        tempdir = new File('ffltemp')
        allowed {
            fileTypes = ['gif', 'jpg', 'jpeg', 'png']
            fileSize = 30 * 1024 * 1024
            dimensions = [x: 1920, y: 1280]
        }
    }
}
