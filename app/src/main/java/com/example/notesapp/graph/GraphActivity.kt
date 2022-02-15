package com.example.notesapp.graph

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R
import com.jjoe64.graphview.series.DataPoint

import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_graph.*


class GraphActivity : AppCompatActivity() {

    companion object {
        const val GRAPH_ARRAY = "GRAPH_ARRAY"
        fun getInstance(context: Context, msg: ArrayList<Int>): Intent {
            val intent = Intent(context, GraphActivity::class.java)
            intent.putIntegerArrayListExtra(GraphActivity.GRAPH_ARRAY, msg)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        val st = intent.getIntegerArrayListExtra(GraphActivity.GRAPH_ARRAY)

        var pointArray = st?.let { Array<DataPoint>(it.size+1){DataPoint(0.0,0.0)} }
        if (st != null) {
            var xPoint = 1.0
            var index = 1
            for(i in st){
                pointArray?.set(index, DataPoint(xPoint,i.toDouble()/(1024.0*1024.0)))
                index++
                xPoint += 0.5
            }
        }

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
//            arrayOf( // on below line we are adding
//                // each point on our x and y axis.
//                DataPoint(0.0, 1.0),
//                DataPoint(1.0, 3.0),
//                DataPoint(2.0, 4.0),
//                DataPoint(3.0, 9.0),
//                DataPoint(4.0, 6.0),
//                DataPoint(5.0, 3.0),
//                DataPoint(6.0, 6.0),
//                DataPoint(7.0, 1.0),
//                DataPoint(8.0, 2.0),
//                DataPoint(10.0, 1.0),
//                DataPoint(11.0, 3.0),
//                DataPoint(12.0, 4.0),
//                DataPoint(13.0, 9.0),
//                DataPoint(14.0, 6.0),
//                DataPoint(15.0, 3.0),
//                DataPoint(16.0, 6.0),
//                DataPoint(27.0, 1.0),
//                DataPoint(28.0, 2.0)
//            )
        pointArray
        )

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        graphView.setTitle("RAM Used")

        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.purple_200)

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(25F)

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series)
    }
}