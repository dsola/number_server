package output.persistence
// TODO: You can remove log from here and implement logRepository
interface NumberLogRepository {
    // TODO: can be called write
    suspend fun writeNumberInLog(number: Int)
}