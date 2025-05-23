package usth.intern.notifts.ui.dashboard.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import kotlinx.coroutines.runBlocking

private val sample = mapOf(
    "Gmail" to 10,
    "Facebook" to 9,
    "LinkedIn" to 5
)

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
private fun JetpackComposeBasicColumnChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                label = rememberAxisLabelComponent(Color.Black),
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = rememberAxisLabelComponent(Color.Black),
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter
            ),
            marker = rememberDefaultCartesianMarker(label = TextComponent())
        ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun JetpackComposeBasicColumnChart(
    countByAppMap: Map<String, Number>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    if (countByAppMap.isNotEmpty()) {
        LaunchedEffect(Unit) {
            modelProducer.runTransaction {
                columnSeries {
                    series(countByAppMap.values)
                    extras {  it[BottomAxisLabelKey] = countByAppMap.keys.toList() }
                }
            }
        }

        JetpackComposeBasicColumnChart(modelProducer, modifier)
    }
}

@Composable
@Preview
private fun Preview() {
    val modelProducer = remember { CartesianChartModelProducer() }
    runBlocking {
        modelProducer.runTransaction {
            columnSeries {
                series(sample.values)
                extras {  it[BottomAxisLabelKey] = sample.keys.toList() }
            }
        }
    }
    JetpackComposeBasicColumnChart(modelProducer)
}

