package http.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import output.WriteNumber
import java.net.ServerSocket
import java.net.Socket
typealias ClientConnection = Pair<Socket, Job>

class ClientActionHandler(
    private val clientConnections: MutableMap<String, ClientConnection>,
    private val server: ServerSocket,
    private val writeNumber: WriteNumber
) {
    private val mutex = Mutex()
    suspend fun handle(action: ClientAction) {
        when (action) {
            is ClientAction.Shutdown -> shutdownClients()
            is ClientAction.NewValue -> processNewInput(action.value)
            is ClientAction.Disconnect -> disconnectClient(action.clientId)
        }
    }

    private suspend fun shutdownClients() {
        println("Shutting down the system.")
        clientConnections.values.forEach {
            terminateClient(it)
        }
        withContext(Dispatchers.IO) {
            server.close()
        }
    }

    private suspend fun processNewInput(input: Int) {
        println("Writing number $input.")
        writeNumber.write(input)
    }

    private suspend fun disconnectClient(clientId: String) {
        if (clientConnections.containsKey(clientId)) {
            clientConnections[clientId]?.let { terminateClient(it) }
            removeClient(clientId)
        }
    }

    private fun terminateClient(clientConnection: ClientConnection) {
        clientConnection.first.close()
        clientConnection.second.cancel()
    }

    private suspend fun removeClient(clientId: String) {
        mutex.withLock {
            clientConnections.remove(clientId)
        }
    }
}
