package output.persistence.memory

import output.persistence.ReportHistoryRepository
import report.ReportResult

class MemoryReportHistoryRepository : ReportHistoryRepository {
    // TODO: Define a default report result with 0,0
    private var lastReportResult: ReportResult? = null

    override fun getResultsFromLastReport(): ReportResult? {
        return lastReportResult
    }

    override fun saveResultFromCurrentReport(reportResult: ReportResult) {
        lastReportResult = reportResult
    }
}
