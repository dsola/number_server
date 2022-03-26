package domain.contract

interface NumberHistoryRepository {
    fun countUniqueNumbers(): Int
    fun countDuplicateNumbers(): Int
    fun persistUniqueNumber(number: Int)
    fun persistDuplicateNumber(number: Int)
    fun isNumberAlreadyPersisted(number: Int): Boolean
}
