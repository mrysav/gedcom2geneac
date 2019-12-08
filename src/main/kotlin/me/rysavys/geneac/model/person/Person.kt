package me.rysavys.geneac.model.person

import me.rysavys.geneac.GedcomImporter
import org.folg.gedcom.model.EventFact
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Person(gedPerson: org.folg.gedcom.model.Person, globalEventCallback: (EventFact) -> Unit) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GedcomImporter::class.java)
    }

    val name: Name
    val alternateNames: List<Name>

    var gender: String? = null

    var birth_date: String? = null
    var birth_place: String? = null
    var death_date: String? = null
    var death_place: String? = null
    var burial_date: String? = null
    var burial_place: String? = null

    init {
        val names = gedPerson.names.map { n -> Name(n) }

        name = names.first()
        alternateNames = names.drop(1)

        gedPerson.eventsFacts.forEach { e -> parseEvent(e, globalEventCallback) }
    }

    private fun parseEvent(event: EventFact, globalEventCallback: (EventFact) -> Unit) {
        when(event.tag) {
            "BIRT" -> {
                birth_date = event.date
                birth_place = event.place
            }
            "DEAT" -> {
                death_date = event.date
                death_place = event.place
            }
            "BURI" -> {
                burial_date = event.date
                burial_place = event.place
            }
            "SEX" -> gender = event.value
            else -> globalEventCallback(event)
        }
    }

    override fun toString(): String {
        return "Person(name=$name, alternateNames=$alternateNames, gender=$gender, birth_date=$birth_date, birth_place=$birth_place, death_date=$death_date, death_place=$death_place, burial_date=$burial_date, burial_place=$burial_place)"
    }

}
