package androidx.appcompat.app;

import android.os.Bundle;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentActivity;


/**
 * Date: 25-July-18
 * Time: 2:51
 *
 * @author Vova Stelmashchuk
 */

public class AppCompatActivity extends FragmentActivity {

    public AppCompatActivity() {
        super();
    }

    @ContentView
    public AppCompatActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    protected void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    protected void onStart() {
        throw new RuntimeException("Stub!");
    }

    protected void onResume() {
        throw new RuntimeException("Stub!");
    }

    protected void onSaveInstanceState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    protected void onStop() {
        throw new RuntimeException("Stub!");
    }

    protected void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    public boolean isFinishing() {
        throw new RuntimeException("Stub!");
    }
}
