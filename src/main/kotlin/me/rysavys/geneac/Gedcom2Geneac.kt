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
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class Gedcom2Geneac() : CliktCommand() {

    val input:String by option("-i", "--input", help="Input GEDCOM file").required()
    val output:String? by option("-o", "--output", help="Output JSON archive")

    companion object {
        val gson: Gson = GedGsonBuilder.create()
    }

    private var person_counter: Int = 0
    private var fact_counter: Int = 0
    private var people: List<Person> = emptyList()
    private var facts: MutableList<Fact> = ArrayList()

    private val people_by_ged_id: MutableMap<String, Person> = HashMap()

    override fun run() {
        val gedcom = ModelParser().parseGedcom(File(input))

        echo("Processing people and events/facts")
        people = getPeeps(gedcom)

        echo("Processing family relationships")
        populateRelationships(gedcom)

        val outFileName: String = output ?: basename(Paths.get(input).toString()) + ".zip"
        echo("Outputting to $outFileName")

        val fos = FileOutputStream(outFileName)
        val zipOut = ZipOutputStream(fos)

        val personJson = gson.toJson(people)
        val peepEntry = ZipEntry("Person.json")
        zipOut.putNextEntry(peepEntry)
        zipOut.write(personJson.toByteArray(StandardCharsets.UTF_8))

        val factJson = gson.toJson(facts)
        val factEntry = ZipEntry("Fact.json")
        zipOut.putNextEntry(factEntry)
        zipOut.write(factJson.toByteArray(StandardCharsets.UTF_8))

        zipOut.close()
        fos.close()
    }

    private fun getPeeps(ged: Gedcom): List<Person> {
        return ged.people.asSequence()
            .map { p -> Person(person_counter++, p, ::parseEvent) }
            .onEach { p -> people_by_ged_id.put(p.ged_id, p) }
            .toList()
    }

    private fun populateRelationships(ged: Gedcom) {
        ged.families.asSequence().forEach { fam ->
            // TODO: I guess maybe support polygamy?
            val husGedId = fam.husbandRefs[0].ref
            val wifGedId = fam.wifeRefs[0].ref
            val husId = people_by_ged_id[husGedId]?.id
            val wifId = people_by_ged_id[wifGedId]?.id

            if (husGedId != null && wifGedId != null) {
                people_by_ged_id[husGedId]?.current_spouse_id = wifId
                people_by_ged_id[wifGedId]?.current_spouse_id = husId
            }

            for (childRef in fam.childRefs) {
                people_by_ged_id[childRef.ref]?.father_id = husId
                people_by_ged_id[childRef.ref]?.mother_id = wifId
            }
        }
    }

    private fun parseEvent(src: ModelObj, event: EventFact) {
        facts.add(Fact(fact_counter++, src, event))
    }

    private fun basename(filename: String): String {
        val extIdx = filename.lastIndexOf('.')
        return filename.substring(0, extIdx)
    }
}
