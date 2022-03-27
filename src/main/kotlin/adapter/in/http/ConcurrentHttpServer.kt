package adapter.`in`.http

import domain.contract.NumberQueueWriter
import kotlinx.coroutines.*
import java.net.ServerSocket

@DelicateCoroutinesApi
class ConcurrentHttpServer(private val queueWriter: NumberQueueWriter, private val server: ServerSocket) {
    private val clientConnections: MutableList<Job> = mutableListOf()
    fun start() {
        GlobalScope.launch(Dispatchers.IO) {
            println("Server is running on port ${server.localPort}")
            while (true) {
                val client = server.accept()
                if (clientConnections.size == 5) {
                    println("Too many concurrent connections")
                } else {
                    println("Client connected: ${client.inetAddress.hostAddress}")
                    clientConnections.add(launch { HandleClientConnection(client, queueWriter).run() })
                }
            }
        }
    }
}