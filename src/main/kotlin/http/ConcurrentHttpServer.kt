package http

import http.client.ClientActionHandler
import http.client.ClientConnectionHandler
import http.client.ClientInputDispatcher
import http.client.ClientAction
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import java.net.ServerSocket
import java.net.Socket
import java.util.*

@DelicateCoroutinesApi
@ObsoleteCoroutinesApi
class ConcurrentHttpServer(
    private val server: ServerSocket,
    private val clientConnections: MutableMap<String, Pair<Socket, Job>> = mutableMapOf(),
    private val clientActionHandler: ClientActionHandler
) {

    fun start() = runBlocking {
        println("Server is running on port ${server.localPort}")
        val channel = actor<ClientAction> {
            consumeEach { action -> clientActionHandler.handle(action) }
        }
        val clientConnectionHandler = ClientConnectionHandler(
            ClientInputDispatcher(channel)
        )

        launch {
            while (!server.isClosed) {
                val client = withContext(Dispatchers.IO) {
                    server.accept()
                }
                if (clientConnections.size == 5) {
                    println("Too many concurrent connections")
                    withContext(Dispatchers.IO) {
                        client.close()
                    }
                } else {
                    val clientId = UUID.randomUUID().toString()
                    println("Client connected: $clientId")
                    clientConnections[clientId] = Pair(
                        client,
                        launch { clientConnectionHandler.handle(client, clientId) },
                    )
                }
            }
        }

        for (clientConnection in clientConnections.values) {
            clientConnection.second.join()
        }
    }
}
