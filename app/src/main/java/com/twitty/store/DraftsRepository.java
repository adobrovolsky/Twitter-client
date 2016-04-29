package com.twitty.store;

import com.twitty.store.DatabaseContract.DraftTable;
import com.twitty.store.entity.Draft;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DraftsRepository extends GenericRepository<Draft, Integer>{

    public interface DraftsRepositoryObserver {
        void onDraftsChanged();
    }

    private List<DraftsRepositoryObserver> observers;

    public DraftsRepository(DatabaseHelper dbHelper) {
        super(DraftTable.TABLE_NAME, dbHelper);
        this.observers = new ArrayList<>();
    }

    public void addObserver(DraftsRepositoryObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(DraftsRepositoryObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    private void notifyObservers() {
        for (DraftsRepositoryObserver observer : observers) {
            observer.onDraftsChanged();
        }
    }

    @Override public long save(Draft entity) {
        notifyObservers();
        return super.save(entity);

    }

    @Override public void delete(Draft entity) {
        super.delete(entity);
        notifyObservers();
    }

    @Override public void refresh() {
        notifyObservers();
    }

    @Override protected Draft transformCursorToModel(final Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DraftTable._ID));
        String text = cursor.getString(cursor.getColumnIndex(DraftTable.COLUMN_TEXT));
        long date = cursor.getLong(cursor.getColumnIndex(DraftTable.COLUMN_CREATED));

        return new Draft(id, text, new Date(date));
    }

    @Override protected ContentValues transformModelToContentValues(final Draft draft) {
        ContentValues cv = new ContentValues();

        cv.put(DraftTable._ID, draft.getId());
        cv.put(DraftTable.COLUMN_TEXT, draft.getText());
        cv.put(DraftTable.COLUMN_CREATED, draft.getCreationDate().getTime());

        return cv;
    }
}
