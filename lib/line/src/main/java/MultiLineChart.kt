import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.LineChartUtils
import com.github.tehras.charts.line.LineChartUtils.calculateYAxisDrawableArea
import com.github.tehras.charts.line.renderer.line.LineDrawer
import com.github.tehras.charts.line.renderer.line.LineShader
import com.github.tehras.charts.line.renderer.line.NoLineShader
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.point.PointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.xaxis.XAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.YAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@Composable
fun MultiLineChart(
    lineChartData: List<LineChartData>,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    pointDrawer: PointDrawer = FilledCircularPointDrawer(),
    lineDrawer: LineDrawer = SolidLineDrawer(),
    lineShader: LineShader = NoLineShader,
    xAxisDrawer: XAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = SimpleYAxisDrawer(),
    horizontalOffset: Float = 5f
) {
    check(horizontalOffset in 0f..25f) {
        "Horizontal offset is the % offset from sides, " +
                "and should be between 0%-25%"
    }

    val animationState = remember {
        Array(lineChartData.size) {
            Animatable(initialValue = 0f)
        }
    }


    lineChartData.forEachIndexed { index, lineData ->
        LaunchedEffect(lineData.points) {
            animationState[index].snapTo(0f)
            animationState[index].animateTo(1f, animationSpec = animation)
        }
    }

    /* val transitionAnimation = remember(lineChartData.points) { Animatable(initialValue = 0f) }

     LaunchedEffect(lineChartData.points) {
         transitionAnimation.snapTo(0f)
         transitionAnimation.animateTo(1f, animationSpec = animation)
     }
 */
    Canvas(modifier = modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->

            val yAxisDrawableArea = calculateYAxisDrawableArea(
                xAxisLabelSize = xAxisDrawer.requiredHeight(this),
                size = size
            )

            val xAxisDrawableArea = LineChartUtils.calculateXAxisDrawableArea(
                yAxisWidth = yAxisDrawableArea.width,
                labelHeight = xAxisDrawer.requiredHeight(this),
                size = size
            )

            val xAxisLabelsDrawableArea = LineChartUtils.calculateXAxisLabelsDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                offset = horizontalOffset
            )

            val chartDrawableArea = LineChartUtils.calculateDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                yAxisDrawableArea = yAxisDrawableArea,
                size = size,
                offset = horizontalOffset
            )


            lineChartData.forEachIndexed { index, lineData ->

                // Draw the chart line.
                lineDrawer.drawLine(
                    drawScope = this,
                    canvas = canvas,
                    linePath = LineChartUtils.calculateLinePath(
                        drawableArea = chartDrawableArea,
                        lineChartData = lineData,
                        transitionProgress = animationState[index].value
                    )
                )


                lineShader.fillLine(
                    drawScope = this,
                    canvas = canvas,
                    fillPath = LineChartUtils.calculateFillPath(
                        drawableArea = chartDrawableArea,
                        lineChartData = lineData,
                        transitionProgress = animationState[index].value
                    )
                )

                lineData.points.forEachIndexed { pointIndex, point ->
                    LineChartUtils.withProgress(
                        index = index,
                        lineChartData = lineData,
                        transitionProgress = animationState[index].value
                    ) {
                        pointDrawer.drawPoint(
                            drawScope = this,
                            canvas = canvas,
                            center = LineChartUtils.calculatePointLocation(
                                drawableArea = chartDrawableArea,
                                lineChartData = lineData,
                                point = point,
                                index = pointIndex
                            )
                        )
                    }
                }


            }


        }
    }
}


/*private fun draw(canvas: Canvas, data: LineChartData) {


    // Draw the X Axis line.
    xAxisDrawer.drawAxisLine(
        drawScope = this,
        drawableArea = xAxisDrawableArea,
        canvas = canvas
    )

    xAxisDrawer.drawAxisLabels(
        drawScope = this,
        canvas = canvas,
        drawableArea = xAxisLabelsDrawableArea,
        labels = lineChartData.points.map { it.label }
    )

    // Draw the Y Axis line.
    yAxisDrawer.drawAxisLine(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableArea
    )

    yAxisDrawer.drawAxisLabels(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableArea,
        minValue = lineChartData.minYValue,
        maxValue = lineChartData.maxYValue
    )
}*/


