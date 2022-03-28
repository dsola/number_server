package adapter.`in`.http.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.Socket
import java.net.SocketException
import java.util.Scanner
import kotlin.system.exitProcess

@ObsoleteCoroutinesApi
class ClientConnectionHandler(private val numberDispatcher: ClientInputDispatcher) {
    suspend fun handle(client: Socket, clientId: String) {
        val input = Scanner(InputStreamReader(client.inputStream))
        withContext(Dispatchers.IO) {
            try {
                while (input.hasNext()) {
                    val number = input.nextLine()
                    println("Reading $number")
                    numberDispatcher.dispatchToChannel(
                        clientId,
                        number
                    )
                }
                input.close()
                exitProcess(0)
            } catch (e: SocketException) {
                println("The client $clientId was disconnected")
            }
        }
    }
}
