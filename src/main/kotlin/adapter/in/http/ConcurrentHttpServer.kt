package adapter.`in`.http

import adapter.`in`.http.client.ClientActionHandler
import adapter.`in`.http.client.ClientConnectionHandler
import adapter.`in`.http.client.ClientInputDispatcher
import domain.entity.ClientAction
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import java.net.ServerSocket
import java.net.Socket

@DelicateCoroutinesApi
@ObsoleteCoroutinesApi
class ConcurrentHttpServer(
    private val server: ServerSocket,
    private val clientConnections: MutableMap<String, Pair<Socket, Job>> = mutableMapOf(),
    private val clientActionHandler: ClientActionHandler
) {

    fun start() = runBlocking {
        GlobalScope.launch(Dispatchers.IO) {
            println("Server is running on port ${server.localPort}")
            val channel = actor<ClientAction> {
                consumeEach { action -> clientActionHandler.handle(action) }
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
                    clientConnections[clientId] = Pair(
                        client,
                        launch { clientConnectionHandler.handle(client, clientId) },
                    )
                }

                for (clientConnection in clientConnections.values) {
                    clientConnection.second.join()
                }
            }
        }
    }
}
