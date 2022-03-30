package output

import output.persistence.NumberHistoryRepository
import output.persistence.NumberRepository
import kotlinx.coroutines.runBlocking

class WriteNumber(
    private val numberHistoryRepository: NumberHistoryRepository,
    private val logRepository: NumberRepository
) {
    fun write(number: Int) = runBlocking {
        if (!numberHistoryRepository.isNumberAlreadyPersisted(number)) {
            logRepository.write(number)
        }
        numberHistoryRepository.persistNumber(number)
    }
}
