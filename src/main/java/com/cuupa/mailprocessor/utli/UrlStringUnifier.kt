package com.cuupa.mailprocessor.utli

class UrlStringUnifier {

    fun unify(path: String): String {
        return path.replace(" ", "%20")
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("Ä", "Ae")
                .replace("Ö", "Oe")
                .replace("Ü", "Ue")
                .replace("ß", "ss")
    }
}
