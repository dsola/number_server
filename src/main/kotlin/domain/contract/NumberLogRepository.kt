package domain.contract

interface NumberLogRepository {
    fun writeNumberInLog(number: Int)
}