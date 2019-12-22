package me.rysavys.geneac

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
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

class GedcomConverter() : CliktCommand() {

    val input:String by option("-i", "--input", help="Input GEDCOM file").required()
    val output:String? by option("-o", "--output", help="Output JSON archive")

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GedcomConverter::class.java)
        val gson: Gson = GedGsonBuilder.create()
    }

    private var person_counter: Int = 0
    private var fact_counter: Int = 0
    private var people: List<Person>? = null
    private var facts: MutableList<Fact> = ArrayList()

    override fun run() {
        val gedcom = ModelParser().parseGedcom(File(input))

        people = getPeeps(gedcom)

        //logger.info("{}", gson.toJson(people))
        logger.info("{}", gson.toJson(facts))
    }

    private fun getPeeps(ged: Gedcom): List<Person> {
        return ged.people.asSequence()
            .map { p -> Person(person_counter++, p, ::parseEvent) }
            .toList()
    }

    private fun parseEvent(src: ModelObj, event: EventFact) {
        facts.add(Fact(fact_counter++, src, event))
    }
}
