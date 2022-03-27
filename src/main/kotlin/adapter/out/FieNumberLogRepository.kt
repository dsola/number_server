package adapter.out

import domain.contract.NumberLogRepository
import java.io.File

class FieNumberLogRepository(private val file: File) : NumberLogRepository {
    override fun writeNumberInLog(number: Int) {
        file.appendText("$number\n")
    }
}
