package example.com.moxy_androidx_sample;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import com.omegar.mvp.MvpAppCompatActivity;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    public BaseActivity() {
        super();
    }

    @ContentView
    public BaseActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void testFunction() {

    }

}