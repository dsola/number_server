package adapter.out

import domain.contract.ReportHistoryRepository
import domain.entity.ReportResult

class MemoryReportHistoryRepository : ReportHistoryRepository {
    override fun getResultsFromLastReport(): ReportResult? {
        return null
    }

    override fun saveResultFromCurrentReport(reportResult: ReportResult) {

    }
}