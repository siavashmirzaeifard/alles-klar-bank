package co.alles_klar.bank.utility.constant

enum class TransactionTypes(val type: String) {

    DEPOSIT(type = "DEPOSIT"),
    WITHDRAW(type = "WITHDRAW"),
    TRANSFER(type = "TRANSFER")

}