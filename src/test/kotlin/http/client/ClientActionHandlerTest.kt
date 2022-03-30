package http.client

import output.WriteNumber
import generator.NumberGenerator
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Job
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.ServerSocket
import java.net.Socket
import kotlin.test.assertFalse

@ExtendWith(MockKExtension::class)
class ClientActionHandlerTest {
    @Test
    suspend fun `process input received if action is new value`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val number = NumberGenerator.generateRandomNumber()
        justRun { writeNumber.write(number) }
        val clientConnections = mutableMapOf<String, ClientConnection>()

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )

        handler.handle(ClientAction.NewValue(number))

        verify { writeNumber.write(number) }
    }

    @Test
    suspend fun `not break when disconnect does not match with any client`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val clientId = "1"
        val clientConnections = mutableMapOf<String, ClientConnection>()

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )

        handler.handle(ClientAction.Disconnect(clientId))
    }

    @Test
    suspend fun `disconnect client and cancel socket when disconnect action arrives`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val clientId = "1"
        val item = generateClientConnection()
        val clientConnections = mutableMapOf<String, ClientConnection>()
        clientConnections[clientId] = item

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )
        handler.handle(ClientAction.Disconnect(clientId))

        verify { item.clientSocket.close() }
        verify { item.job.cancel() }
        assertFalse { clientConnections.contains(clientId) }
    }

    @Test
    suspend fun `disconnect all clients when shutdown appears`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val clientId1 = "1"
        val clientId2 = "2"
        val item1 = generateClientConnection()
        val item2 = generateClientConnection()
        justRun { server.close() }
        val clientConnections = mutableMapOf<String, ClientConnection>()
        clientConnections[clientId1] = item1
        clientConnections[clientId2] = item2

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )
        handler.handle(ClientAction.Shutdown)

        verify { item1.clientSocket.close() }
        verify { item1.job.cancel() }
        verify { item2.clientSocket.close() }
        verify { item2.job.cancel() }
        verify(exactly = 1) { server.close() }
    }

    private fun generateClientConnection(): ClientConnection {
        val client = mockk<Socket>()
        val job = mockk<Job>()
        justRun { client.close() }
        justRun { job.cancel() }
        return ClientConnection(client, job)
    }
}
