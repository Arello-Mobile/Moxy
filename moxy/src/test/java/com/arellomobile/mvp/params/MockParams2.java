package com.arellomobile.mvp.params;

import com.arellomobile.mvp.ParamsProvider;
import com.arellomobile.mvp.factory.MockPresenterFactory;

/**
 * Date: 24.02.2016
 * Time: 18:54
 *
 * @author Savin Mikhail
 */
@ParamsProvider(MockPresenterFactory.class)
public interface MockParams2 {
	String mockParams2(String presenterId);
}
