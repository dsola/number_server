package adapter.`in`.writer

import domain.contract.NumberHistoryRepository
import domain.contract.NumberLogRepository

class WriteNumber(
    private val numberHistoryRepository: NumberHistoryRepository,
    private val logRepository: NumberLogRepository
) {
    fun write(number: Int) {
        if (numberHistoryRepository.isNumberAlreadyPersisted(number)) {
            numberHistoryRepository.persistDuplicateNumber(number)
            return
        }
        numberHistoryRepository.persistUniqueNumber(number)
        logRepository.writeNumberInLog(number)
    }
}
