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
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-y")
val data =
    mapOf(
        LocalDate.parse("01-07-2022", dateFormatter) to 2f,
        LocalDate.parse("02-07-2022", dateFormatter) to 6f,
        LocalDate.parse("04-07-2022", dateFormatter) to 4f,
        LocalDate.parse("07-07-2022", dateFormatter) to 4f,
    )

val xToDateMapKey: ExtraStore.Key<Map<Float, LocalDate>> = ExtraStore.Key()
val xToDates = data.keys.associateBy { it.toEpochDay().toFloat() }

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM")

val horizontalLabelFormatter = CartesianValueFormatter { _, x, _ ->
    (LocalDate.ofEpochDay(x.toLong()))
        .format(dateTimeFormatter)
}

@Composable
private fun LineChartHost(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        pointProvider = LineCartesianLayer.PointProvider.single(
                            LineCartesianLayer.point(
                                rememberShapeComponent(
                                    fill(Color(0xFF2196F3)),
                                    CorneredShape.Pill
                                )
                            )
                        )
                    )
                )
            ),
            // TODO: Need to change this if the app ever needs a dark theme.
            startAxis = VerticalAxis.rememberStart(
                label = rememberAxisLabelComponent(Color.Black)
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = rememberAxisLabelComponent(Color.Black),
                valueFormatter = horizontalLabelFormatter,
            ),
            marker = rememberDefaultCartesianMarker(label = TextComponent())
        ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun JetpackComposeBasicLineChart(
    countMap: Map<LocalDate, Number>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val floatToDate = countMap.keys.associateBy { it.toEpochDay().toFloat() }

    if (countMap.isNotEmpty()) {
        LaunchedEffect(Unit) {
            modelProducer.runTransaction {
                lineSeries {
                    series(floatToDate.keys, countMap.values)
                    extras { it[xToDateMapKey] = xToDates }
                }
            }
        }
        LineChartHost(modelProducer, modifier)
    }
}



@Composable
@Preview
private fun Preview() {
    val modelProducer = remember { CartesianChartModelProducer() }

    runBlocking {
        modelProducer.runTransaction {
            lineSeries { series(xToDates.keys, data.values) }
            extras { it[xToDateMapKey] = xToDates }
        }
    }
    LineChartHost(modelProducer)
}
