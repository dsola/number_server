package adapter.`in`.report

import domain.contract.NumberHistoryRepository
import domain.contract.ReportHistoryRepository
import domain.entity.ReportResult
import java.util.TimerTask

class GenerateReportTask(
    private val numberHistoryRepository: NumberHistoryRepository,
    private val reportHistoryRepository: ReportHistoryRepository
) : TimerTask() {

    override fun run() {
        val uniqueNumbersCount = numberHistoryRepository.countUniqueNumbers()
        val duplicateNumbersCount = numberHistoryRepository.countDuplicateNumbers()
        val resultsFromLastReport = reportHistoryRepository.getResultsFromLastReport()
        val resultsFromCurrentReport = ReportResult(
            uniqueNumbersCount,
            duplicateNumbersCount
        )
        val uniqueNumbersSinceLastReport =
            calculateUniqueNumbersSinceLastReport(resultsFromCurrentReport, resultsFromLastReport)
        val duplicateNumbersSinceLastReport = calculateDuplicateNumbersSinceLastReport(resultsFromCurrentReport, resultsFromLastReport)
        println("Received $uniqueNumbersSinceLastReport unique numbers, $duplicateNumbersSinceLastReport duplicates. Unique total: $uniqueNumbersCount")
    }

    private fun calculateUniqueNumbersSinceLastReport(currentReport: ReportResult, previousReport: ReportResult?): Int {
        val uniqueNumbersUntilLastReport = previousReport?.countUniqueNumbers ?: 0

        return currentReport.countUniqueNumbers - uniqueNumbersUntilLastReport
    }

    private fun calculateDuplicateNumbersSinceLastReport(currentReport: ReportResult, previousReport: ReportResult?): Int {
        val duplicateNumbersUntilLastReport = previousReport?.countDuplicateNumbers ?: 0

        return currentReport.countDuplicateNumbers - duplicateNumbersUntilLastReport
    }
}
