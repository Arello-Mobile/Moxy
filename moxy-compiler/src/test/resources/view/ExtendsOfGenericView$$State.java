package view;

import com.arellomobile.mvp.MvpView;

import java.io.Serializable;

public interface ExtendsOfGenericView extends GenericWithExtendsView<Serializable> {

    @Override
    public void testEvent(Serializable param) {
        ExtendsOfGenericView$$State.TestEventCommand testEventCommand = new ExtendsOfGenericView$$State.TestEventCommand(param);
        mViewCommands.beforeApply(testEventCommand);

        if (mViews == null || mViews.isEmpty()) {
            return;
        }

        for (ExtendsOfGenericView view : mViews) {
            view.testEvent(param);
        }

        mViewCommands.afterApply(testEventCommand);
    }


    public class TestEventCommand extends ViewCommand<ExtendsOfGenericView> {
        public final Serializable param;

        TestEventCommand(Serializable param) {
            super("testEvent", AddToEndStrategy.class);
            this.param = param;
        }

        @Override
        public void apply(ExtendsOfGenericView mvpView) {
            mvpView.testEvent(param);
        }
    }

}
