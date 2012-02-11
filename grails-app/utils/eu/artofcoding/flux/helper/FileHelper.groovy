/*
 * Flux
 * Copyright (C) 2009-2010 Informationsysteme Ralf Bensmann.
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
package eu.artofcoding.flux.helper

/**
 * 
 * @author rbe
 */
final class FileHelper {
    
    /**
     * Decompose a filename in name and extension.
     * @param file
     * @return Map Keys: name, ext.
     */
    public static Map decomposeFilename(String filename) {
        Map map = [:]
        if (filename.contains('.')) {
            String[] split = filename.split('[.]')
            map['name'] = split[0..-2].join('')
            map['ext'] = split[-1] //.toLowerCase()
        } else {
            map['name'] = filename
            map['ext'] = 'xxx'
        }
        // Return
        map
    }
    
    /**
     * Convenience method for {@link decomposeFilename(String)}.
     * @see #decomposeFilename(String)
     * @param file
     * @return Map Keys: name, ext.
     */
    public static Map decomposeFilename(File file) {
        decomposeFilename(file.name)
    }
    
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
        decomp1.ext.toLowerCase() != decomp2.ext.toLowerCase()
    }
    
}
