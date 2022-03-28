package output

import output.persistence.NumberHistoryRepository
import output.persistence.NumberLogRepository
import kotlinx.coroutines.runBlocking

class WriteNumber(
    private val numberHistoryRepository: NumberHistoryRepository,
    private val logRepository: NumberLogRepository
) {
    fun write(number: Int) = runBlocking {
        if (!numberHistoryRepository.isNumberAlreadyPersisted(number)) {
            logRepository.writeNumberInLog(number)
        }
        numberHistoryRepository.persistNumber(number)
    }
}
