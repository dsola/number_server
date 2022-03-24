package domain.contract

import domain.entity.ReportResult

interface ReportHistoryRepository {
    fun getResultsFromLastReport(): ReportResult?
    fun saveResultFromCurrentReport(reportResult: ReportResult)
}