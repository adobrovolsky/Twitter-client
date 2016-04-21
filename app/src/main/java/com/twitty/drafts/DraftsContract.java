package com.twitty.drafts;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.twitty.store.entity.Draft;

import java.util.List;

public interface DraftsContract {

    interface View extends MvpLceView<List<Draft>> {

    }

    abstract class Presenter extends MvpBasePresenter<View> {

        abstract void loadDrafts(boolean pullToRefresh);

        abstract void removeDraft(Draft draft);
    }
}
