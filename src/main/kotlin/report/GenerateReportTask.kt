package report

import output.persistence.NumberHistoryRepository
import output.persistence.ReportHistoryRepository
import java.util.TimerTask

class GenerateReportTask(
    private val numberHistoryRepository: NumberHistoryRepository,
    private val reportHistoryRepository: ReportHistoryRepository
) : TimerTask() {

    override fun run() {
        val resultsFromLastReport = reportHistoryRepository.getResultsFromLastReport()
        val resultsFromCurrentReport = ReportResult(
            numberHistoryRepository.countUniqueNumbers(),
            numberHistoryRepository.countDuplicateNumbers()
        )
        val uniqueNumbersSinceLastReport =
            calculateUniqueNumbersSinceLastReport(resultsFromCurrentReport, resultsFromLastReport)
        val duplicateNumbersSinceLastReport = calculateDuplicateNumbersSinceLastReport(resultsFromCurrentReport, resultsFromLastReport)
        println(
            "Received $uniqueNumbersSinceLastReport unique numbers, $duplicateNumbersSinceLastReport duplicates. Unique total: ${resultsFromCurrentReport.countUniqueNumbers}"
        )
        reportHistoryRepository.saveResultFromCurrentReport(resultsFromCurrentReport)
    }

    private fun calculateUniqueNumbersSinceLastReport(currentReport: ReportResult, previousReport: ReportResult?): Int {
        val uniqueNumbersUntilLastReport = previousReport?.countUniqueNumbers ?: 0

        return maxOf(0, currentReport.countUniqueNumbers - uniqueNumbersUntilLastReport)
    }

    private fun calculateDuplicateNumbersSinceLastReport(currentReport: ReportResult, previousReport: ReportResult?): Int {
        val duplicateNumbersUntilLastReport = previousReport?.countDuplicateNumbers ?: 0

        return maxOf(0, currentReport.countDuplicateNumbers - duplicateNumbersUntilLastReport)
    }
}
