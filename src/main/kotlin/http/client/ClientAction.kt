package http.client

sealed class ClientAction {
    object Shutdown : ClientAction()
    data class NewValue(val value: Int) : ClientAction()
    data class Disconnect(val clientId: String) : ClientAction()
}
