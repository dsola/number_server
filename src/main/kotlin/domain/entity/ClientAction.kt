package domain.entity

sealed class ClientAction {
    object Shutdown : ClientAction()
    data class NewValue(val value: String) : ClientAction()
    data class Disconnect(val clientId: String) : ClientAction()
}
