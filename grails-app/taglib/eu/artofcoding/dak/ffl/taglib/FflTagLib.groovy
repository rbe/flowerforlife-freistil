package eu.artofcoding.dak.ffl.taglib

class FflTagLib {
    
    /**
     * Our namespace.
     */
    static namespace = "ffl"
    
    def gallery = { attr, body ->
        def d = [2010, 2011, 2012]
        out << "<div id=\"menu\">"
        d.each {
            out << "<li><a href=\"${it}\">${it}</a></li>"
        }
        out << "</div>"
    }
    
}
