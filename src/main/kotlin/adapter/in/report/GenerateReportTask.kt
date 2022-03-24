package adapter.`in`.report

import domain.contract.NumberHistoryRepository
import java.util.TimerTask

class GenerateReportTask(private val numberHistoryRepository: NumberHistoryRepository) : TimerTask() {

    override fun run() {
        val uniqueNumbersCount = numberHistoryRepository.countUniqueNumbers()
        val duplicateNumbersCount = numberHistoryRepository.countDuplicateNumbers()
        println("Received 50 unique numbers, 2 duplicates. Unique total: $uniqueNumbersCount")
    }
}
