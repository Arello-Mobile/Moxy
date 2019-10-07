package example.com.moxy_androidx_sample

import example.com.moxy_androidx_sample.first.FirstView
import example.com.moxy_androidx_sample.packagee.Item
import example.com.moxy_androidx_sample.second.SecondView
import example.com.moxy_androidx_sample.third.ThirdView
import java.io.Serializable

interface MainView : FirstView<Item>, SecondView, ThirdView, Serializable {

    fun printLog(msg: Double?)

}