package adapter.`in`.input

class NumberValidator {
    companion object {
        fun validateNumberIsCorrect(value: String): Boolean {
            if (value.length != 9) {
                return false
            }

            return try {
                value.toInt()
                true
            } catch (e: NumberFormatException) {
                false
            }

        }
    }
}