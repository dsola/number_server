package output.persistence.memory

import output.persistence.ReportHistoryRepository
import report.ReportResult

class MemoryReportHistoryRepository : ReportHistoryRepository {
    private var lastReportResult: ReportResult = ReportResult(0, 0)

    override fun getResultsFromLastReport(): ReportResult {
        return lastReportResult
    }

    override fun saveResultFromCurrentReport(reportResult: ReportResult) {
        lastReportResult = reportResult
    }
}
