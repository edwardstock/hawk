package com.orhanobut.hawk;

import java.util.LinkedList;
import java.util.Queue;

public class StorageBatch {
    private Queue<Operation> mOperations = new LinkedList<>();

    public enum OpType {
        Put,
        Delete
    }

    /**
     * Put value into db
     * @param key data name
     * @param value data value
     * @return self
     */
    public StorageBatch put(String key, String value) {
        mOperations.add(new Operation(key, value));
        return this;
    }

    /**
     * Delete value from db
     * @param key data name
     * @return self
     */
    public StorageBatch delete(String key) {
        mOperations.add(new Operation(key));
        return this;
    }

    /**
     * Get queue of operations
     * @return Ordered Queue
     */
    public Queue<Operation> getOperations() {
        return mOperations;
    }

    public final static class Operation {
        private OpType mType;
        private String mKey;
        private String mValue;

        Operation(String key) {
            mType = OpType.Delete;
            mKey = key;
        }

        Operation(String key, String value) {
            mType = OpType.Put;
            mKey = key;
            mValue = value;
        }

        /**
         * Get operation type
         * @return
         */
        public OpType getType() {
            return mType;
        }

        /**
         * Get data name
         * @return
         */
        public String getKey() {
            return mKey;
        }

        /**
         * Get data value (delete operation does not have value)
         * @return data value or null
         */
        public String getValue() {
            return mValue;
        }
    }
}
