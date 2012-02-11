<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Flower for Life - Development - <g:layoutTitle default="Grails" /></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
        <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
        <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'js/jquery.fancybox-1.3.4/fancybox', file: 'jquery.fancybox-1.3.4.css')}" type="text/css" media="screen" />
        <g:layoutHead />
        <g:javascript library="application" />
        <g:javascript library="jquery" plugin="jquery" />
        <r:layoutResources />
    </head>
    <body>
        <div id="grailsLogo" role="banner">
            <a href="http://grails.org"><img src="${resource(dir: 'images', file: 'grails_logo.png')}" alt="Grails" /></a>
        </div>
        <g:layoutBody />
        <div class="footer" role="contentinfo"></div>
        <div id="spinner" class="spinner" style="display: none;">
            <g:message code="spinner.alt" default="Loading&hellip;" />
        </div>
        <r:layoutResources />
        <script type="text/javascript" src="${resource(dir: 'js/jquery.fancybox-1.3.4/fancybox', file: 'jquery.fancybox-1.3.4.pack.js')}"></script>
        <script type="text/javascript" src="${resource(dir: 'js/jquery.fancybox-1.3.4/fancybox', file: 'jquery.easing-1.3.pack.js')}"></script>
        <script type="text/javascript" src="${resource(dir: 'js/jquery.fancybox-1.3.4/fancybox', file: 'jquery.mousewheel-3.0.4.pack.js')}"></script>
        <script type="text/javascript" src="${resource(dir: 'js', file: 'flux.fastms.js')}"></script>
        <script type="text/javascript" src="${resource(dir: 'js', file: 'flowerforlife.js')}"></script>
        <script type="text/javascript">
            jQuery(document).ready(function() {
                FLUX.FASTMS.activateSpinner();
            });
        </script>
    </body>
</html>