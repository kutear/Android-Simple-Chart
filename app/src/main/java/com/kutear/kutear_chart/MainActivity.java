package com.kutear.kutear_chart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kutear.kutear_chart.view.AbsChartView;
import com.kutear.kutear_chart.view.HistogramView;
import com.kutear.kutear_chart.view.IChartContract;
import com.kutear.kutear_chart.view.TrendView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ArrayList<IChartContract.ChartSingleData> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datas = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            int randomInt = random.nextInt(100);
            datas.add(new IChartContract.ChartSingleData(String.valueOf(i), randomInt));
        }
        datas.add(new IChartContract.ChartSingleData(String.valueOf(13), -20));
        HistogramView view = (HistogramView) findViewById(R.id.chart);
        view.setData(datas);
        TrendView trendView = (TrendView) findViewById(R.id.trend);
        trendView.setData(datas);
        init(view);
        init(trendView);
        view.setDefaultSelected(datas.size() - 1);
        trendView.setDefaultSelected(datas.size() - 1);
    }

    private void init(AbsChartView view) {
        view.setFormatAxis(new IChartContract.IFormatAxis() {
            @Override
            public String formatX(String xAxis, int i) {
                return xAxis;
            }

            @Override
            public String formatY(float yAxis) {
                return String.valueOf(yAxis);
            }
        });

        view.setOnTapListener(new IChartContract.OnTapListener() {
            @Override
            public void onTapEvent(int position, float value) {
                Log.v(TAG, "onTapEvent:  " + position + "  " + value);
            }

            @Override
            public void onTapCancel() {

            }
        });

        view.setMaxMin(new IChartContract.IMaxMin() {
            @Override
            public float getMax(float max, float min) {
                return max + (max - min) / 6;
            }

            @Override
            public float getMin(float max, float min) {
                return min - (max - min) / 6;
            }
        });

        view.setTipsShow(new IChartContract.ITipShow() {
            @Override
            public String getTips(int position, String x, float y) {
                return "Y:" + y;
            }
        });
    }

    public void reload(View v) {
        recreate();
    }
}
