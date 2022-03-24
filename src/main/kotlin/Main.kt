import adapter.`in`.report.Scheduler
import adapter.out.MemoryNumberHistoryRepository
import adapter.out.MemoryReportHistoryRepository

fun main(args: Array<String>) {
    Scheduler(
        MemoryNumberHistoryRepository(),
        MemoryReportHistoryRepository()
    ).execute(1)
}
