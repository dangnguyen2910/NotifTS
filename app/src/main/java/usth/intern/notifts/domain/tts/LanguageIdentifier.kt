package usth.intern.notifts.domain.tts

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import javax.inject.Inject

class LanguageIdentifier @Inject constructor() {
    /**
     * Model for identify language
     */
    private val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(
        Language.ENGLISH,
        Language.VIETNAMESE,
        Language.FRENCH
    ).build()

    fun predict(text: String) : String {
        val detectedLanguageOfText: Language = detector.detectLanguageOf(text)
        return detectedLanguageOfText.name
    }
}