package adapter.`in`.writer

import domain.contract.NumberHistoryRepository

class WriteNumber(private val numberHistoryRepository: NumberHistoryRepository) {
    fun write(number: Int) {
        numberHistoryRepository.persistUniqueNumber(number)
    }
}