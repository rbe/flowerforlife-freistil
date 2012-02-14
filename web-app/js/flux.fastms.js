/**
 * Flux
 *
 * Copyright (C) 2011-2012 art of coding UG (haftungsbeschränkt).
 * Alle Rechte vorbehalten.
 */
var FLUX = {};
FLUX.FASTMS = function ($) {

    this.cache = {lastTime:0};

    /**
     * Activate spinner.
     */
    this.activateSpinner = function () {
        $('#spinner')
            .ajaxStart(function () {
                $(this).fadeIn();
            })
            .ajaxStop(function () {
                $(this).fadeOut();
            });
    };

    /**
     * Call a function when user scrolls to end of window.
     * @param q
     *            A map: fn: Function to execute.
     */
    this.lazyLoad = function (q) {
        var w = window,
            d = document,
            e = d.documentElement,
            g = d.getElementsByTagName('body')[0],
            x = w.innerWidth || e.clientWidth || g.clientWidth,
            y = w.innerHeight || e.clientHeight || g.clientHeight;
        // $('body').append('<div id="more"></div>');
        $(window).scroll(function () {
            var scrollTop = $(window).scrollTop(),
                height = $(document).height() - $(window).height(),
                atEnd = scrollTop >= Math.round(height * 0.8), // 80 percent
                t = new Date().getTime(),
                t2 = FLUX.FASTMS.cache.lastTime + 2 * 1000;
            // var position = $('#more').offset().top;
            if (atEnd && (FLUX.FASTMS.cache.lastTime == 0 || t > t2)) {
                q.fn();
                FLUX.FASTMS.cache.lastTime = new Date().getTime();
            }
        });
    };

    /**
     * Send JSON data to server using HTTP POST.
     */
    this.postJSON = function (q) {
        var jqXHR = $.ajax({
            url:q.url,
            type:'POST',
            contentType:'application/json',
            data:JSON.stringify(q.json),
            dataType:'json' // of response
        })
            .done(q.done || function () {
        })
            .fail(q.fail || function () {
        })
            .always(q.always || function () {
        });
        return jqXHR;
    };

    /**
     *
     */
    this.fileUpload = function (q) {
        // Get input[@type=file]
        var inputFile = $('#' + q.fileId);
        var originalInputFile;
        inputFile.change(function (e) {
            // Execute custom function when file was chosen
            if (q.postChoose) q.postChoose(this);
            // iframe
            var iframeId = q.fileId + '-upload';
            var iframe = $('#' + iframeId);
            if (iframe) iframe.remove();
            // Append iframe to body to post file in background
            var iframeHtml = '<iframe id="' + iframeId + '" name="' + iframeId + '" src="#" style="display: none;"></iframe>';
            $(document).contents().find('body').append(iframeHtml);
            // Render form, wrap input:file
            var formId = q.fileId + '-form';
            $(this).wrap('<form id="' + formId + '" action="' + q.action + '" method="post" enctype="multipart/form-data" target="' + iframeId + '">');
            // Execute custom function when file was uploaded
            if (q.preUpload) q.preUpload(this);
            // Submit form
            $('#' + formId).submit().replaceWith(originalInputFile);
            // Execute custom function when file was uploaded
            if (q.postUpload) {
                var iframe = $('#' + iframeId);
                iframe.load(function () {
                    q.postUpload(iframe);
                });
            }
        });
        // Clone with data and events to allow re-use of input:file
        originalInputFile = inputFile.clone(true);
    };

    /**
     * Render JSON data using a HTML template.
     * @param q
     *            A map
     *            <ul>
     *            <li>template: Selector for HTML template.</li>
     *            <li>json: JSON data.</li>
     *            <li>doBeforeRow: function(row, tmpl): Execute this code before data is populated
     *            to template (optional).</li>
     *            <li>doAfterRow: function(row, tmpl): Execute this code after data was populated
     *            to template (optional).</li>
     *            </ul>
     */
    this.renderTemplate = function (q) {
        // Append
        var appendElem = $(q.append);
        // For each row from JSON, insert template
        var tmpl = null;
        var row = null;
        var rowIndex = 0;
        for (rowIndex in q.json) {
            // Clone HTML template
            tmpl = $(q.template).clone();
            tmpl.attr('id', tmpl.attr('id') + '_' + rowIndex);
            // Get row/data
            row = q.json[rowIndex];
            // Execute function before rendering the row
            if (q.doBeforeRow) q.doBeforeRow(row, tmpl);
            // Process every field
            var key = null;
            var keyElem = null;
            for (key in row) {
                // Use key to find place for content in HTML template
                keyElem = $('#' + key, tmpl);
                /*
                 if (keyElem.attr('id')) {
                 keyElem.attr('id', keyElem.attr('id') + rowIndex);
                 }
                 */
                keyElem.html(row[key]);
            }
            // Execute function after rendering the row
            row.rowIndex = rowIndex;
            if (q.doAfterRow) q.doAfterRow(row, tmpl);
            // Append row to target
            appendElem.append(tmpl);
        }
        // Don't show template
        $(q.template).css('display', 'none !important');
    };

    /**
     */
    this.renderDate = function (date, format) {
        var sp = date.split('T');
        $('td#birthday', tmpl).html(birthday.split('T')[0]);
    };

    /**
     * @param Map
     *            q
     *            <ul>
     *            <li>form: Selector</li>
     *            <li>rules: Rules for input</li>
     *            </ul>
     */
    this.validate = function (q) {
        $.each($(q.form + ' input'), function (k, v) {
            // Lookup validation action
            // Get configuration for 'id'
            var config = q.rules[v.id];
            if (config) {
                if ($(v).attr('class')) {
                    // Find validate[] entry in attribute 'class'
                    var attrClass = $(v).attr('class').split(' ');
                    var c;
                    var match;
                    var rules;
                    for (c in attrClass) {
                        // validate[rules]
                        match = attrClass[c].match(/^validate\[(.*)\]/);
                        if (null != match && match.length == 2) {
                            // Multiple rules, separated by comma?
                            rules = match[1].split(',');
                            var r;
                            var rule;
                            for (r in rules) {
                                rule = rules[r];
                                switch (rule) {
                                    case 'required':
                                        var requiredFns = config[rule];
                                        // Field entered (focus gained)
                                        $(v).focus(function (e) {
                                            // Call user function
                                            requiredFns.focusGained(v, e);
                                        });
                                        // Field was left (focus lost)
                                        $(v).blur(function (e) {
                                            // Call user function
                                            requiredFns.focusLost(v, e);
                                        });
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        });
    };

    // Return object
    return this;

};

//Initialize with jQuery
if (typeof jQuery !== 'undefined') {
    FLUX.FASTMS = FLUX.FASTMS(jQuery);
} else {
    alert('Could not initialize FLUX.FASTMS: jQuery not loaded.');
}
