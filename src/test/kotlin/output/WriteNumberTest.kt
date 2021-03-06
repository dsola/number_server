package output

import output.persistence.NumberHistoryRepository
import output.persistence.NumberRepository
import generator.NumberGenerator
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class WriteNumberTest {
    @Test
    fun `write unique provided number if it was not in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns false
        justRun { runBlocking {  numberHistoryRepository.persistNumber(number) } }
        justRun { runBlocking { logRepository.write(number) } }

        writeNumber.write(number)

        verify(exactly = 1) { runBlocking {  numberHistoryRepository.persistNumber(number) } }
    }

    @Test
    fun `write number in log if it was not in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        justRun { runBlocking {  numberHistoryRepository.persistNumber(number) } }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns false
        justRun { runBlocking { numberHistoryRepository.persistNumber(number) } }
        justRun { runBlocking { logRepository.write(number) } }

        writeNumber.write(number)

        verify(exactly = 1) { runBlocking { logRepository.write(number) } }
    }

    @Test
    fun `not write number in log if it was in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        justRun { runBlocking { numberHistoryRepository.persistNumber(number) } }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns true
        justRun { runBlocking { numberHistoryRepository.persistNumber(number) } }
        justRun { runBlocking { logRepository.write(number) } }

        writeNumber.write(number)

        verify(exactly = 0) { runBlocking { logRepository.write(number) } }
    }
}
