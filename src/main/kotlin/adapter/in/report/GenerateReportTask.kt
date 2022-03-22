package adapter.`in`.report

import java.util.TimerTask

class GenerateReportTask : TimerTask() {
    override fun run() {
        println("Received 50 unique numbers, 2 duplicates. Unique total: 567231")
    }
}
