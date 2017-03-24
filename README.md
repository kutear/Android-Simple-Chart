# Android-Simple-Chart
这是一个简单的图表实现，目前支持柱状图和折线图

模仿蚂蚁聚宝的资产分析页面。样式高度可配

![](./img/p1.png)

![](./img/p2.png)

# 用法
`HistogramView`和`TrendView`都继承至`AbsChartView`，下面是所有的属性

```xml
    <declare-styleable name="AbsChartView">
        <!--X轴字体大小-->
        <attr name="x_size" format="dimension" />
        <attr name="y_size" format="dimension" />
        <!--X轴字体颜色-->
        <attr name="x_color" format="color" />
        <attr name="y_color" format="color" />
        <!--X轴字体与轴的间隔-->
        <attr name="x_space" format="dimension" />
        <attr name="y_space" format="dimension" />
        <!--水平横线数目-->
        <attr name="y_horizontal_line_num" format="integer" />
        <!--水平横线颜色-->
        <attr name="y_horizontal_line_color" format="color" />
        <!--坐标轴颜色-->
        <attr name="axis_color" format="color" />
        <!--坐标轴轴线宽度-->
        <attr name="axis_width" format="dimension" />
        <attr name="axis_space" format="dimension" />
        <!--竖直指示线颜色-->
        <attr name="line_color" format="color" />
        <!--上方浮动指示字体颜色-->
        <attr name="tips_color" format="color" />
        <!--上方浮动指示字体大小-->
        <attr name="tips_size" format="dimension" />
        <!--上方浮动指示字体左右间距-->
        <attr name="tips_padding" format="dimension" />
        <!--上方浮动指示方块背景-->
        <attr name="tips_background" format="color" />
        <!--上方浮动指示方块圆角-->
        <attr name="tips_radio" format="dimension" />
        <!--0基准线宽度-->
        <attr name="zero_line_size" format="dimension" />
        <!--0基准线颜色-->
        <attr name="zero_line_color" format="color" />
        <!--大于0柱子/折线顶端drawable-->
        <attr name="tap_up_bg" format="reference" />
        <!--小于0柱子/折线顶端drawable-->
        <attr name="tap_down_bg" format="reference" />
    </declare-styleable>

    <declare-styleable name="HistogramView">
        <!--大于0时柱状图渐变颜色起点-->
        <attr name="histogram_positive_top_color" format="color" />
        <!--大于0时柱状图渐变颜色终点-->
        <attr name="histogram_positive_bottom_color" format="color" />
        <!--小于0时柱状图渐变颜色起点-->
        <attr name="histogram_negative_top_color" format="color" />
        <!--小于0时柱状图渐变颜色终点-->
        <attr name="histogram_negative_bottom_color" format="color" />
        <!--以下属性为选择时的柱状颜色，同上-->
        <attr name="histogram_select_positive_top_color" format="color"/>
        <attr name="histogram_select_positive_bottom_color" format="color"/>
        <attr name="histogram_select_negative_top_color" format="color"/>
        <attr name="histogram_select_negative_bottom_color" format="color"/>
        <!--(柱子宽度)/(柱子宽度+柱子间空格宽度) 柱子所占比例-->
        <attr name="histogram_width_percent" format="float" />
    </declare-styleable>

    <declare-styleable name="TrendView">
        <!--折线图颜色渐变起点-->
        <attr name="trend_line_start_color" format="color" />
        <!--折线图颜色渐变终点-->
        <attr name="trend_line_end_color" format="color" />
        <!--线宽-->
        <attr name="trend_line_size" format="dimension" />
        <!--填充渐变颜色起点-->
        <attr name="trend_fill_start_color" format="dimension" />
        <!--填充渐变颜色终点-->
        <attr name="trend_fill_end_color" format="dimension" />
    </declare-styleable>
```

提供数据格式化接口
```java
    /**
     * X轴数据展示
     */
    interface IFormatAxis {
        /**
         * 格式化X的展示
         *
         * @param xAxis
         * @return
         */
        String formatX(String xAxis, int index);

        /**
         * 格式化X的展示
         *
         * @param yAxis
         * @return
         */
        String formatY(float yAxis);
    }

    /**
     * Y轴范围
     */
    interface IMaxMin {
        float getMax(float max, float min);

        float getMin(float max, float min);
    }

    /**
     * 上方的浮动指示器
     */
    interface ITipShow {
        /**
         * 格式化上方指示器展示的内容
         *
         * @param position
         * @param x
         * @param y
         * @return
         */
        String getTips(int position, String x, float y);
    }

    /**
     * 点击事件
     */
    interface OnTapListener {
        /**
         * 点击
         *
         * @param position
         * @param value
         */
        void onTapEvent(int position, float value);

        /**
         * 点击取消
         */
        void onTapCancel();
    }

```
对应提供的方法只有这几个
```java
    /**
     * 格式化Xy轴数据
     *
     * @param formatAxis
     */
    public void setFormatAxis(IChartContract.IFormatAxis formatAxis) {
        //...
    }

    /**
     * 设置监听事件
     * @param listener
     */
    public void setOnTapListener(IChartContract.OnTapListener listener) {
        //...
    }

    /**
     * 控制Y轴最大值与最小值
     *
     * @param maxMin
     */
    public void setMaxMin(IChartContract.IMaxMin maxMin) {
        //...
    }

    /**
     * 上方指示内容
     *
     * @param show
     */
    public void setTipsShow(IChartContract.ITipShow show) {
        //...
    }
```
