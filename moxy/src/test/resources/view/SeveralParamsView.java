package view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.params.MockParams;
import com.arellomobile.mvp.params.MockParams2;

import params.Params1;
import params.Params2;

/**
 * Date: 24.02.2016
 * Time: 18:52
 *
 * @author Savin Mikhail
 */
public class SeveralParamsView implements MvpView, Params1, Params2 {
	@Override
	public String mockParams1(final String presenterId) {
		return null;
	}

	@Override
	public String mockParams2(final String presenterId) {
		return null;
	}
}
