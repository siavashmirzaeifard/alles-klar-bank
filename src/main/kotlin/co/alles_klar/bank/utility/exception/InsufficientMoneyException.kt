package co.alles_klar.bank.utility.exception

class InsufficientMoneyException: RuntimeException(
    "You don't have enough money for this transaction"
)