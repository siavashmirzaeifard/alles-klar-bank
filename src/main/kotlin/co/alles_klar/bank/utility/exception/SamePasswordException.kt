package co.alles_klar.bank.utility.exception

class SamePasswordException: RuntimeException(
    "New password and old password can not be the same"
)