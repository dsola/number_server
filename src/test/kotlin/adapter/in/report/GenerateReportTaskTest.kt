package adapter.`in`.report

import adapter.out.NumberGenerator
import adapter.out.TestNumberHistoryRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.random.Random
import kotlin.test.assertEquals

class GenerateReportTaskTest {
    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @Test
    fun `display last occurrences in stdout`() {
        val numberOfItems = Random.nextInt(1, 10)
        val uniqueNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val duplicateNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(uniqueNumbers, duplicateNumbers)
        )

        task.run()

        assertEquals(
            "Received 50 unique numbers, 2 duplicates. Unique total: $numberOfItems",
            outputStreamCaptor.toString().trim()
        )
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
    }
}
