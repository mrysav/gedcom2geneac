package me.rysavys.geneac.model

class Person {
    var firstName: String? = null
    var lastName: String? = null
    var alternateNames: String? = null
    var birthday: String? = null
    var deathday: String? = null

    override fun toString(): String {
        return "Person(firstName=$firstName, lastName=$lastName, alternateNames=$alternateNames, birthday=$birthday, deathday=$deathday)"
    }
}
