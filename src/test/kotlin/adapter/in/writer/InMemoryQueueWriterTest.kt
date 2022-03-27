package adapter.`in`.writer

import adapter.out.InMemoryNumberQueueWriter
import generator.NumberGenerator
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.DelicateCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@DelicateCoroutinesApi
@ExtendWith(MockKExtension::class)
class InMemoryQueueWriterTest {
    @Test
    fun `worker does not process any number if queue is empty`(@MockK writeNumber: WriteNumber) {
        val queueWriter = InMemoryNumberQueueWriter(writeNumber)
        justRun { writeNumber.write(any()) }

        queueWriter.start(1)
        queueWriter.stop()

        verify(exactly = 0) { writeNumber.write(any()) }
    }

    @Test
    fun `worker does process one number if one job is added into queue`(@MockK writeNumber: WriteNumber) {
        val queueWriter = InMemoryNumberQueueWriter(writeNumber)
        val number = NumberGenerator.generateRandomNumber()
        justRun { writeNumber.write(any()) }

        queueWriter.start(1)
        queueWriter.writeNewNumber(number)
        queueWriter.stop()

        verify(exactly = 1) { writeNumber.write(number) }
    }

    @Test
    fun `worker does process multiple numbers if multiple jobs are added into queue`(@MockK writeNumber: WriteNumber) {
        val queueWriter = InMemoryNumberQueueWriter(writeNumber)
        val number = NumberGenerator.generateRandomNumber()
        val number2 = NumberGenerator.generateRandomNumber()
        justRun { writeNumber.write(any()) }

        queueWriter.start(1)
        queueWriter.writeNewNumber(number)
        queueWriter.writeNewNumber(number2)
        queueWriter.stop()

        verify(exactly = 1) { writeNumber.write(number) }
        verify(exactly = 1) { writeNumber.write(number2) }
    }

    @Test
    fun `multiple works can process multiple numbers without inconsistency problems`(@MockK writeNumber: WriteNumber) {
        val queueWriter = InMemoryNumberQueueWriter(writeNumber)
        val number = NumberGenerator.generateRandomNumber()
        val number2 = NumberGenerator.generateRandomNumber()
        justRun { writeNumber.write(any()) }

        queueWriter.start(5)
        queueWriter.writeNewNumber(number)
        queueWriter.writeNewNumber(number2)
        queueWriter.stop()

        verify(exactly = 1) { writeNumber.write(number) }
        verify(exactly = 1) { writeNumber.write(number2) }
    }
}