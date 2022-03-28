import adapter.`in`.http.ConcurrentHttpServer
import adapter.`in`.http.client.ClientActionHandler
import adapter.`in`.report.Scheduler
import adapter.`in`.writer.WriteNumber
import adapter.out.FileNumberLogRepository
import adapter.out.InMemoryNumberQueueWriter
import adapter.out.MemoryNumberHistoryRepository
import adapter.out.MemoryReportHistoryRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.net.ServerSocket
import java.net.Socket

@ObsoleteCoroutinesApi
@DelicateCoroutinesApi
fun main() {
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

    val clientConnections = mutableMapOf<String, Pair<Socket, Job>>()
    val server = ServerSocket(4000)
    val clientActionHandler = ClientActionHandler(
        clientConnections,
        server,
        queue
    )
    ConcurrentHttpServer(
        server,
        clientConnections,
        clientActionHandler
    ).start()
}
