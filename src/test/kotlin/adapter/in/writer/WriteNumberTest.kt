package adapter.`in`.writer

import domain.contract.NumberHistoryRepository
import domain.contract.NumberLogRepository
import domain.generator.NumberGenerator
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

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
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        justRun { numberHistoryRepository.persistDuplicateNumber(number) }
        justRun { logRepository.writeNumberInLog(number) }

        writeNumber.write(number)

        verify(exactly = 0) { numberHistoryRepository.persistDuplicateNumber(number) }
        verify(exactly = 1) { numberHistoryRepository.persistUniqueNumber(number) }
    }

    @Test
    fun `write number as duplicate if it was already in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberLogRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns true
        justRun { numberHistoryRepository.persistDuplicateNumber(number) }

        writeNumber.write(number)

        verify(exactly = 1) { numberHistoryRepository.persistDuplicateNumber(number) }
        verify(exactly = 0) { numberHistoryRepository.persistUniqueNumber(number) }
    }

    @Test
    fun `write number in log if it was not in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberLogRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns false
        justRun { numberHistoryRepository.persistDuplicateNumber(number) }
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        justRun { logRepository.writeNumberInLog(number) }

        writeNumber.write(number)

        verify(exactly = 1) { logRepository.writeNumberInLog(number) }
    }

    @Test
    fun `not write number in log if it was in repository`(
        @MockK numberHistoryRepository: NumberHistoryRepository,
        @MockK logRepository: NumberLogRepository
    ) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository, logRepository)
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns true
        justRun { numberHistoryRepository.persistDuplicateNumber(number) }
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        justRun { logRepository.writeNumberInLog(number) }

        writeNumber.write(number)

        verify(exactly = 0) { logRepository.writeNumberInLog(number) }
    }
}
