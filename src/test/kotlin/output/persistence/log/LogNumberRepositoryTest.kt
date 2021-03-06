package output.persistence.log

import generator.NumberGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LogNumberRepositoryTest {

    private lateinit var file: File
    @BeforeEach
    fun setUp() {
        val currentDir = System.getProperty("user.dir")
        file = File("$currentDir/src/test/kotlin/file/numbers.log")
        file.createNewFile()
    }

    @Test
    fun `write number in file provided in constructor`() = runTest {
        withContext(Dispatchers.IO) {
            file.createNewFile()
        }
        val number = NumberGenerator.generateRandomNumber()
        val repository = LogNumberRepository(file)

        repository.write(number)

        assertContains(file.readText(), number.toString())
    }

    @Test
    fun `write multiple numbers number in file provided in constructor`() = runTest {
        val number = NumberGenerator.generateRandomNumber()
        val number2 = NumberGenerator.generateRandomNumber()
        val repository = LogNumberRepository(file)

        repository.write(number)
        repository.write(number2)

        assertEquals("$number\n$number2\n", file.readText())
    }

    @AfterEach
    fun tearDown() {
        file.writeText("")
    }
}