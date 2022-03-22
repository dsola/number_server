package adapter.`in`.report

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class GenerateReportTaskTest {
    private val outputStreamCaptor = ByteArrayOutputStream()
    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }
    @Test
    fun `display last occurrences in stdout`() {
        val task = GenerateReportTask()

        task.run()

        assertEquals("Received 50 unique numbers, 2 duplicates. Unique total: 567231", outputStreamCaptor.toString().trim())
    }
}