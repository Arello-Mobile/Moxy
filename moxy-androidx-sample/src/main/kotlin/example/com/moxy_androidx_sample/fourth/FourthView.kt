package example.com.moxy_androidx_sample.fourth

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.omegar.mvp.viewstate.strategy.StateStrategyType
import example.com.moxy_androidx_sample.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface FourthView<R> : BaseView {

    fun fourth(item: R)

}