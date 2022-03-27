package adapter.`in`.writer

import adapter.out.InMemoryNumberQueueWriter
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class InMemoryQueueWriterTest {
    @Test
    suspend fun `job does not process any number if queue is empty`(@MockK writeNumber: WriteNumber) {
        val queueWriter = InMemoryNumberQueueWriter(writeNumber)
        justRun { writeNumber.write(any()) }

        queueWriter.start(5)

        verify(exactly = 0) { writeNumber.write(any()) }
    }
}