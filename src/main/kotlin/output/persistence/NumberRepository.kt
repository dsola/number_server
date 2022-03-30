package output.persistence
interface NumberRepository {
    suspend fun write(number: Int)
}