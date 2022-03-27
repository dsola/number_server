package adapter.out

import generator.NumberGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class MemoryNumberRepositoryTest {
    private val repository: MemoryNumberHistoryRepository = MemoryNumberHistoryRepository()

    @Test
    fun `zero unique numbers found if nothing was saved`() {
        assertEquals(0, repository.countUniqueNumbers())
    }

    @Test
    fun `zero duplicate numbers found if nothing was saved`() {
        assertEquals(0, repository.countDuplicateNumbers())
    }

    @Test
    fun `one unique number found if it was persisted before`() = runTest {
        val number = NumberGenerator.generateRandomNumber()
        repository.persistNumber(number)

        assertEquals(1, repository.countUniqueNumbers())
    }

    @Test
    fun `multiple unique numbers found if multiple were persisted before`() = runTest {
        val number = NumberGenerator.generateRandomNumber()
        val number2 = NumberGenerator.generateRandomNumber()
        val number3 = NumberGenerator.generateRandomNumber()

        repository.persistNumber(number)
        repository.persistNumber(number2)
        repository.persistNumber(number3)

        assertEquals(3, repository.countUniqueNumbers())
    }

    @Test
    fun `one duplicate number found if it was already there`() = runTest {
        val number = NumberGenerator.generateRandomNumber()
        repository.persistNumber(number)
        repository.persistNumber(number)

        assertEquals(1, repository.countDuplicateNumbers())
    }

    @Test
    fun `multiple duplicates found if persisted more than two times but only count one`() = runTest {
        val number = NumberGenerator.generateRandomNumber()
        repository.persistNumber(number)
        repository.persistNumber(number)
        repository.persistNumber(number)

        assertEquals(1, repository.countDuplicateNumbers())
    }

    @Test
    fun `number is not count as unique if persisted more than once`() = runTest {
        val number = NumberGenerator.generateRandomNumber()
        repository.persistNumber(number)
        repository.persistNumber(number)

        assertEquals(0, repository.countUniqueNumbers())
    }

    @Test
    fun `inform that number was persisted`() = runTest {
        val number = NumberGenerator.generateRandomNumber()

        repository.persistNumber(number)

        assertTrue { repository.isNumberAlreadyPersisted(number) }
    }

    @Test
    fun `inform that number was not persisted`() {
        val number = NumberGenerator.generateRandomNumber()

        assertFalse { repository.isNumberAlreadyPersisted(number) }
    }
}
