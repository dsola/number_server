package domain.contract

interface NumberHistoryRepository {
    fun countUniqueNumbers(): Int
    fun countDuplicateNumbers(): Int
}
