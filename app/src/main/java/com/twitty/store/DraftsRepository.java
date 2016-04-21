package com.twitty.store;

import com.twitty.BuildConfig;
import com.twitty.store.DatabaseContract.DraftTable;
import com.twitty.store.entity.Draft;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DraftsRepository implements CrudRepository<Draft, Integer> {

    private static final String TAG = DraftsRepository.class.getSimpleName();

    public interface DraftsRepositoryObserver {
        void onDraftsChanged();
    }

    private DatabaseHelper dbHelper;
    private List<DraftsRepositoryObserver> observers;

    public DraftsRepository() {
        dbHelper = new DatabaseHelper();
        observers = new ArrayList<>();
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

    @Override public Integer save(Draft entity) {
        ContentValues cv = transformModelToContentValues(entity);

        int id  = (int) dbHelper.getWritableDatabase()
                .insert(DraftTable.TABLE_NAME, null, cv);

        if (BuildConfig.DEBUG) {
            entity.setId(id);
            Log.d(TAG, "Saved: " + entity.toString());
        }

        notifyObservers();
        return id;

    }

    @Override public Draft findOne(Integer id) {
        Draft draft = null;
        final String where = DraftTable._ID + " = ?";
        final String[] args = { String.valueOf(id) };

        Cursor cursor = dbHelper.getReadableDatabase()
                .query(DraftTable.TABLE_NAME, null, where, args, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            draft = transformCursorToModel(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return draft;
    }

    @Override public void update(Draft entity) {

    }

    @Override public void delete(Draft entity) {
        final String where = DraftTable._ID + " = ?";
        final String[] args = { String.valueOf(entity.getId()) };

        dbHelper.getWritableDatabase().delete(DraftTable.TABLE_NAME, where, args);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Deleted: " + entity.toString());
        }

        notifyObservers();
    }

    @Override public List<Draft> findAll() {
        final List<Draft> drafts = new ArrayList<>();

        Cursor cursor = dbHelper.getWritableDatabase().query(
                DraftTable.TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Draft draft = transformCursorToModel(cursor);
                drafts.add(draft);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return drafts;
    }

    @Override public void refresh() {
        notifyObservers();
    }

    @Override public boolean exists(Integer id) {
        final String[] columns = { DraftTable._ID };
        final String selection = DraftTable._ID + " = ?";
        final String[] selectionArgs = { String.valueOf(id) };
        boolean exists = false;

        Cursor cursor = dbHelper.getReadableDatabase().query(
                DraftTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = true;
            }
            cursor.close();
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Draft exists: " + exists);
        }

        return exists;
    }

    private Draft transformCursorToModel(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DraftTable._ID));
        String text = cursor.getString(cursor.getColumnIndex(DraftTable.COLUMN_TEXT));
        long date = cursor.getLong(cursor.getColumnIndex(DraftTable.COLUMN_CREATED));

        return new Draft(id, text, new Date(date));
    }

    private ContentValues transformModelToContentValues(Draft draft) {
        ContentValues cv = new ContentValues();

        cv.put(DraftTable.COLUMN_TEXT, draft.getText());
        cv.put(DraftTable.COLUMN_CREATED, draft.getCreated().getTime());

        return cv;
    }
}
