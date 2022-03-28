package output.persistence

interface NumberLogRepository {
    suspend fun writeNumberInLog(number: Int)
}