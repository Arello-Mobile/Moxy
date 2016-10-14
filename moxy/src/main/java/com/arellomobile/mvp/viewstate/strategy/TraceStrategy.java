package com.arellomobile.mvp.viewstate.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

import java.lang.reflect.Field;
import java.util.List;

/**
Forces contract: calling method should have  parameter<? extends Object> which is called "id"

@author Alex Shafir
*/
public class TraceStrategy implements StateStrategy {
    private static final String ID_FIELD = "id";

    @Override
    public <V extends MvpView> void beforeApply(List<ViewCommand<V>> list, ViewCommand<V> viewCommand) {
        String newName = viewCommand.getTag() + getId(viewCommand);
        FacadeViewCommand command = new FacadeViewCommand(
                newName,
                SkipStrategy.class, // not really relevant as this command will never be called by user
                viewCommand);

        list.add(command);
    }

    static String getId(Object object) {
        String result = null;
        try {
            Class clazz = object.getClass();
            Field idField = clazz.getDeclaredField(ID_FIELD);
            Object value = idField.get(object);
            result = value.toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public <V extends MvpView> void afterApply(List<ViewCommand<V>> list, ViewCommand<V> viewCommand) {
        // pass
    }

    private static class FacadeViewCommand extends ViewCommand {
        private ViewCommand toWrap;

        FacadeViewCommand(String tag, Class<? extends StateStrategy> stateStrategyType, ViewCommand toWrap) {
            super(tag, stateStrategyType);
            this.toWrap = toWrap;
        }

        @Override
        public void apply(MvpView view) {
            toWrap.apply(view);
        }
    }
}
