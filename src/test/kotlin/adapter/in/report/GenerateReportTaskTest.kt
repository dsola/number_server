package adapter.`in`.report

import domain.generator.NumberGenerator
import adapter.out.TestNumberHistoryRepository
import adapter.out.TestReportHistoryRepository
import domain.entity.ReportResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.random.Random
import kotlin.test.assertTrue

class GenerateReportTaskTest {
    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @Test
    fun `display correct count of total unique numbers in stdout`() {
        val numberOfItems = Random.nextInt(1, 10)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(
                NumberGenerator.generateRandomList(numberOfItems),
                NumberGenerator.generateRandomList(numberOfItems)
            ),
            TestReportHistoryRepository(null)
        )

        task.run()

        assertTrue { outputStreamCaptor.toString().contains("Unique total: $numberOfItems") }
    }

    @Test
    fun `display correct diff of unique numbers from last report in stdout`() {
        val numberOfItems = 5
        val uniqueNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val duplicateNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val reportResults = ReportResult(2, 1)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(uniqueNumbers, duplicateNumbers),
            TestReportHistoryRepository(reportResults)
        )

        task.run()

        assertTrue { outputStreamCaptor.toString().contains("3 unique numbers") }
    }

    @Test
    fun `display correct diff of duplicate numbers from last report in stdout`() {
        val numberOfItems = 5
        val uniqueNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val duplicateNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val reportResults = ReportResult(2, 3)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(uniqueNumbers, duplicateNumbers),
            TestReportHistoryRepository(reportResults)
        )

        task.run()

        assertTrue { outputStreamCaptor.toString().contains("2 duplicates") }
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
    }
}
