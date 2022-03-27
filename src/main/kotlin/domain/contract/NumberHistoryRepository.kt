package domain.contract

interface NumberHistoryRepository {
    fun countUniqueNumbers(): Int
    fun countDuplicateNumbers(): Int
    suspend fun persistNumber(number: Int)
    fun isNumberAlreadyPersisted(number: Int): Boolean
}
