package adapter.`in`.http

import domain.entity.ServerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

@ObsoleteCoroutinesApi
class HandleClientConnection(
    private val client: Socket,
    private val channel: SendChannel<Pair<ServerStatus, String>>
) {
    private var running: Boolean = false
    private val input: BufferedReader = BufferedReader(InputStreamReader(client.inputStream))

    suspend fun run() {
        withContext(Dispatchers.IO) {
            while (input.readLine() != null) {
                readValueFromClient()
            }
        }
    }

    private suspend fun readValueFromClient() {
        var text: String
        withContext(Dispatchers.IO) {
            while (input.readLine() != null) {
                text = input.readLine()
                sendValueToChannel(text)
            }
        }
    }

    private suspend fun sendValueToChannel(text: String) {
        if (text == "shutdown") {
            shutdown()
            channel.send(Pair(ServerStatus.SHUTDOWN, text))
        } else {
            channel.send(Pair(ServerStatus.NEW_VALUE, text))
        }
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }
}
