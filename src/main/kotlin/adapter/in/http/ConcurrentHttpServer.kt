package adapter.`in`.http

import domain.contract.NumberQueueWriter
import domain.entity.ServerStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import java.net.ServerSocket

@DelicateCoroutinesApi
@ObsoleteCoroutinesApi
class ConcurrentHttpServer(private val queueWriter: NumberQueueWriter, private val server: ServerSocket) {
    private val clientConnections: MutableList<Job> = mutableListOf()

    fun start() = runBlocking {
        GlobalScope.launch(Dispatchers.IO) {
            println("Server is running on port ${server.localPort}")
            val channel = actor<Pair<ServerStatus, String>> {
                for (message in channel) {
                    when (message.first) {
                        ServerStatus.SHUTDOWN -> shutdownClients()
                        ServerStatus.NEW_VALUE -> processNewInput(message.second)
                    }
                }
            }
            while (!server.isClosed) {
                val client = server.accept()
                if (clientConnections.size == 5) {
                    println("Too many concurrent connections")
                } else {
                    println("Client connected: ${client.inetAddress.hostAddress}")
                    clientConnections.add(launch { HandleClientConnection(client, channel).run() })
                }

                for (clientConnection in clientConnections) {
                    clientConnection.join()
                }
            }
        }
    }

    private fun shutdownClients() {
        println("Shutting down the system.")
        for (clientConnection in clientConnections) {
            clientConnection.cancel()
        }
        queueWriter.stop()
        server.close()
    }

    private fun processNewInput(input: String) {
        println("Processing new input $input")
        queueWriter.writeNewNumber(input.toInt())
    }
}
