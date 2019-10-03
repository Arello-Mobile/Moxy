package example.com.moxy_androidx_sapmle

import example.com.moxy_androidx_sapmle.first.FirstView
import example.com.moxy_androidx_sapmle.packagee.Item
import java.io.Serializable

interface MainView : FirstView<Item>, Serializable {

    fun printLog(msg: Double?)

}