package com.twitty.store;

import com.twitty.BuildConfig;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericRepository<T extends Identified<PK>, PK extends Serializable>
        implements CrudRepository<T, PK> {

    private static final String TAG = GenericRepository.class.getSimpleName();

    protected final DatabaseHelper dbHelper;
    protected final String table;

    public GenericRepository(String table, DatabaseHelper dbHelper) {
        this.table = table;
        this.dbHelper = dbHelper;
    }

    @Override public long save(T entity) {
        ContentValues cv = transformModelToContentValues(entity);
        long id = -1;

        if (entity.getId() == null) {
            id = dbHelper.getWritableDatabase().insert(table, null, cv);
        } else {
            final String where = BaseColumns._ID + " = ? ";
            final String [] args = { String.valueOf(entity.getId()) };
            dbHelper.getWritableDatabase().update(table, cv, where, args);
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Saved: " + entity.toString());
        }

        return id;
    }

    @Override public T findOne(PK id) {
        T entity = null;
        final String where = BaseColumns._ID + " = ?";
        final String[] args = { String.valueOf(id) };

        Cursor cursor = dbHelper.getReadableDatabase()
                .query(table, null, where, args, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                entity = transformCursorToModel(cursor);
            }
            cursor.close();
        }

        return entity;
    }

    @Override public void delete(T entity) {
        final String where = BaseColumns._ID + " = ?";
        final String[] args = { String.valueOf(entity.getId()) };

        dbHelper.getWritableDatabase().delete(table, where, args);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Deleted: " + entity.toString());
        }
    }

    @Override public List<T> findAll() {
        final List<T> entities = new ArrayList<>();

        Cursor cursor = dbHelper.getWritableDatabase()
                .query(table, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                T entity = transformCursorToModel(cursor);
                entities.add(entity);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return entities;
    }

    @Override public boolean exists(PK id) {
        final String[] columns = { BaseColumns._ID };
        final String selection = BaseColumns._ID + " = ?";
        final String[] selectionArgs = { String.valueOf(id) };
        boolean exists = false;

        Cursor cursor = dbHelper.getReadableDatabase()
                .query(table, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = true;
            }
            cursor.close();
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Entity exists: " + exists);
        }

        return exists;
    }

    protected abstract T transformCursorToModel(Cursor cursor);

    protected abstract ContentValues transformModelToContentValues(T entity);
}
