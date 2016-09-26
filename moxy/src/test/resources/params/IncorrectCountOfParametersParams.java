package params;

import com.arellomobile.mvp.ParamsProvider;
import com.arellomobile.mvp.factory.MockPresenterFactory;

/**
 * Date: 24.02.2016
 * Time: 18:17
 *
 * @author Savin Mikhail
 */
@ParamsProvider(MockPresenterFactory.class)
public interface IncorrectCountOfParametersParams {
	void method1(String s1, String s2);
}
