package adapter.`in`.input

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumberValidatorTest {
    @Test
    fun `fail for numbers with different size than nine`() {
        val value = "0111234"

        assertFalse { NumberValidator.validateNumberIsCorrect(value) }
    }

    @Test
    fun `pass for numbers with nine digits`() {
        val value = "123456789"

        assertTrue { NumberValidator.validateNumberIsCorrect(value) }
    }

    @Test
    fun `fail for non-numeric values`() {
        val value = "This is not a number"

        assertFalse { NumberValidator.validateNumberIsCorrect(value) }
    }
}