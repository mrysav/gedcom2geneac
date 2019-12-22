package me.rysavys.geneac.model.person

import me.rysavys.geneac.gson.SkipSerialisation
import me.rysavys.geneac.model.ModelObj
import org.folg.gedcom.model.EventFact

data class Person (override val id: Int,
                  @SkipSerialisation val gedPerson: org.folg.gedcom.model.Person,
                  @SkipSerialisation val globalEventCallback: (ModelObj, EventFact) -> Unit) : ModelObj {

    @SkipSerialisation
    val name: Name
    val first_name: String?
    val last_name: String?

    @SkipSerialisation
    val alternateNames: List<Name>
    val alternate_names: String?

    var gender: String? = null

    var birth_date_string: String? = null
    var birthplace: String? = null
    var death_date_string: String? = null
    var deathplace: String? = null
    var burial_date_string: String? = null
    var burialplace: String? = null

    init {
        val names = gedPerson.names.map { n -> Name(n) }

        name = names.first()
        first_name = name.firstName
        last_name = name.lastName

        alternateNames = names.drop(1)
        alternate_names = if (alternateNames.isNotEmpty()) {
            alternateNames.joinToString(", ")
        } else {
            null
        }

        gedPerson.eventsFacts.forEach { e -> parseEvent(e, globalEventCallback) }
    }

    private fun parseEvent(event: EventFact, globalEventCallback: (ModelObj, EventFact) -> Unit) {
        when (event.tag) {
            "BIRT" -> {
                birth_date_string = event.date
                birthplace = event.place
            }
            "DEAT" -> {
                death_date_string = event.date
                deathplace = event.place
            }
            "BURI" -> {
                burial_date_string = event.date
                burialplace = event.place
            }
            "SEX" -> gender = event.value
            else -> globalEventCallback(this, event)
        }
    }
}
