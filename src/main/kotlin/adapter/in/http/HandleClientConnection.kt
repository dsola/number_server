package adapter.`in`.http

import domain.contract.NumberQueueWriter
import java.net.Socket
import java.util.Scanner

class HandleClientConnection(private val client: Socket, private val queueWriter: NumberQueueWriter) {
    private var running: Boolean = false
    private val reader: Scanner = Scanner(client.getInputStream())

    fun run() {
        while (running) {
            running = true
            try {
                val text = reader.nextLine()
                println("Client just typed $text")
            } catch (ex: Exception) {
                shutdown()
            }
        }
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }
}
