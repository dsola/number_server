package adapter.`in`.writer

import domain.contract.NumberHistoryRepository

class WriteNumber(private val numberHistoryRepository: NumberHistoryRepository) {
    fun write(number: Int) {
        if (numberHistoryRepository.isNumberAlreadyPersisted(number)) {
            numberHistoryRepository.persistDuplicateNumber(number)
            return
        }
        numberHistoryRepository.persistUniqueNumber(number)
    }
}