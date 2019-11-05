package com.orhanobut.hawk;

public class DefaultHawkDB implements HawkDB {

    private final Storage mStorage;
    private final Converter mConverter;
    private final Encryption mEncryption;
    private final Serializer mSerializer;
    private final LogInterceptor mLogger;

    public DefaultHawkDB(HawkBuilder builder) {
        mEncryption = builder.getEncryption();
        mStorage = builder.getStorage();
        mConverter = builder.getConverter();
        mSerializer = builder.getSerializer();
        mLogger = builder.getLogger();
        mLogger.onLog("Hawk.init -> Encryption : " + mEncryption.getClass().getSimpleName());
    }

    @Override
    public <T> boolean put(String key, T value) {
        // Validate
        HawkUtils.checkNull("Key", key);
        log("Hawk.put -> key: " + key + ", value: " + value);

        // If the value is null, delete it
        if (value == null) {
            log("Hawk.put -> Value is null. Any existing value will be deleted with the given key");
            return delete(key);
        }

        // 1. Convert to text
        String plainText = mConverter.toString(value);
        log("Hawk.put -> Converted to " + plainText);
        if (plainText == null) {
            log("Hawk.put -> Converter failed");
            return false;
        }

        // 2. Encrypt the text
        String cipherText = null;
        try {
            cipherText = mEncryption.encrypt(key, plainText);
            log("Hawk.put -> Encrypted to " + cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cipherText == null) {
            log("Hawk.put -> Encryption failed");
            return false;
        }

        // 3. Serialize the given object along with the cipher text
        String serializedText = mSerializer.serialize(cipherText, value);
        log("Hawk.put -> Serialized to " + serializedText);
        if (serializedText == null) {
            log("Hawk.put -> Serialization failed");
            return false;
        }

        // 4. Save to the storage
        if (mStorage.put(key, serializedText)) {
            log("Hawk.put -> Stored successfully");
            return true;
        } else {
            log("Hawk.put -> Store operation failed");
            return false;
        }
    }

    @Override
    public void batch(StorageBatch batch) {
        StorageBatch outBatch = new StorageBatch();

        while (!batch.getOperations().isEmpty()) {
            StorageBatch.Operation op = batch.getOperations().poll();

            if (op.getType() == StorageBatch.OpType.Delete) {
                outBatch.delete(op.getKey());
            } else {
                final String key = op.getKey();
                final String value = op.getValue();

                // Validate
                HawkUtils.checkNull("Key", op.getKey());
                log("Hawk.put -> key: " + key + ", value: " + value);

                // If the value is null, delete it
                if (value == null) {
                    log("Hawk.put -> Value is null. Any existing value will be deleted with the given key");
                    outBatch.delete(key);
                }

                // 1. Convert to text
                String plainText = mConverter.toString(value);
                log("Hawk.put -> Converted to " + plainText);
                if (plainText == null) {
                    log("Hawk.put -> Converter failed");

                    //@todo throw
                    return;
                }

                // 2. Encrypt the text
                String cipherText = null;
                try {
                    cipherText = mEncryption.encrypt(key, plainText);
                    log("Hawk.put -> Encrypted to " + cipherText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cipherText == null) {
                    log("Hawk.put -> Encryption failed");
                    //@todo throw
                    return;
                }

                // 3. Serialize the given object along with the cipher text
                String serializedText = mSerializer.serialize(cipherText, value);
                log("Hawk.put -> Serialized to " + serializedText);
                if (serializedText == null) {
                    log("Hawk.put -> Serialization failed");
                    //@todo throw
                    return;
                }

                // 4. Save to the storage
                outBatch.put(key, serializedText);
            }
        }

        mStorage.batch(outBatch);
    }

    @Override
    public <T> T get(String key) {
        log("Hawk.get -> key: " + key);
        if (key == null) {
            log("Hawk.get -> null key, returning null value ");
            return null;
        }

        // 1. Get serialized text from the storage
        String serializedText = mStorage.get(key);
        log("Hawk.get -> Fetched from storage : " + serializedText);
        if (serializedText == null) {
            log("Hawk.get -> Fetching from storage failed");
            return null;
        }

        // 2. Deserialize
        DataInfo dataInfo = mSerializer.deserialize(serializedText);
        log("Hawk.get -> Deserialized");
        if (dataInfo == null) {
            log("Hawk.get -> Deserialization failed");
            return null;
        }

        // 3. Decrypt
        String plainText = null;
        try {
            plainText = mEncryption.decrypt(key, dataInfo.cipherText);
            log("Hawk.get -> Decrypted to : " + plainText);
        } catch (Throwable e) {
            log("Hawk.get -> Decrypt failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        if (plainText == null) {
            log("Hawk.get -> Decrypt failed");
            return null;
        }

        // 4. Convert the text to original data along with original type
        T result = null;
        try {
            result = mConverter.fromString(plainText, dataInfo);
            log("Hawk.get -> Converted to : " + result);
        } catch (Exception e) {
            log("Hawk.get -> Converter failed");
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        T t = get(key);
        if (t == null) return defaultValue;
        return t;
    }

    @Override
    public long count() {
        return mStorage.count();
    }

    @Override
    public boolean deleteAll() {
        return mStorage.deleteAll();
    }

    @Override
    public boolean delete(String key) {
        return mStorage.delete(key);
    }

    @Override
    public boolean contains(String key) {
        return mStorage.contains(key);
    }

    @Override
    public boolean isBuilt() {
        return true;
    }

    private void log(String message) {
        mLogger.onLog(message);
    }
}
