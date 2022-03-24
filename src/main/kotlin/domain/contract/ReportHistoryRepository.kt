package domain.contract

import domain.entity.ReportResult

interface ReportHistoryRepository {
    fun getResultsFromLastReport(): ReportResult?
}