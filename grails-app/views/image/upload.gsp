<!doctype html>
<html>
    <head>
        <meta name="layout" content="main" />
        <title>Welcome to Grails</title>
    </head>
    <body>
    <a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>
    <div id="status" role="complementary"></div>
    <div id="page-body" role="main">
        <form id="uploadForm">
            <p>
                <input type="text" id="username" name="image.username" value="Der Ralf" class="validate[required,min=5]" />
                <div id="username_message"/>
            </p>
                <input type="text" id="email" name="image.email" value="ralf@art-of-coding.eu" />
                <input type="text" id="email" name="image.email" value="ralf@art-of-coding.eu" />
            </p>
            </p>
                <input type="text" id="zipcode" name="image.zipcode" value="48123" />
            </p>
            <p>
                <input type="text" id="birthday.day" name="image.birthday.day" value="20" />
                <input type="text" id="birthday.month" name="image.birthday.month" value="9" />
                <input type="text" id="birthday.year" name="image.birthday.year" value="1978" />
            </p>
            <p>
                <input type="text" id="tag" name="image.tag" value="sonne,mond,sterne" />
            </p>
            <p>
                <select id="knownBy" name="image.knownBy">
                    <option value="dakfit">DAK fit</option>
                    <option value="website">Website</option>
                </select>
            </p>
            <p>
                <input type="checkbox" id="terms" name="image.terms" value="true">Terms &amp; Conditions</input>
                <input type="checkbox" id="contactMe" name="image.contactMe" value="true">You may contact me</input>
            </p>
            <p>
                <img id="preview"/>
            </p>
            <p>
                <input id="upl" type="file" name="file.bild">
            </p>
            <input id="submitButton" type="submit">
            <input id="bla" type="submit" value="Bla">
        </form>
    </div>
    <script type="text/javascript">
    $(document).ready(function() {
        /*
        $('input[type=text]').focus(function() {
            $(this).val('');
        }); 
        */
         FLUX.FASTMS.validate({
                form: '#dataform',
                rules: {
                    username: {
                        required: {
                                   focusGained: function(v, e) {
                                        if ($(v).val() == '') {
                                            $('#username_message')
                                                .css('display', 'block')
                                                .html('Bitte geben Sie einen Wert ein!');
                                        } else {
                                            $('#username_message')
                                                .css('display', 'none')
                                                .html('');
                                        }
                                    },
                                    focusLost: function(v, e) {
                                        if ($(v).val() == '') {
                                            $('#username_message')
                                                .css('display', 'block')
                                                .html('Bitte geben Sie einen Wert ein!');
                                        } else {
                                            $('#username_message')
                                                .css('display', 'none')
                                                .html('');
                                       }
                                   }
                        }
                    },
                    email: {
                        required: {
                                   focusGained: function(v, e) {
                                       if ($(v).val() == '') {
                                           $('#email_message')
                                            .css('display', 'block')
                                            .html('Bitte geben Sie einen Wert ein!');
                                       }
                                       /*
                                       else {
                                           $('#email_message')
                                            .css('display', 'none')
                                            .html('');
                                       }
                                       */
                                   },
                                   focusLost: function(v, e) {
                                       var match = $('#confirm_email').val() == $(v).val();
                                       if ($(v).val() == '') {
                                           $('#email_message')
                                            .css('display', 'block')
                                            .html('Bitte geben Sie einen Wert ein!');
                                       }
                                       /*
                                       else if (!match) {
                                           $('#email_message')
                                            .css('display', 'block')
                                            .html('Die Email-Adressen stimmen nicht &uuml;berein!');
                                       }
                                       */
                                       else if (match) {
                                            $('#confirm_email_message,#email_message')
                                                .css('display', 'none')
                                       } else {
                                           $('#email_message')
                                            .css('display', 'none')
                                            .html('');
                                       }
                                   }
                        }
                    },
                    confirm_email: {
                        required: {
                                   focusGained: function(v, e) {
                                       if ($(v).val() == '') {
                                           $('#confirm_email_message')
                                            .css('display', 'block')
                                            .html('Bitte geben Sie einen Wert ein!');
                                       }
                                       /*
                                       else {
                                           $('#confirm_email_message')
                                            .css('display', 'none')
                                            .html('');
                                       }
                                       */
                                   },
                                   focusLost: function(v, e) {
                                       var match = $('#email').val() == $(v).val();
                                        if ($(v).val() == '') {
                                            $('#confirm_email_message')
                                                .css('display', 'block')
                                                .html('Bitte geben Sie einen Wert ein!');
                                        } else if (!match) {
                                            //$('#email_message').css('display', 'block').html('Die Email-Adressen stimmen nicht &uuml;berein!');
                                            $('#confirm_email_message')
                                                .css('display', 'block')
                                                .html('Die Email-Adressen stimmen nicht &uuml;berein!');
                                        } else if (match) {
                                            $('#confirm_email_message,#email_message')
                                                .fadeOut(1000);//.css('display', 'none');
                                        }
                                        /*
                                        else {
                                            $('#confirm_email_message')
                                                .html('');
                                        }
                                        */
                                   }
                        }
                    },
                    usertags: {
                        required: {
                                   focusGained: function(v, e) {
                                        if ($(v).val() == '') {
                                            $('#uploadtags_message')
                                                .css('display', 'block')
                                                .html('Bitte geben Sie einen Wert ein!');
                                        } else {
                                            $('#uploadtags_message')
                                                .css('display', 'none')
                                                .html('');
                                        }
                                    },
                                    focusLost: function(v, e) {
                                        if ($(v).val() == '') {
                                            $('#uploadtags_message')
                                                .css('display', 'block')
                                                .html('Bitte geben Sie Suchbegriffe ein die Ihr Bild beschreiben!');
                                        } else {
                                            $('#uploadtags_message')
                                                .html('');
                                        }
                                    }
                        }
                    }
                }
            });
            //
            $('#terms').change(function() {
                $('#terms_message').fadeOut(1000);
            });
            $('#saveForm').click(function(e) {
               e.preventDefault();
               if (undefined == $('#terms').attr('checked')) {
                   $('#terms_message').html('Bitte lesen und akzeptieren Sie die Teilnahmebedingungen!').fadeIn(1000);
               } else {
                   var form = $('#dataform').serialize();
                   $.post('/flowerforlife/image/upload', form, function(data) {
                       $('#contentinner_upload').css('display', 'none');
                       $('#contentinner_ok').css('display', 'block');
                   });
               }
            });
            //
            $('#saveForm').attr('disabled', true);
            FLUX.FASTMS.fileUpload({
                fileId: 'upl',
                action: '/flowerforlife/image/upload',
                postUpload: function(iframe) {
                    $.get('/flowerforlife/image/lastContestId', function(data) {
                        if (data.success && data.success.contestId) {
                            var contestId = data.success.contestId[0];
                            // Load preview
                            $('#preview').attr('src', '/flowerforlife/image/' + contestId + '.jpg/h/150');
                            // Enable submit button for data
                            $('#saveForm').attr('disabled', false);
                        } else {
                            alert('Leider liegt ein Problem vor!');
                        }
                    });
                    // Remove the iframe
                    iframe.remove();
                }
            });
        });
        </script>
    </body>
</html>
