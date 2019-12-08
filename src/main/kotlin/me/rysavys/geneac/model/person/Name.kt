package me.rysavys.geneac.model.person

class Name(gedName: org.folg.gedcom.model.Name) {

    val firstName: String?
    val lastName: String?

    init {
        val name_regex = "(.+)\\s+/(.+)/".toRegex()
        val nameMatches = name_regex.find(gedName.value)
        firstName= nameMatches?.groups?.get(1)?.value
        lastName= nameMatches?.groups?.get(2)?.value
    }

    override fun toString(): String {
        return "$firstName $lastName";
    }
}
