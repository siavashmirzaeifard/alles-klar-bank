package co.alles_klar.bank.transaction.database.repository

import co.alles_klar.bank.transaction.database.entity.TransactionEntity
import co.alles_klar.bank.utility.constant.TransactionId
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository: JpaRepository<TransactionEntity, TransactionId>