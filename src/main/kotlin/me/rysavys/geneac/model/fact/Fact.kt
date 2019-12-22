package me.rysavys.geneac.model.fact

import me.rysavys.geneac.gson.SkipSerialisation
import me.rysavys.geneac.model.ModelObj
import org.folg.gedcom.model.EventFact

data class Fact(override val id: Int,
                @SkipSerialisation val factable: ModelObj,
                @SkipSerialisation val eventFact: EventFact) : ModelObj {

    val factable_id: Int = factable.id
    // TODO: Maybe not the best way to get the type
    val factable_type: String = factable.javaClass.simpleName
    val date_string: String? = eventFact.date
    val fact_type: String? = eventFact.displayType
    val place: String? = eventFact.place
    val description: String? = eventFact.value

}
