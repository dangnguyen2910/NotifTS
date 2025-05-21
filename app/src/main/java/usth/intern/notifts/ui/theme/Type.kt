package usth.intern.notifts.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import usth.intern.notifts.R

private val dmSans = FontFamily(
    Font(R.font.dmsansregular),
    Font(R.font.dmsansmedium, FontWeight.W500),
    Font(R.font.dmsansbold, FontWeight.Bold)
)


val typography = Typography(bodyLarge = TextStyle(fontFamily = dmSans))
