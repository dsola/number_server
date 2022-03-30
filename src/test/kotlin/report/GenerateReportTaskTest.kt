package report

import output.persistence.ReportHistoryRepository
import generator.NumberGenerator
import implementation.TestNumberHistoryRepository
import implementation.TestReportHistoryRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class GenerateReportTaskTest {
    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @Test
    fun `display zero values if no numbers were introduced in stdout`() {
        val task = GenerateReportTask(
            TestNumberHistoryRepository(
                listOf(),
                listOf()
            ),
            TestReportHistoryRepository(ReportResult(0,0))
        )

        task.run()

        assertTrue(
            "Unique counter is wrong in stdout"
        ) { outputStreamCaptor.toString().contains("Unique total: 0") }
        assertTrue(
            "Unique counter from last report is wrong in stdout"
        ) { outputStreamCaptor.toString().contains("0 unique numbers") }
        assertTrue(
            "Duplicate counter from last report is wrong in stdout"
        ) { outputStreamCaptor.toString().contains("0 duplicates") }
    }

    @Test
    fun `display correct count of total unique numbers in stdout`() {
        val numberOfItems = Random.nextInt(1, 10)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(
                NumberGenerator.generateRandomList(numberOfItems),
                NumberGenerator.generateRandomList(numberOfItems)
            ),
            TestReportHistoryRepository(ReportResult(0,0))
        )

        task.run()

        assertTrue(
            "Unique counter is wrong in stdout"
        ) { outputStreamCaptor.toString().contains("Unique total: $numberOfItems") }
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

        assertTrue(
            "Unique counter from last report is wrong in stdout"
        ) { outputStreamCaptor.toString().contains("3 unique numbers") }
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

        assertTrue(
            "Duplicate counter from last report is wrong in stdout"
        ) { outputStreamCaptor.toString().contains("2 duplicates") }
    }

    @Test
    fun `display absolute unique counter if it is the first report in stdout`() {
        val numberOfItems = 5
        val uniqueNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val duplicateNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(uniqueNumbers, duplicateNumbers),
            TestReportHistoryRepository(ReportResult(0,0))
        )

        task.run()

        assertTrue("Unique counter from last report is wrong in stdout") { outputStreamCaptor.toString().contains("$numberOfItems unique numbers") }
    }

    @Test
    fun `display absolute duplicate counter if it is the first report in stdout`() {
        val numberOfItems = 5
        val duplicateNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(listOf(), duplicateNumbers),
            TestReportHistoryRepository(ReportResult(0,0))
        )

        task.run()

        assertTrue("Duplicate counter from last report is wrong in stdout") { outputStreamCaptor.toString().contains("$numberOfItems duplicates") }
    }

    @Test
    fun `results from current report are stored`(@MockK reportHistoryRepository: ReportHistoryRepository) {
        val uniqueNumbers = NumberGenerator.generateRandomList(4)
        val duplicateNumbers = NumberGenerator.generateRandomList(6)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(uniqueNumbers, duplicateNumbers),
            reportHistoryRepository
        )
        every { reportHistoryRepository.getResultsFromLastReport() } returns ReportResult(3, 3)
        justRun { reportHistoryRepository.saveResultFromCurrentReport(any()) }
        task.run()

        verify(exactly = 1) {
            reportHistoryRepository.saveResultFromCurrentReport(
                withArg {
                    assertEquals(4, it.countUniqueNumbers)
                    assertEquals(6, it.countDuplicateNumbers)
                }
            )
        }
    }

    @Test
    fun `do not display a negative result from last report`(@MockK reportHistoryRepository: ReportHistoryRepository) {
        val numberOfItems = 5
        val uniqueNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val duplicateNumbers = NumberGenerator.generateRandomList(numberOfItems)
        val task = GenerateReportTask(
            TestNumberHistoryRepository(uniqueNumbers, duplicateNumbers),
            TestReportHistoryRepository(
                ReportResult(7,7)
            )
        )

        task.run()

        assertTrue("Unique counter from last report is wrong in stdout") {
            outputStreamCaptor.toString().contains("0 unique numbers")
        }

        assertTrue("Unique counter from last report is wrong in stdout") {
            outputStreamCaptor.toString().contains("0 duplicates")
        }
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
    }
}
