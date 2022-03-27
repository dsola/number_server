package adapter.out

import domain.contract.NumberLogRepository
import java.io.File

class FileNumberLogRepository(private val file: File) : NumberLogRepository {
    override fun writeNumberInLog(number: Int) {
        file.appendText("$number\n")
    }

    companion object {
        fun generateForPath(filePath: String): NumberLogRepository {
            val file = File(filePath)
            file.createNewFile()
            return FileNumberLogRepository(file)
        }
    }
}
