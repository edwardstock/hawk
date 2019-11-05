package com.orhanobut.hawk;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Secure, simple key-value storage for Android.
 */
public final class Hawk {
    public final static String DEFAULT_DB_TAG = "Hawk2";
    static Map<String, HawkDB> INSTANCES = new HashMap<>(1);

    private Hawk() {
        // no instance
    }

    /**
     * This will init the hawk without password protection.
     * @param context is used to instantiate context based objects.
     *         ApplicationContext will be used
     */
    public static HawkBuilder init(Context context) {
        HawkUtils.checkNull("Context", context);
        INSTANCES.put(DEFAULT_DB_TAG, null);
        return new HawkBuilder(DEFAULT_DB_TAG, context);
    }

    /**
     * This will init the hawk without password protection.
     * @param context is used to instantiate context based objects.
     *         ApplicationContext will be used
     */
    public static HawkBuilder init(String storageTag, Context context) {
        HawkUtils.checkNull("Context", context);
        HawkUtils.checkNull("Storage tag", storageTag);
        INSTANCES.put(storageTag, new HawkDB.UninitializedHawkDB());
        return new HawkBuilder(storageTag, context);
    }

    public static HawkDB db(String tag) {
        if (!INSTANCES.containsKey(tag) || INSTANCES.get(tag) == null) {
            throw new IllegalStateException(String.format("Hawk DB (%s) isn't initialized", DEFAULT_DB_TAG));
        }

        return INSTANCES.get(tag);
    }

    public static HawkDB db() {
        return db(DEFAULT_DB_TAG);
    }

    static void build(String tag, HawkBuilder hawkBuilder) {
        INSTANCES.put(tag, new DefaultHawkDB(hawkBuilder));
    }

}
