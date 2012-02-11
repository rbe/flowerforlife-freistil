<html>
    <head>
        <meta name="layout" content="main" />
    </head>
    <body>
        <input type="button" id="gallery" value="Gallery" />
        <div id="content_gallery">
            <div id="gallery_template">
                <div class="gallery_item">
                    <a class="group" rel="group"><img class="gallery_image_thumb"></img></a>
                </div>
                <div id="username" class="gallery_thumb_text"></div>
            </div>
        </div>
        <script type="text/javascript">
            var firstResult = 1;
            var max = 10;
            function next() {
                FFL.find({
                    query: {
                        meta: {
                            max: max,
                            offset: firstResult++ * max,
                            order: {'dateCreated': 'desc'}
                        },
                        criteria: {
                            contest: 'smiling_faces',
                            approved: true
                        }
                    },
                    done: FFL.renderGallery
                });
            }
            jQuery('#gallery').click(function() {
                // Reset display counter
                firstResult = 0;
                next();
            });
            FLUX.FASTMS.lazyLoad({ fn: next });
        </script>
    </body>
</html>
