package adapter.`in`.http.client

import domain.entity.ClientAction
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@DelicateCoroutinesApi
@ExtendWith(MockKExtension::class)
class ClientInputDispatcherTest {
    @Test
    fun `send as new value if number is correct`(@MockK channel: SendChannel<ClientAction>) = runBlocking {
        val clientId = "clientId"
        val number = "123456789"

        justRun { runBlocking { channel.send(any()) } }

        val dispatcher = ClientInputDispatcher(channel)

        dispatcher.dispatchToChannel(clientId, number)

        verify { runBlocking { channel.send(ClientAction.NewValue(number)) } }
    }

    @Test
    fun `send as disconnect with client ID if number is incorrect`(@MockK channel: SendChannel<ClientAction>) = runBlocking {
        val clientId = "clientId"
        val number = "I am not a number"

        justRun { runBlocking { channel.send(any()) } }

        val dispatcher = ClientInputDispatcher(channel)

        dispatcher.dispatchToChannel(clientId, number)

        verify { runBlocking { channel.send(ClientAction.Disconnect(clientId)) } }
    }

    @Test
    fun `send as shutdown if the message is terminate`(@MockK channel: SendChannel<ClientAction>) = runBlocking {
        val clientId = "clientId"
        val number = "terminate"

        justRun { runBlocking { channel.send(any()) } }

        val dispatcher = ClientInputDispatcher(channel)

        dispatcher.dispatchToChannel(clientId, number)

        verify { runBlocking { channel.send(ClientAction.Shutdown) } }
    }
}
