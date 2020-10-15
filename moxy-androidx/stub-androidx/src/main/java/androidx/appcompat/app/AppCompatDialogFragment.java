package androidx.appcompat.app;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.DialogFragment;

/**
 * Date: 22-March-19
 * Time: 09:18
 *
 * @author R12rus
 */
public class AppCompatDialogFragment extends DialogFragment {
    public AppCompatDialogFragment() { super(); }

    @ContentView
    public AppCompatDialogFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }
}