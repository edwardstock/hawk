package com.orhanobut.hawk;

public interface HawkDB {

    /**
     * Saves any type including any collection, primitive values or custom objects
     * @param key is required to differentiate the given data
     * @param value is the data that is going to be encrypted and persisted
     * @return true if the operation is successful. Any failure in any step will return false
     */
    <T> boolean put(String key, T value);

    /**
     * Put/Delete huge amount of values into storage by single (or optimized) operation, it can be handy
     * Performance of this approach depends on storage
     * @param batch batch operations to
     * @see StorageBatch it have simple api
     */
    void batch(StorageBatch batch);

    /**
     * Gets the original data along with original type by the given key.
     * This is not guaranteed operation since Hawk uses serialization. Any change in in the requested
     * data type might affect the result. It's guaranteed to return primitive types and String type
     * @param key is used to get the persisted data
     * @return the original object
     */
    <T> T get(String key);

    /**
     * Gets the saved data, if it is null, default value will be returned
     * @param key is used to get the saved data
     * @param defaultValue will be return if the response is null
     * @return the saved object
     */
    <T> T get(String key, T defaultValue);

    /**
     * Size of the saved data. Each key will be counted as 1
     * @return the size
     */
    long count();

    /**
     * Clears the storage, note that crypto data won't be deleted such as salt key etc.
     * Use resetCrypto in order to deleteAll crypto information
     * @return true if deleteAll is successful
     */
    boolean deleteAll();

    /**
     * Removes the given key/value from the storage
     * @param key is used for removing related data from storage
     * @return true if delete is successful
     */
    boolean delete(String key);

    /**
     * Checks the given key whether it exists or not
     * @param key is the key to check
     * @return true if it exists in the storage
     */
    boolean contains(String key);

    /**
     * Use this method to verify if Hawk is ready to be used.
     * @return true if correctly initialised and built. False otherwise.
     */
    boolean isBuilt();

    class UninitializedHawkDB implements HawkDB {

        @Override
        public <T> boolean put(String key, T value) {
            throwValidation();
            return false;
        }

        @Override
        public void batch(StorageBatch batch) {
            throwValidation();
        }

        @Override
        public <T> T get(String key) {
            throwValidation();
            return null;
        }

        @Override
        public <T> T get(String key, T defaultValue) {
            throwValidation();
            return null;
        }

        @Override
        public long count() {
            throwValidation();
            return 0;
        }

        @Override
        public boolean deleteAll() {
            throwValidation();
            return false;
        }

        @Override
        public boolean delete(String key) {
            throwValidation();
            return false;
        }

        @Override
        public boolean contains(String key) {
            throwValidation();
            return false;
        }

        @Override
        public boolean isBuilt() {
            return false;
        }

        private void throwValidation() {
            throw new IllegalStateException("Hawk is not built. " +
                    "Please call build() and wait the initialisation finishes.");
        }
    }
}
