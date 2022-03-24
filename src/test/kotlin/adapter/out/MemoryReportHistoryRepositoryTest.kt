package adapter.out

import domain.entity.ReportResult
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MemoryReportHistoryRepositoryTest {

    @Test
    fun `return null report if no previous reports were stored`() {
        val repository = MemoryReportHistoryRepository()

        assertNull(repository.getResultsFromLastReport())
    }

    @Test
    fun `return last report when multiple reports were persisted`() {
        val repository = MemoryReportHistoryRepository()
        val firstReport = ReportResult(1,2)
        val secondReport = ReportResult(5,6)

        repository.saveResultFromCurrentReport(firstReport)
        repository.saveResultFromCurrentReport(secondReport)

        assertEquals(secondReport, repository.getResultsFromLastReport())
    }
}