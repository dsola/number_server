package adapter.out

import domain.contract.ReportHistoryRepository
import domain.entity.ReportResult

class TestReportHistoryRepository(private val lastReportResult: ReportResult?) : ReportHistoryRepository {
    override fun getResultsFromLastReport(): ReportResult? {
        return lastReportResult
    }

    override fun saveResultFromCurrentReport(reportResult: ReportResult) {}
}
