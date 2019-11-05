package com.orhanobut.hawk;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesStorage implements Storage {

    private final SharedPreferences preferences;

    public SharedPreferencesStorage(Context context, String tag) {
        preferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
    }

    public SharedPreferencesStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public <T> boolean put(String key, T value) {
        HawkUtils.checkNull("key", key);
        return getEditor().putString(key, String.valueOf(value)).commit();
    }

    @Override
    public void batch(StorageBatch batch) {
        HawkUtils.checkNull("batch", batch);
        if (batch.getOperations().isEmpty()) return;

        SharedPreferences.Editor editor = getEditor();
        while (!batch.getOperations().isEmpty()) {
            StorageBatch.Operation op = batch.getOperations().poll();
            if (op == null) return;

            switch (op.getType()) {
                case Put:
                    editor.putString(op.getKey(), op.getValue());
                    break;
                case Delete:
                    editor.remove(op.getKey());
                    break;
            }
        }

        editor.commit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        return (T) preferences.getString(key, null);
    }

    @Override
    public boolean delete(String key) {
        return getEditor().remove(key).commit();
    }

    @Override
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    @Override
    public boolean deleteAll() {
        return getEditor().clear().commit();
    }

    @Override
    public long count() {
        return preferences.getAll().size();
    }

    private SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

}
