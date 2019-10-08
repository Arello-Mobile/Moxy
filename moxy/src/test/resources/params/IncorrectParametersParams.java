package params;

import com.omegar.mvp.ParamsProvider;
import com.omegar.mvp.factory.MockPresenterFactory;

/**
 * Date: 24.02.2016
 * Time: 18:17
 *
 * @author Savin Mikhail
 */
@ParamsProvider(MockPresenterFactory.class)
public interface IncorrectParametersParams {
	void method1(Integer i);
}
