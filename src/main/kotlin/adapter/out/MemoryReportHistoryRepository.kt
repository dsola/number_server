package adapter.out

import domain.contract.ReportHistoryRepository
import domain.entity.ReportResult

class MemoryReportHistoryRepository : ReportHistoryRepository {
    private var lastReportResult: ReportResult? = null

    override fun getResultsFromLastReport(): ReportResult? {
        return lastReportResult
    }

    override fun saveResultFromCurrentReport(reportResult: ReportResult) {
        lastReportResult = reportResult
    }
}
