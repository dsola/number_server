package adapter.`in`.http

import domain.contract.NumberQueueWriter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.ServerSocket

class ConcurrentHttpServer {
    @DelicateCoroutinesApi
    fun start(queueWriter: NumberQueueWriter, port: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            startSocket(port)
        }
    }

    private fun startSocket(port: Int) {
        val server = ServerSocket(port)
        println("Server is running on port ${server.localPort}")
        while (true) {
            val client = server.accept()
            println("Client connected: ${client.inetAddress.hostAddress}")
        }
    }
}