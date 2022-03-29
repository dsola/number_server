package output.persistence.file

import output.persistence.NumberLogRepository
import java.io.File

class FileNumberLogRepository(private val file: File) : NumberLogRepository {
    override suspend fun writeNumberInLog(number: Int) {
        file.appendText("$number\n")
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
