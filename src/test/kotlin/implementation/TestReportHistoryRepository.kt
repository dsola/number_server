package implementation

import output.persistence.ReportHistoryRepository
import report.ReportResult

class TestReportHistoryRepository(private val lastReportResult: ReportResult?) : ReportHistoryRepository {
    override fun getResultsFromLastReport(): ReportResult? {
        return lastReportResult
    }

    override fun saveResultFromCurrentReport(reportResult: ReportResult) {}
}
