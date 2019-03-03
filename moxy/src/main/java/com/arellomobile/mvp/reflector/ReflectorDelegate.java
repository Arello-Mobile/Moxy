package com.arellomobile.mvp.reflector;

import java.util.List;

public interface ReflectorDelegate {
    Object getViewState(Class<?> presenterClass);
    List<Object> getPresenterBinders(Class<?> delegated);
    Object getStrategy(Class<?> strategyClass);
}
