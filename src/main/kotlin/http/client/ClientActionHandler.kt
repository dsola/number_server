package http.client

import output.WriteNumber
import kotlinx.coroutines.Job
import java.net.ServerSocket
import java.net.Socket

class ClientActionHandler(
    private val clientConnections: MutableMap<String, Pair<Socket, Job>>,
    private val server: ServerSocket,
    private val writeNumber: WriteNumber
) {

    fun handle(action: ClientAction) {
        when (action) {
            is ClientAction.Shutdown -> {
                shutdownClients()
            }
            is ClientAction.NewValue -> {
                processNewInput(action.value)
            }
            is ClientAction.Disconnect -> {
                disconnectAndRemoveClient(action.clientId)
            }
        }
    }

    private fun shutdownClients() {
        println("Shutting down the system.")
        for (clientConnection in clientConnections.values) {
            clientConnection.first.close()
            clientConnection.second.cancel()
        }
        server.close()
    }

    private fun processNewInput(input: Int) {
        println("Writing number $input.")
        writeNumber.write(input)
    }

    private fun disconnectAndRemoveClient(clientId: String) {
        println("Removing client $clientId")
        clientConnections[clientId]?.first?.close()
        clientConnections[clientId]?.second?.cancel()
        clientConnections.remove(clientId)
    }
}
