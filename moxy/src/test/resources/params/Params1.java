package params;

import com.omegar.mvp.ParamsProvider;
import com.omegar.mvp.factory.MockPresenterFactory;

/**
 * Date: 25.02.2016
 * Time: 9:21
 *
 * @author Savin Mikhail
 */
@ParamsProvider(MockPresenterFactory.class)
public interface Params1 {
	String mockParams1(String presenterId);
}
