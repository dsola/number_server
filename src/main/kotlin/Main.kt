import adapter.`in`.report.Scheduler
import adapter.out.MemoryNumberHistoryRepository

fun main(args: Array<String>) {
    Scheduler(
        MemoryNumberHistoryRepository()
    ).execute(1)
}
