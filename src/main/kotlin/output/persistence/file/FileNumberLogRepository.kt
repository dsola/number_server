package output.persistence.file

import output.persistence.NumberLogRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

class FileNumberLogRepository(private val file: File) : NumberLogRepository {
    private val mutex = Mutex()
    override suspend fun writeNumberInLog(number: Int) {
        mutex.withLock {
            file.appendText("$number\n")
        }
    }

    companion object {
        fun generateForPath(filePath: String): NumberLogRepository {
            val file = File(filePath)
            file.createNewFile()
            file.writeText("")
            return FileNumberLogRepository(file)
        }
    }
}