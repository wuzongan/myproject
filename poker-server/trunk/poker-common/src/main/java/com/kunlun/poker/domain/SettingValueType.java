package com.kunlun.poker.domain;

public enum SettingValueType {
    INT {
        @Override
        public Object parse(String value) {
            return Integer.valueOf(value);
        }
    },
    FLOAT {
        @Override
        public Object parse(String value) {
            return Float.valueOf(value);
        }
    },
    STRING {
        @Override
        public Object parse(String value) {
            return value;
        }
    },
    BOOLEAN {
        @Override
        public Object parse(String value) {
            return Boolean.valueOf(value);
        }
    };

    public abstract Object parse(String value);
}
