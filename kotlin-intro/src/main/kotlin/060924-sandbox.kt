fun parties(): List<String> = ParliamentMembersData.members.map { it.party }.toSet().sorted()

fun partiesBySize(): List<String> =
//    ParliamentMembersData.members.groupingBy { it.party }.eachCount().toList().sortedBy { it.second }.map { it.first }
    ParliamentMembersData.members.groupBy { it.party }.map { it.key to it.value.size }.sortedBy { it.second }.map { it.first }

fun partyMembers(party: String): List<String> =
    ParliamentMembersData.members.filter { it.party == party }.sortedBy { it.lastname }.map { it.lastname + " " + it.firstname }

fun governmentParties(): List<String> =
    ParliamentMembersData.members.filter { it.minister }.map { it.party }.toSet().sorted()

fun govtPartiesFromLargestPartytoSmallestParty(): List<String> =
    ParliamentMembersData.members.groupBy { it.party }.map { it.key to it.value.size }.sortedByDescending { it.second }.map { it.first }

fun seats(parties: Set<String>): List<Pair<String, Int>> =
    ParliamentMembersData.members.filter { it.party in parties }.groupingBy { it.party }.eachCount().toList()

fun main() {
    println(parties())
    println(partiesBySize())
    println(partyMembers("r"))
    println(governmentParties())
    println(govtPartiesFromLargestPartytoSmallestParty())
    println(seats(setOf("r", "kd", "ps")))

}

