/*
 * DAK Flower for Life
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 */
var FFL = function($) {
    
    /**
     * Find image(s).
     * @param q
     *            Map with the following keys: json = JSON data for search query, done =
     *            function(data, statusText, jqXHR)
     */
    this.find = function(q) {
        var jqXHR = FLUX.FASTMS.postJSON({
            url: 'image/find',
            json: q.query,
            done: q.done || function() { console.log('FFL.find: ' + jqXHR.responseText); }
        });
    };
    
    /**
     * Render an image as a new img tag.
     * @param Map
     *            q
     *            <ul>
     *            <li>selector = Where to put the HTML?</li>
     *            <li>contestId = Contest ID of image to render.</li>
     *            <li>html = HTML source, default to img tag wit attributes id, src (optional).</li>
     *            <li>smooth = Milliseconds for fadeIn() effect (optional).
     *            </ul>
     */
    this.renderImage = function(q) {
        var item = $(q.html || '<img id="' + q.contestId + '" src="image/' + q.contestId + '"></img>');
        if (q.smooth) {
            item.hide().fadeIn(q.smooth);
        }
        $(q.selector, q.scope || document).append(item);
    };
    
    /**
     * Render an image when building the gallery.
     * @param row One row of data, got via JSON from server.
     * @param tmpl The just create new instance of the HTML template.
     */
    this.renderGalleryImage = function(row, tmpl) {
        tmpl.hide();
        $('a.group', tmpl).attr('title', row.username).attr('href', 'image/' + row.contestId + '.jpg/w/800');
        $('img.gallery_image_thumb', tmpl).attr('src', 'image/' + row.contestId + '.jpg/thumb');
        tmpl.fadeIn(2500);
    };
    
    /**
     * Render the gallery.
     * @param data
     * @param statusText
     * @param jqXHR
     */
    this.renderGallery = function(data, statusText, jqXHR) {
        FLUX.FASTMS.renderTemplate({
            template: '#gallery_template',
            append: '#content_gallery',
            json: data.success.result,
            doAfterRow: FFL.renderGalleryImage
        });
        // Activate fancybox
        $('a.group').fancybox({
            'transitionIn'       : 'elastic',
            'transitionOut'      : 'elastic',
            'speedIn'            : 600,
            'speedOut'           : 200,
            'overlayShow'        : false,
            'hideOnOverlayClick' : true,
            'hideOnContentClick' : true,
            'title'              : jQuery(this).title,
            'titlePosition'      : 'over',
            'onComplete'         : function() {
                                       $("#fancybox-wrap").hover(
                                           function() { $("#fancybox-title").show(); },
                                           function() { $("#fancybox-title").hide(); }
                                       );
                                   }
        });
    };
    
    // Return object
    return this;
    
};

// Initialize with jQuery
if (typeof jQuery !== 'undefined') {
    FFL = FFL(jQuery);
} else {
    alert('Could not initialize FFL: jQuery not loaded.');
}
