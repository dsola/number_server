package adapter.`in`.http.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

@ObsoleteCoroutinesApi
class ClientConnectionHandler(private val numberDispatcher: ClientInputDispatcher) {
    suspend fun handle(client: Socket, clientId: String) {
        val input = BufferedReader(InputStreamReader(client.inputStream))
        withContext(Dispatchers.IO) {
            while (input.readLine() != null) {
                numberDispatcher.dispatchToChannel(
                    clientId,
                    input.readLine()
                )
            }
        }
    }
}
