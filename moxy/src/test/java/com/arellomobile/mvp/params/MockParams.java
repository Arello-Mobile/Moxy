package com.arellomobile.mvp.params;

import com.arellomobile.mvp.ParamsProvider;
import com.arellomobile.mvp.factory.MockPresenterFactory;

/**
 * Date: 08.02.2016
 * Time: 17:51
 *
 * @author Savin Mikhail
 */
@ParamsProvider(MockPresenterFactory.class)
public interface MockParams {
	String mockParams(String presenterId);
}
