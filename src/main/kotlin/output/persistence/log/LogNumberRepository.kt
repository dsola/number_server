package output.persistence.log

import output.persistence.NumberRepository
import java.io.File

class LogNumberRepository(private val file: File) : NumberRepository {
    override suspend fun write(number: Int) {
        file.appendText("$number\n")
    }

    companion object {
        fun generateForPath(filePath: String): NumberRepository {
            val file = File(filePath)
            file.createNewFile()
            file.writeText("")
            return LogNumberRepository(file)
        }
    }
}
