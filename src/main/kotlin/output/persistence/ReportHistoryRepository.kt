package output.persistence

import report.ReportResult

interface ReportHistoryRepository {
    fun getResultsFromLastReport(): ReportResult
    fun saveResultFromCurrentReport(reportResult: ReportResult)
}