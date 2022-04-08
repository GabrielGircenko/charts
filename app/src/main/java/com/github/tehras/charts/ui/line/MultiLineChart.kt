package com.github.tehras.charts.ui.line

import com.github.tehras.charts.line.MultiLineChart
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.ui.ChartScreenStatus

@Composable
fun MultiLineChartScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { ChartScreenStatus.navigateHome() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back to home")
                    }
                },
                title = { Text(text = "Multi Line Chart") }
            )
        },
    ) {

        val lineChartDataModel1 = LineChartDataModel()
        val lineChartDataModel2 = LineChartDataModel()


        Box(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        ) {
            MultiLineChart(
                lineChartData = arrayListOf(
                    lineChartDataModel1.lineChartData,
                    lineChartDataModel2.lineChartData
                ),
                horizontalOffset = lineChartDataModel1.horizontalOffset,
                pointDrawer = lineChartDataModel1.pointDrawer
            )
        }


    }
}

@Preview
@Composable
fun MultiLineChartPreview() = MultiLineChartScreen()