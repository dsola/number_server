package adapter.`in`.report

import java.util.Timer

class Scheduler() {

    fun execute(seconds: Long) {
        // TODO: Check ScheduledExecutorService
        Timer().scheduleAtFixedRate(
            GenerateReportTask(),
            seconds * 1000,
            seconds * 1000
        )
    }
}
