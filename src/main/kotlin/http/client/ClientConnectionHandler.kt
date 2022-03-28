package http.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.Socket
import java.net.SocketException
import java.util.Scanner

@ObsoleteCoroutinesApi
class ClientConnectionHandler(private val numberDispatcher: ClientInputDispatcher) {
    suspend fun handle(client: Socket, clientId: String) {
        val input = Scanner(InputStreamReader(client.inputStream))
        withContext(Dispatchers.IO) {
            try {
                while (input.hasNext()) {
                    val number = input.nextLine()
                    numberDispatcher.dispatchToChannel(
                        clientId,
                        number
                    )
                }
                input.close()
            } catch (e: SocketException) {
                println("The client $clientId was disconnected")
            }
        }
    }
}
