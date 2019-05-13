package com.arellomobile.mvp.reflector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 07.12.2016
 * Time: 16:39
 *
 * @author Yuri Shmakov
 */
public final class MoxyReflector {

	private static final Collection<ReflectorDelegate> sReflectorDelegates = new HashSet<>();

	public static void registerDelegate(final ReflectorDelegate reflectorDelegate) {
		sReflectorDelegates.add(reflectorDelegate);
	}

	public static Object getViewState(final Class<?> presenterClass) {

	    for (final ReflectorDelegate delegate: sReflectorDelegates) {

	        final Object viewState = delegate.getViewState(presenterClass);

	        if (viewState != null) {

	            return viewState;
            }
        }

		return null;
	}

	public static List<Object> getPresenterBinders(final Class<?> delegated) {
        for (final ReflectorDelegate delegate: sReflectorDelegates) {

            final List<Object> binders = delegate.getPresenterBinders(delegated);

            if (binders != null) {

                return binders;
            }
        }

        return null;
	}

	public static Object getStrategy(final Class strategyClass) {

        for (final ReflectorDelegate delegate: sReflectorDelegates) {

            final Object strategy = delegate.getStrategy(strategyClass);

            if (strategy != null) {

                return strategy;
            }
        }

        return null;
	}
}
