package http.client

import validator.NumberValidator
import kotlinx.coroutines.channels.SendChannel

class ClientInputDispatcher(private val channel: SendChannel<ClientAction>) {
    suspend fun dispatchToChannel(clientId: String, message: String) {
        if (NumberValidator.validateNumberIsCorrect(message)) {
            return channel.send(ClientAction.NewValue(message.toInt()))
        }
        if (message == "terminate") {
            return channel.send(ClientAction.Shutdown)
        }
        return channel.send(ClientAction.Disconnect(clientId))
    }
}