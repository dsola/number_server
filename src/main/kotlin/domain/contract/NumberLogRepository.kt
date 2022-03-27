package domain.contract

interface NumberLogRepository {
    suspend fun writeNumberInLog(number: Int)
}