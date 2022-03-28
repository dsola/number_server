package implementation

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class SimpleSocketClient(
    private val clientSocket: Socket,
    private val output: PrintWriter,
    private val input: BufferedReader
) {
    companion object {
        fun startConnection(ip: String, port: Int): SimpleSocketClient {
            val clientSocket = Socket(ip, port)
            return SimpleSocketClient(
                Socket(ip, port),
                PrintWriter(clientSocket.getOutputStream(), true),
                BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            )
        }
    }

    fun sendMessage(msg: String?): String? {
        output.println(msg)
        return input.readLine()
    }

    fun stopConnection() {
        input.close()
        output.close()
        clientSocket.close()
    }
}