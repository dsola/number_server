package adapter.`in`.http

import domain.contract.NumberQueueWriter
import implementation.SimpleSocketClient
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.ServerSocket

@ObsoleteCoroutinesApi
@DelicateCoroutinesApi
@ExtendWith(MockKExtension::class)
class ConcurrentHttpServerTest {
    lateinit var socketServer: ServerSocket

    @BeforeEach
    fun setUp() {
        socketServer = ServerSocket(5001)
    }

    @Test
    fun `server should add to the queue numbers processed by client`(@MockK queueWriter: NumberQueueWriter) {
        val concurrentHttpServer = ConcurrentHttpServer(queueWriter, socketServer)
        val inputValue = "123456"
        concurrentHttpServer.start()
        val client = SimpleSocketClient.startConnection("127.0.0.1", socketServer.localPort)
        justRun { queueWriter.writeNewNumber(inputValue.toInt()) }

        client.sendMessage(inputValue)

        verify(exactly = 1) { queueWriter.writeNewNumber(inputValue.toInt()) }

        client.stopConnection()
        socketServer.close()
    }

    @AfterEach
    fun tearDown() {
        if (!socketServer.isClosed) {
            socketServer.close()
        }
    }
}
