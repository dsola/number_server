package adapter.`in`.writer

import domain.contract.NumberHistoryRepository
import domain.generator.NumberGenerator
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class WriteNumberTest {
    @Test
    fun `write provided number in repository`(@MockK numberHistoryRepository: NumberHistoryRepository) {
        val number = NumberGenerator.generateRandomNumber()
        val writeNumber = WriteNumber(numberHistoryRepository)
        justRun { numberHistoryRepository.persistUniqueNumber(number) }

        writeNumber.write(number)

        verify(exactly = 1) { numberHistoryRepository.persistUniqueNumber(number) }
    }
}