package com.orhanobut.hawk;

import android.content.Context;

import com.google.gson.Gson;

public class HawkBuilder {
    private Context mContext;
    private Storage mStorage;
    private Converter mConverter;
    private Parser mParser;
    private Encryption mEncryption;
    private Serializer mSerializer;
    private LogInterceptor mLogger;
    private String mTag;

    public HawkBuilder(String tag, Context context) {
        HawkUtils.checkNull("Context", context);
        mTag = tag;
        mContext = context.getApplicationContext();
    }

    HawkBuilder(Context context) {
        this(Hawk.DEFAULT_DB_TAG, context);
    }

    public void build() {
        Hawk.build(mTag, this);
    }

    public LogInterceptor getLogger() {
        if (mLogger == null) {
            mLogger = new LogInterceptor() {
                @Override
                public void onLog(String message) {
                    //empty implementation
                }
            };
        }
        return mLogger;
    }

    public HawkBuilder setLogger(LogInterceptor logger) {
        mLogger = logger;
        return this;
    }

    public Storage getStorage() {
        if (mStorage == null) {
            mStorage = new SharedPreferencesStorage(mContext, Hawk.DEFAULT_DB_TAG);
        }
        return mStorage;
    }

    public HawkBuilder setStorage(Storage storage) {
        mStorage = storage;
        return this;
    }

    public Converter getConverter() {
        if (mConverter == null) {
            mConverter = new HawkConverter(getParser());
        }
        return mConverter;
    }

    public HawkBuilder setConverter(Converter converter) {
        mConverter = converter;
        return this;
    }

    public Parser getParser() {
        if (mParser == null) {
            mParser = new GsonParser(new Gson());
        }
        return mParser;
    }

    public HawkBuilder setParser(Parser parser) {
        mParser = parser;
        return this;
    }

    public Encryption getEncryption() {
        if (mEncryption == null) {
            mEncryption = new ConcealEncryption(mContext);
            if (!mEncryption.init()) {
                mEncryption = new NoEncryption();
            }
        }
        return mEncryption;
    }

    public HawkBuilder setEncryption(Encryption encryption) {
        mEncryption = encryption;
        return this;
    }

    public Serializer getSerializer() {
        if (mSerializer == null) {
            mSerializer = new HawkSerializer(getLogger());
        }
        return mSerializer;
    }

    public HawkBuilder setSerializer(Serializer serializer) {
        mSerializer = serializer;
        return this;
    }
}
