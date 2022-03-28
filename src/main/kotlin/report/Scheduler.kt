package report

import output.persistence.NumberHistoryRepository
import output.persistence.ReportHistoryRepository
import java.util.Timer
import kotlinx.coroutines.runBlocking

class Scheduler(
    private val numberHistoryRepository: NumberHistoryRepository,
    private val reportHistoryRepository: ReportHistoryRepository
) {

    fun execute(seconds: Long) = runBlocking {
        Timer().scheduleAtFixedRate(
            GenerateReportTask(numberHistoryRepository, reportHistoryRepository),
            seconds * 1000,
            seconds * 1000
        )
    }
}
