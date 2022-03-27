import adapter.`in`.report.Scheduler
import adapter.`in`.writer.WriteNumber
import adapter.out.FileNumberLogRepository
import adapter.out.InMemoryNumberQueueWriter
import adapter.out.MemoryNumberHistoryRepository
import adapter.out.MemoryReportHistoryRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
fun main(args: Array<String>) {
    val numberHistoryRepository = MemoryNumberHistoryRepository()
    val numberReportHistoryRepository = MemoryReportHistoryRepository()
    val logRepository = FileNumberLogRepository.generateForPath(
        "${System.getProperty("user.dir")}/log/numbers.log"
    )
    val queue = InMemoryNumberQueueWriter(
        WriteNumber(numberHistoryRepository, logRepository)
    )
    Scheduler(
        numberHistoryRepository,
        numberReportHistoryRepository
    ).execute(10)
    queue.start(1)
    queue.writeNewNumber(15)
    queue.writeNewNumber(15)
    queue.writeNewNumber(15)
    queue.writeNewNumber(30)
    queue.writeNewNumber(40)
    queue.writeNewNumber(50)

}
