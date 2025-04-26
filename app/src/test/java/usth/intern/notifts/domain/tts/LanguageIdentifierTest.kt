package usth.intern.notifts.domain.tts

import org.junit.Assert.assertEquals
import org.junit.Test


class LanguageIdentifierTest {
    private val languageIdentifier = LanguageIdentifier()

    @Test
    fun predict_VietnameseText_ReturnVIETNAMESE() {
        val input = "đây là 1 câu lệnh để thử"
        val language = languageIdentifier.predict(input)
        val expected = "VIETNAMESE"
        assertEquals(language, expected)
    }

    @Test
    fun predict_EnglishText_ReturnENGLISH() {
        val input = "This is a text to test"
        val language = languageIdentifier.predict(input)
        val expected = "ENGLISH"
        assertEquals(language, expected)
    }

    @Test
    fun predict_UnknownLanguageText_ReturnUKNOWN() {
        val input = "これはテストするための文です"
        val language = languageIdentifier.predict(input)
        val expected = "UNKNOWN"
        assertEquals(expected, language)
    }
}