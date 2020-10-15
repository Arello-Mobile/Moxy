package params;

import com.omegar.mvp.ParamsProvider;
import com.omegar.mvp.factory.MockPresenterFactory;

/**
 * Date: 24.02.2016
 * Time: 10:11
 *
 * @author Savin Mikhail
 */
@ParamsProvider(MockPresenterFactory.class)
public interface SeveralMethodParams {
	void method1();

	void method2();
}
