package me.rysavys.geneac

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
    }

    private val gedcom: Gedcom

    init {
        val parser = ModelParser()
        gedcom = parser.parseGedcom(File(filename))

        val people = getPeeps(gedcom)
    }

    private fun getPeeps(ged: Gedcom): List<Person> {

        return ged.people.asSequence()
            .map { p -> Person(p, ::parseEvent) }
            .onEach { p -> logger.info("{}", p) }
            .toList()
    }

    private fun parseEvent(event: EventFact) {
        logger.info("{} {} {}", event.tag, event.place, event.date)
    }
}
