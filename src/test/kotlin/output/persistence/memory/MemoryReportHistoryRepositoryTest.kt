package output.persistence.memory

import org.junit.jupiter.api.Test
import report.ReportResult
import kotlin.test.assertEquals

class MemoryReportHistoryRepositoryTest {
    @Test
    fun `return last report when multiple reports were persisted`() {
        val repository = MemoryReportHistoryRepository()
        val firstReport = ReportResult(1, 2)
        val secondReport = ReportResult(5, 6)

        repository.saveResultFromCurrentReport(firstReport)
        repository.saveResultFromCurrentReport(secondReport)

        assertEquals(secondReport, repository.getResultsFromLastReport())
    }
}
