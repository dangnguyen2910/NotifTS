package usth.intern.notifts.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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

val data =
    mapOf(
        LocalDate.parse("2022-07-01") to 2f,
        LocalDate.parse("2022-07-02") to 6f,
        LocalDate.parse("2022-07-10") to 4f,
    )

val xToDateMapKey: ExtraStore.Key<Map<Float, LocalDate>> = ExtraStore.Key()
val xToDates = data.keys.associateBy { it.toEpochDay().toFloat() }

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM")

val horizontalLabelFormatter = CartesianValueFormatter { _, x, _ ->
    (LocalDate.ofEpochDay(x.toLong()))
        .format(dateTimeFormatter)
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>()
    val uiState by dashboardViewModel.uiState.collectAsState()

    Column(modifier = modifier) {
        Button(onClick = {dashboardViewModel.getNotificationCountByDay()}) {
            Text("Refresh")
        }
        if (uiState.notificationCountByDate.isNotEmpty()) {
            JetpackComposeBasicLineChart(
                countMap = uiState.notificationCountByDate,
                updateNotificationCount = {}
            )
        }
    }
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
                valueFormatter = horizontalLabelFormatter
            ),
            marker = rememberDefaultCartesianMarker(label = TextComponent())
        ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun JetpackComposeBasicLineChart(
    countMap: Map<String, Number>,
    updateNotificationCount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    updateNotificationCount()

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(xToDates.keys, countMap.values)
                extras { it[xToDateMapKey] = xToDates }
            }
        }
    }
    LineChartHost(modelProducer, modifier)
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
