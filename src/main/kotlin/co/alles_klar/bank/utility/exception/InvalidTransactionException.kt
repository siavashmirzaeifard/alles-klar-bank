package co.alles_klar.bank.utility.exception

class InvalidTransactionException(
    override val message: String?
): RuntimeException(
    message ?: "Invalid transaction"
)