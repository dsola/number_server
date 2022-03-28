package adapter.`in`.http

import adapter.`in`.http.client.ClientConnectionHandler
import adapter.`in`.http.client.ClientInputDispatcher
import domain.contract.NumberQueueWriter
import domain.entity.ClientAction
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import java.net.ServerSocket

@DelicateCoroutinesApi
@ObsoleteCoroutinesApi
class ConcurrentHttpServer(
    private val queueWriter: NumberQueueWriter,
    private val server: ServerSocket
) {

    private val clientConnections: MutableMap<String, Job> = mutableMapOf()

    fun start() = runBlocking {
        GlobalScope.launch(Dispatchers.IO) {
            println("Server is running on port ${server.localPort}")
            val channel = actor<ClientAction> {
                consumeEach { action ->
                    when (action) {
                        is ClientAction.Shutdown -> {
                            shutdownClients()
                        }
                        is ClientAction.NewValue -> {
                            processNewInput(action.value)
                        }
                        is ClientAction.Disconnect -> {
                            removeClient(action.clientId)
                        }
                    }
                }
            }
            val clientConnectionHandler = ClientConnectionHandler(
                ClientInputDispatcher(channel)
            )

            while (!server.isClosed) {
                val client = server.accept()
                if (clientConnections.size == 5) {
                    println("Too many concurrent connections")
                } else {
                    val clientId = client.inetAddress.hostAddress
                    println("Client connected: $clientId")
                    clientConnections[clientId] = launch { clientConnectionHandler.handle(client, clientId) }
                }

                for (clientConnection in clientConnections.values) {
                    clientConnection.join()
                }
            }
        }
    }

    private fun shutdownClients() {
        println("Shutting down the system.")
        for (clientConnection in clientConnections.values) {
            clientConnection.cancel()
        }
        queueWriter.stop()
        server.close()
    }

    private fun processNewInput(input: String) {
        println("Processing new input $input")
        queueWriter.writeNewNumber(input.toInt())
    }

    private fun removeClient(clientId: String) {
        println("Removing client $clientId")
        clientConnections.remove(clientId)
    }
}
