package example.com.moxy_androidx_sample.contract

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType
import example.com.moxy_androidx_sample.fifth.Contract
import example.com.moxy_androidx_sample.first.FirstView
import example.com.moxy_androidx_sample.packagee.Item
import example.com.moxy_androidx_sample.second.SecondView

interface Contract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface MainView : FirstView<Item>, SecondView, Contract.FifthView {

        fun printLog(msg: Double?, log: String?)

    }

}