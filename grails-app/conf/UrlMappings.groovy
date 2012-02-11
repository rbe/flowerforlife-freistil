/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */

class UrlMappings {
    
    //(parseRequest: false)
    
    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }
        // Administration
        "/admin/login" (controller: 'user', action: 'login') {}
        "/admin/logout" (controller: 'user', action: 'login') {}
        // Upload an image
        "/image/upload" (controller: 'image', action: 'upload') {
            action = [POST: 'upload']
        }
        // Approve an image
        "/image/$id/approve" (controller: 'image', action: 'approve') {
            action = [GET: 'approve']
        }
        // Reject an image
        "/image/$id/reject" (controller: 'image', action: 'reject') {
            action = [GET: 'reject']
        }
        // Delete an image
        "/image/$id/remove" (controller: 'image', action: 'remove') {
            action = [GET: 'remove']
        }
        // Find (an) image(s)
        "/image/find" (controller: 'image', action: 'find') {
            action = [POST: 'find']
        }
        // Get information of an image
        "/image/$id.$ext/info" (controller: 'image') {
            action = [GET: 'info']
        }
        // Get thumbnail of an image
        "/image/$id.$ext/thumb" (controller: 'image', action: 'thumbnail') {
            height = '90'
            width = '90'
            action = [GET: 'thumbnail']
        }
        // Get an image by width and height
        "/image/$id.$ext/w/$width/h/$height" (controller: 'image', action: 'stream') {
            action = [GET: 'stream']
        }
        // Get an image by width
        "/image/$id.$ext/w/$width" (controller: 'image', action: 'stream') {
            height = ''
            action = [GET: 'stream']
        }
        // Get an image by height
        "/image/$id.$ext/h/$height" (controller: 'image', action: 'stream') {
            width = ''
            action = [GET: 'stream']
        }
        // Get an image
        "/image/$id.$ext" (controller: 'image', action: 'stream') {
            action = [GET: 'stream']
        }
        // Home sweet home
        '/' (view:"/index")
        /*
        // HTTP error codes
        '403' (controller: "errors", action: "forbidden")
        '404' (controller: "errors", action: "notFound")
        '500' (controller: "errors", action: "serverError", exception: IllegalStateException)
        */
    }
    
}
