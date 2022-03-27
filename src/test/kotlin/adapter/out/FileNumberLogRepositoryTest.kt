package adapter.out
import generator.NumberGenerator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals

class FileNumberLogRepositoryTest {

    private lateinit var file: File
    @BeforeEach
    fun setUp() {
        val currentDir = System.getProperty("user.dir")
        file = File("$currentDir/src/test/kotlin/file/numbers.log")
        file.createNewFile()
    }

    @Test
    fun `write number in file provided in constructor`() {
        file.createNewFile()
        val number = NumberGenerator.generateRandomNumber()
        val repository = FileNumberLogRepository(file)

        repository.writeNumberInLog(number)

        assertContains(file.readText(), number.toString())
    }

    @Test
    fun `write multiple numbers number in file provided in constructor`() {
        val number = NumberGenerator.generateRandomNumber()
        val number2 = NumberGenerator.generateRandomNumber()
        val repository = FileNumberLogRepository(file)

        repository.writeNumberInLog(number)
        repository.writeNumberInLog(number2)

        assertEquals("$number\n$number2\n", file.readText())
    }

    @AfterEach
    fun tearDown() {
        file.writeText("")
    }
}
