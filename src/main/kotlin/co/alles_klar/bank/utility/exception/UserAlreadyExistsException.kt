package co.alles_klar.bank.utility.exception

class UserAlreadyExistsException: RuntimeException(
    "User with this username or email already exists"
)