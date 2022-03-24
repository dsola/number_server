package adapter.`in`.report

import domain.contract.NumberHistoryRepository
import java.util.Timer

class Scheduler(val numberHistoryRepository: NumberHistoryRepository) {

    fun execute(seconds: Long) {
        Timer().scheduleAtFixedRate(
            GenerateReportTask(numberHistoryRepository),
            seconds * 1000,
            seconds * 1000
        )
    }
}
