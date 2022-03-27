package adapter.`in`.writer

import domain.contract.NumberHistoryRepository
import domain.contract.NumberLogRepository
import kotlinx.coroutines.runBlocking

class WriteNumber(
    private val numberHistoryRepository: NumberHistoryRepository,
    private val logRepository: NumberLogRepository
) {
    fun write(number: Int) = runBlocking {
        println("New number $number from queue.")
        if (!numberHistoryRepository.isNumberAlreadyPersisted(number)) {
            logRepository.writeNumberInLog(number)
        }
        numberHistoryRepository.persistNumber(number)
    }
}
