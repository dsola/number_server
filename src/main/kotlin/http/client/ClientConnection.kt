package http.client

import kotlinx.coroutines.Job
import java.net.Socket

data class ClientConnection(val clientSocket: Socket, val job: Job)
