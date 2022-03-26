package adapter.`in`.writer

import domain.contract.NumberHistoryRepository
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
    fun `write unique provided number in repository`(@MockK numberHistoryRepository: NumberHistoryRepository) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository)
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns false
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        justRun { numberHistoryRepository.persistDuplicateNumber(number) }

        writeNumber.write(number)

        verify(exactly = 0) { numberHistoryRepository.persistDuplicateNumber(number) }
        verify(exactly = 1) { numberHistoryRepository.persistUniqueNumber(number) }
    }

    @Test
    fun `write number as duplicate if it was already in repository`(@MockK numberHistoryRepository: NumberHistoryRepository) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository)
        justRun { numberHistoryRepository.persistUniqueNumber(number) }
        every { numberHistoryRepository.isNumberAlreadyPersisted(number) } returns true
        justRun { numberHistoryRepository.persistDuplicateNumber(number) }

        writeNumber.write(number)

        verify(exactly = 1) { numberHistoryRepository.persistDuplicateNumber(number) }
        verify(exactly = 0) { numberHistoryRepository.persistUniqueNumber(number) }
    }
}
