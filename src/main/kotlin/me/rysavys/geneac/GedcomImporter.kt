package me.rysavys.geneac

import com.google.gson.Gson
import me.rysavys.geneac.gson.GedGsonBuilder
import me.rysavys.geneac.model.ModelObj
import me.rysavys.geneac.model.fact.Fact
import me.rysavys.geneac.model.person.Person
import org.folg.gedcom.model.EventFact
import org.folg.gedcom.model.Gedcom
import org.folg.gedcom.parser.ModelParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class GedcomImporter(filename: String) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GedcomImporter::class.java)
        val gson: Gson = GedGsonBuilder.create()
    }

    private val gedcom: Gedcom

    private var person_counter: Int = 0
    private var fact_counter: Int = 0
    private var people: List<Person>
    private var facts: MutableList<Fact> = ArrayList()

    init {
        val parser = ModelParser()
        gedcom = parser.parseGedcom(File(filename))

        people = getPeeps(gedcom)

        //logger.info("{}", gson.toJson(people))
        logger.info("{}", gson.toJson(facts))
    }

    private fun getPeeps(ged: Gedcom): List<Person> {
        return ged.people.asSequence()
            .map { p -> Person(person_counter, p, ::parseEvent) }
            .onEach { p -> person_counter++ }
            .toList()
    }

    private fun parseEvent(src: ModelObj, event: EventFact) {
        val fact = Fact(fact_counter, src, event)
        //logger.info("{}", fact)
        facts.add(fact)
        fact_counter++
    }
}
