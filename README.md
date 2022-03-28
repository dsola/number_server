# Number server

## Description
- This application runs a concurrent server on the background that supports two-bidding connections via TCP. 
- It supports only 9 digit numbers, and any invalid input will conclude closing the client connection.
- No more than 5 concurrent clients can be connected.

## How it works
Please check the following diagram to have a clear vision of the process flow of each request coming into the server:

## How different connections communicate?
- This application takes advantage of the [Kotlin Coroutines](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/actor.html) to be able to send messages between processed that opened connections to the concurrent server.
- The client connections send messages through an [Actor](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/actor.html), which acts as a messaging queue.

## Tests
The domain logic of the application has been tested using the JUnit library. The implementation details were not tested, such as validating that the socket reads the provided input.
