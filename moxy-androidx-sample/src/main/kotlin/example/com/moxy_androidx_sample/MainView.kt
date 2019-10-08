package example.com.moxy_androidx_sample

import example.com.moxy_androidx_sample.fifth.FifthView
import example.com.moxy_androidx_sample.first.FirstView
import example.com.moxy_androidx_sample.packagee.Item
import example.com.moxy_androidx_sample.second.SecondView

interface MainView : FirstView<Item>, SecondView, FifthView {

    fun printLog(msg: Double?, log: String?)

}