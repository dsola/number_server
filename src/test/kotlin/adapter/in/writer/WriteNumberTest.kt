package adapter.`in`.writer

import domain.contract.NumberHistoryRepository
import domain.contract.NumberLogRepository
import generator.NumberGenerator
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class WriteNumberTest {
    @Test
    fun `write unique provided number if it was not in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberLogRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns false
        justRun { runBlocking {  numberHistoryRepository.persistNumber(number) } }
        justRun { runBlocking { logRepository.writeNumberInLog(number) } }

        writeNumber.write(number)

        verify(exactly = 1) { runBlocking {  numberHistoryRepository.persistNumber(number) } }
    }

    @Test
    fun `write number in log if it was not in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberLogRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        justRun { runBlocking {  numberHistoryRepository.persistNumber(number) } }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns false
        justRun { runBlocking { numberHistoryRepository.persistNumber(number) } }
        justRun { runBlocking { logRepository.writeNumberInLog(number) } }

        writeNumber.write(number)

        verify(exactly = 1) { runBlocking { logRepository.writeNumberInLog(number) } }
    }

    @Test
    fun `not write number in log if it was in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberLogRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        justRun { runBlocking { numberHistoryRepository.persistNumber(number) } }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns true
        justRun { runBlocking { numberHistoryRepository.persistNumber(number) } }
        justRun { runBlocking { logRepository.writeNumberInLog(number) } }

        writeNumber.write(number)

        verify(exactly = 0) { runBlocking { logRepository.writeNumberInLog(number) } }
    }
}
