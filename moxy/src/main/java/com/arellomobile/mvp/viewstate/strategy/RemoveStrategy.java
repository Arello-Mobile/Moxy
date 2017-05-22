package com.arellomobile.mvp.viewstate.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;

import java.util.Iterator;
import java.util.List;

/**
Searches for ViewCommand with tag specified in @StateStrategy + id, then removes it.
Also does not put itself on command stack.
Enforces contract: calling method should have  parameter<? extends Object> which is called "id".

@author Alex Shafir
*/
public class RemoveStrategy implements StateStrategy {

    @Override
    public <V extends MvpView> void beforeApply(List<ViewCommand<V>> list, ViewCommand<V> viewCommand) {
        String search = viewCommand.getTag() + getId(viewCommand);
        Iterator<ViewCommand<V>> iterator = list.iterator();

        while (iterator.hasNext()) {
            ViewCommand<V> entry = iterator.next();

            if (entry.getTag().equals(search)) {
                iterator.remove();
                break;
            }
        }

        // skip addition
    }

    @Override
    public <V extends MvpView> void afterApply(List<ViewCommand<V>> list, ViewCommand<V> viewCommand) {

    }

    protected String getId(ViewCommand viewCommand) {
        return TraceStrategy.getId(viewCommand);
    }
    
    /**
    For cases when there is only one command to remove
    
    Does not enforce any contract on calling method
    */
    public static class NoId extends RemoveStrategy {
        @Override
        protected String getId(ViewCommand viewCommand) {
            return "";
        }
    }
}
