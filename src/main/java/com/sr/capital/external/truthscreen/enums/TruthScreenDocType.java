package com.sr.capital.external.truthscreen.enums;

public enum TruthScreenDocType {

        PAN("pan", 2);

        private final String name;
        private final int value;

        TruthScreenDocType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
}
