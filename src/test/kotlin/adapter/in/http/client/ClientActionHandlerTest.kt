package adapter.`in`.http.client

import adapter.`in`.writer.WriteNumber
import domain.entity.ClientAction
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
    fun `process input received if action is new value`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val number = NumberGenerator.generateRandomNumber()
        justRun { writeNumber.write(number) }
        val clientConnections = mutableMapOf<String, Pair<Socket, Job>>()

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )

        handler.handle(ClientAction.NewValue(number))

        verify { writeNumber.write(number) }
    }

    @Test
    fun `not break when disconnect does not match with any client`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val clientId = "1"
        val clientConnections = mutableMapOf<String, Pair<Socket, Job>>()

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )

        handler.handle(ClientAction.Disconnect(clientId))
    }

    @Test
    fun `disconnect client and cancel socket when disconnect action arrives`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val clientId = "1"
        val item = generateClientConnection()
        val clientConnections = mutableMapOf<String, Pair<Socket, Job>>()
        clientConnections[clientId] = item

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )
        handler.handle(ClientAction.Disconnect(clientId))

        verify { item.first.close() }
        verify { item.second.cancel() }
        assertFalse { clientConnections.contains(clientId) }
    }

    @Test
    fun `disconnect all clients when shutdown appears`(
        @MockK server: ServerSocket,
        @MockK writeNumber: WriteNumber,
    ) {
        val clientId1 = "1"
        val clientId2 = "2"
        val item1 = generateClientConnection()
        val item2 = generateClientConnection()
        justRun { server.close() }
        val clientConnections = mutableMapOf<String, Pair<Socket, Job>>()
        clientConnections[clientId1] = item1
        clientConnections[clientId2] = item2

        val handler = ClientActionHandler(
            clientConnections,
            server,
            writeNumber
        )
        handler.handle(ClientAction.Shutdown)

        verify { item1.first.close() }
        verify { item1.second.cancel() }
        verify { item2.first.close() }
        verify { item2.second.cancel() }
        verify(exactly = 1) { server.close() }
    }

    private fun generateClientConnection(): Pair<Socket, Job> {
        val client = mockk<Socket>()
        val job = mockk<Job>()
        justRun { client.close() }
        justRun { job.cancel() }
        return Pair(client, job)
    }
}
