package me.rysavys.geneac

import me.rysavys.geneac.model.Person
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
        val peeps = ArrayList<Person>()


        for (p in ged.people) {
            val peep = Person()
            val fname = p.names[0].value
            val otherNames = p.names.subList(1,p.names.size).joinToString(", ") { n -> n.displayValue }
            peep.firstName = fname
            peep.alternateNames = otherNames
            logger.info("{}", peep)
        }

        return peeps
    }
}
