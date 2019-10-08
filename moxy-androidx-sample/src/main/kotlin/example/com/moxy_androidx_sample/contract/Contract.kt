package example.com.moxy_androidx_sample.contract

import example.com.moxy_androidx_sample.fifth.Contract
import example.com.moxy_androidx_sample.first.FirstView
import example.com.moxy_androidx_sample.packagee.Item
import example.com.moxy_androidx_sample.second.SecondView

interface Contract {

    interface MainView : FirstView<Item>, SecondView, Contract.FifthView {

        fun printLog(msg: Double?, log: String?)

    }

}