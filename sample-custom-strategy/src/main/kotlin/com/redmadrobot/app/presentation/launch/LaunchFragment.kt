package com.redmadrobot.app.presentation.launch;


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.redmadrobot.sample_custom_strategy.R
import kotlinx.android.synthetic.main.fragment_launch.*


class LaunchFragment : MvpAppCompatFragment(), LaunchView {

    companion object {

        val TAG = "LaunchFragment"
        private const val animationDuration: Long = 500

        private const val DEFAULT_COLOR = Color.TRANSPARENT
        private const val CHANGES_COLOR = Color.GREEN
        private const val CLEAR_COLOR = Color.RED

        fun newInstance(): LaunchFragment {
            val fragment = LaunchFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }

    }

    @InjectPresenter
    internal lateinit var presenter: LaunchPresenter


    @ProvidePresenter
    fun providePresenter(): LaunchPresenter {
        return LaunchPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        fragment_launch_button_toggle_bread_type.setOnClickListener { presenter.onToggleBreadClicked() }
        fragment_launch_button_toggle_cheese.setOnClickListener { presenter.onToggleCheeseClicked() }
        fragment_launch_button_burger_clear.setOnClickListener { presenter.onClearBurgerClicked() }
        fragment_launch_button_toggle_lemon.setOnClickListener { presenter.onToggleLemonClicked() }
        fragment_launch_button_apply_sugar.setOnClickListener {
            val count = try {
                fragment_launch_edit_text_sugar.text.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }
            presenter.onApplySugarClicked(count)
        }
        fragment_launch_button_beverage_clear.setOnClickListener { presenter.onClearBeverageClicked() }

        fragment_launch_button_make_order.setOnClickListener { presenter.onMakeOrderClicked() }
    }


    override fun setBreadType(breadType: BreadType) {
        setBreadTypeInternal(breadType)
        startBlinkAnimation(fragment_launch_text_view_burger_bread, DEFAULT_COLOR, CHANGES_COLOR)
        Log.d(TAG, "setBreadType: $breadType")
    }

    override fun toggleCheese(enable: Boolean) {
        toggleCheeseInternal(enable)
        startBlinkAnimation(fragment_launch_text_view_burger_cheese, DEFAULT_COLOR, CHANGES_COLOR)
        Log.d(TAG, "toggleCheese: $enable")
    }

    override fun setSugarCount(count: Int) {
        setSugarCountInternal(count)
        startBlinkAnimation(fragment_launch_text_view_beverage_sugar, DEFAULT_COLOR, CHANGES_COLOR)
        Log.d(TAG, "setSugarCount: $count")
    }

    override fun toggleLemon(enable: Boolean) {
        toggleLemonInternal(enable)
        startBlinkAnimation(fragment_launch_text_view_beverage_lemon, DEFAULT_COLOR, CHANGES_COLOR)
        Log.d(TAG, "toggleLemon: $enable")
    }

    override fun clearBurger(breadType: BreadType, cheeseSelected: Boolean) {
        setBreadTypeInternal(breadType)
        toggleCheeseInternal(cheeseSelected)
        startBlinkAnimation(fragment_launch_container_result_burger, DEFAULT_COLOR, CLEAR_COLOR)
        Log.d(TAG, "clearBurger: $breadType $cheeseSelected")
    }

    override fun clearBeverage(sugarCount: Int, lemonSelected: Boolean) {
        toggleLemonInternal(lemonSelected)
        setSugarCountInternal(sugarCount)
        startBlinkAnimation(fragment_launch_container_result_beverage, DEFAULT_COLOR, CLEAR_COLOR)
        Log.d(TAG, "clearBeverage: $sugarCount $lemonSelected")
    }

    private fun setBreadTypeInternal(breadType: BreadType) {
        fragment_launch_text_view_burger_bread.text = getString(R.string.bread_regex, breadType.toString())
    }

    private fun toggleCheeseInternal(enable: Boolean) {
        fragment_launch_text_view_burger_cheese.text = getString(R.string.cheese_regex, enable.toString())
    }

    private fun setSugarCountInternal(count: Int) {
        fragment_launch_text_view_beverage_sugar.text = getString(R.string.sugar_regex, count)
    }


    private fun toggleLemonInternal(enable: Boolean) {
        fragment_launch_text_view_beverage_lemon.text = getString(R.string.lemon_regex, enable.toString())
    }


    override fun showOrderMade() {
        Snackbar.make(fragment_launch_container, R.string.order_made, Snackbar.LENGTH_SHORT).show()

        Log.d(TAG, "showOrderMade")
    }

    private fun startBlinkAnimation(view: View, startColor: Int, endColor: Int) {

        (view.tag as? ValueAnimator)?.cancel()

        val animation = ValueAnimator.ofArgb(startColor, endColor)
        animation.duration = animationDuration
        animation.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
        }

        val reverseAnimation = ValueAnimator.ofArgb(endColor, startColor)
        reverseAnimation.duration = animationDuration
        reverseAnimation.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
        }


        animation.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                reverseAnimation.start()
                view.tag = reverseAnimation
            }
        })
        animation.start()
        view.tag = animation
    }
}




