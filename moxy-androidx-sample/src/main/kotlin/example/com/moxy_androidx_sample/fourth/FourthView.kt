package example.com.moxy_androidx_sample.fourth

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType
import example.com.moxy_androidx_sample.BaseView

interface FourthView<R> : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun fourth(item: R)

}