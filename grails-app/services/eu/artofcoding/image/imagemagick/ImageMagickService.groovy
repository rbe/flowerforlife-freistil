/**
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten, siehe http://files.art-of-coding.eu/aoc/AOCPL_v10_de.html
 * All rights reserved. Use is subject to license terms, see http://files.art-of-coding.eu/aoc/AOCPL_v10_en.html
 * 
 * ImageMagick:
 * 
 * Licensed under the ImageMagick License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 * http://www.imagemagick.org/script/license.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package eu.artofcoding.image.imagemagick

import eu.artofcoding.dak.ffl.ConfigService
import eu.artofcoding.grails.helper.FileHelper
import eu.artofcoding.image.imagemagick.ImageMagickCategory

/**
 * Service for editing images with ImageMagick.
 * @author rbe
 */
class ImageMagickService {
    
    /**
     * The configuration service.
     */
    ConfigService configService
    
    /**
     * Check if two file have the same format/extension or not.
     * @param file1 Left/first file for check.
     * @param file2 Right/second file for check.
     * @return boolean
     */
    public boolean isDifferentFileFormat(File file1, File file2) {
        // Decompose both filenames
        Map<String, String> decomp1 = FileHelper.decomposeFilename(file1)
        Map<String, String> decomp2 = FileHelper.decomposeFilename(file2)
        // Extensions different?
        boolean differentFormat = decomp1.ext.toLowerCase() != decomp2.ext.toLowerCase()
        if (differentFormat) {
            // Check special cases
            def cc = { decomp, list ->
                if (decomp.ext.toLowerCase() in list) differentFormat = false
            }
            switch (decomp1.ext.toLowerCase()) {
                // JPEG
                case { it in ['jpg', 'jpeg'] }:
                    cc decomp2, ['jpg', 'jpeg']
                    break
                // TIFF
                case { it in ['tif', 'tiff'] }:
                    cc decomp2, ['tif', 'tiff']
                    break
            }
        }
        differentFormat
    }
    
    /**
     * Convert an image into another.
     * @param image java.io.File, object of image to resize.
     * @param converted java.io.File, File object to save converted file to.
     * @return java.io.File The converted image. ATTENTION: Use this File reference, as the filename may have changed.
     */
    public File convert(File image, File converted) {
        if (!image || !converted) {
            throw new IllegalStateException('No input or output file')
        }
        // Where to save?
        File _converted = converted
        use (ImageMagickCategory) {
            // Convert
            Process process = image.init(configService.fflConfig.imageMagickHome).convert(converted)
            // Error while resizing
            if (process.exitValue() != 0) {
                // No converted file
                _converted = null
                // Show Process' error and input stream
                ['errorStream', 'inputStream'].each { log.info "${it}: ${new String(process[it].bytes)}" }
            }
        }
        // Return converted file object
        return _converted
    }
    
    /**
     * Convert an image to a certain size.
     * @param image java.io.File, object of image to resize.
     * @param height String, new height (optional, can be null or empty string).
     * @param width String, new width (optional, can be null or empty string).
     * @param converted java.io.File, File object to save converted file to.
     * @return java.io.File The converted image. ATTENTION: Use this File reference, as the filename may have changed.
     */
    public File resize(File image, String width, String height, File converted = null) {
        // Generate value for convert's -resize option
        String resizeOption = "${width}x${height}"
        // Decomponse original and converted filename
        Map decomposedImageFilename = FileHelper.decomposeFilename(image)
        Map decomposedConvertedFilename = converted ? FileHelper.decomposeFilename(converted) : null
        // Where to save?
        File resized = null
        if (converted) {
            resized = new File(image.parentFile, "${decomposedConvertedFilename.name}_${resizeOption}.${decomposedConvertedFilename.ext}")
        } else {
            resized = new File(image.parentFile, "${decomposedImageFilename.name}_${resizeOption}.${decomposedImageFilename.ext}")
        }
        // If resized files does not exist, create it
        // otherwise return File object of existing file
        if (!resized.exists()) {
            log.info "ImageMagickService.resize: Resizing image ${image} to ${resized} with width=${width} and height=${height}"
            use (ImageMagickCategory) {
                // Convert
                Process process =
                image.init(configService.fflConfig.imageMagickHome)
                .option('-resize', resizeOption.toString())
                .convert(resized)
                // Error while resizing
                if (process.exitValue() != 0) {
                    // No resized file
                    resized = null
                    // Show Process' error and input stream
                    ['errorStream', 'inputStream'].each { log.info "ImageMagickService.resize: ${it}: ${new String(process[it].bytes)}" }
                }
            }
        } else {
            log.info "ImageMagickService.resize: Using existing file ${resized}"
        }
        return resized
    }
    
    /**
     * Convert an image to a thumbnail size.
     * @param image java.io.File, object of image to resize.
     * @param height String, new height (optional, can be null or empty string).
     * @param width String, new width (optional, can be null or empty string).
     * @param converted java.io.File, File object to save converted file to.
     * @return java.io.File The converted image. ATTENTION: Use this File reference, as the filename may have changed.
     */
    public File thumbnail(File image, String width, String height, File converted = null) {
        // Generate value for convert's -resize option
        String resizeOption = "${width}x${height}"
        // Decomponse original and converted filename
        Map decomposedImageFilename = FileHelper.decomposeFilename(image)
        Map decomposedConvertedFilename = converted ? FileHelper.decomposeFilename(converted) : null
        // Where to save?
        File resized = null
        if (converted) {
            resized = new File(image.parentFile, "${decomposedConvertedFilename.name}_${resizeOption}.${decomposedConvertedFilename.ext}")
        } else {
            resized = new File(image.parentFile, "${decomposedImageFilename.name}_${resizeOption}.${decomposedImageFilename.ext}")
        }
        // If resized files does not exist, create it
        // otherwise return File object of existing file
        if (!resized.exists()) {
            log.info "ImageMagickService.thumbnail: Resizing image ${image} to ${resized.absolutePath} with width=${width} and height=${height}"
            use (ImageMagickCategory) {
                // Convert to PNG (used as intermediate working format)?
                File pngFile = null
                if (decomposedImageFilename.ext != 'png') {
                    pngFile = new File(image.parentFile, "${decomposedImageFilename.name}.png")
                    image.init(configService.fflConfig.imageMagickHome).convert(pngFile)
                } else {
                    pngFile = image
                }
                // Create thumbnail
                Process process = null
                try {
                    process = pngFile.init(configService.fflConfig.imageMagickHome)
                    .option('-thumbnail', "${resizeOption}^")
                    .option('-unsharp', '0x.05')
                    .option('-gravity', 'center')
                    .option('-extent', resizeOption.toString())
                    .convert(resized)
                    // Add rounded corners
                    if (decomposedConvertedFilename.ext == 'gif') {
                        process = resized.init(configService.fflConfig.imageMagickHome)
                        .option('\\(')
                        .option('+clone')
                        .option('-alpha', 'extract')
                        .option('-draw', "'fill black polygon 0,0 0,15 15,0 fill white circle 15,15 15,0'")
                        .option('\\(')
                        .option('+clone', '-flip')
                        .option('\\)')
                        .option('-compose', 'Multiply')
                        .option('-composite')
                        .option('\\(')
                        .option('+clone', '-flop')
                        .option('\\)')
                        .option('-compose', 'Multiply')
                        .option('-composite')
                        .option('\\)')
                        .option('-alpha', 'off')
                        .option('-compose', 'CopyOpacity')
                        .option('-composite')
                        .convert(resized)
                    }
                } catch (e) {
                    log.info e
                } finally {
                    // Error while resizing
                    if (process?.exitValue() != 0) {
                        // No resized file
                        resized = null
                        // Show Process' error and input stream
                        ['errorStream', 'inputStream'].each { log.info "ImageMagickService.thumbnail: ${it}: ${new String(process[it].bytes)}" }
                    }
                }
            }
        } else {
            log.info "ImageMagickService.thumbnail: Using existing file ${resized}"
        }
        return resized
    }
    
    /**
     * 
     * @param image
     * @return
     */
    public Map identify(File image) {
        use (ImageMagickCategory) {
            image.init(configService.fflConfig.imageMagickHome).option('-verbose').identify()
        }
    }
    
}
