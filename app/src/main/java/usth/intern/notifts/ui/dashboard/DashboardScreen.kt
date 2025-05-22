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

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
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
                        pointProvider =
                        LineCartesianLayer.PointProvider.single(
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
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = BottomAxisValueFormatter
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
                series(countMap.values)
                extras { it[BottomAxisLabelKey] = countMap.keys.toList() }
            }
        }
    }
    LineChartHost(modelProducer, modifier)
}

private val data = mapOf<String,Number>(
    "2009" to -0,
    "2012" to -44.16,
    "2014" to -6.8,
    "2015" to 0.69,
    "2016" to 6.62,
    "2018" to 11.69,
    "2019" to 9.52,
    "2020" to 16.45,
)

@Composable
@Preview
private fun Preview() {

    val modelProducer = remember { CartesianChartModelProducer() }

    runBlocking {
        modelProducer.runTransaction {
            lineSeries {
                series(data.values)
                extras { it[BottomAxisLabelKey] = data.keys.toList()}
            }
        }
    }
    LineChartHost(modelProducer)
}
