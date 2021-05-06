package com.model;

import java.util.EnumSet;

public enum SpecialDataTypes {

    TEXT("String"),
    NUMBER("String"),
    BOOLEAN("String"),
    PERCENTAGE("String"),
    USER_LIST("List<User>"),
    TOPIC_LIST("List<Topic>");


    private String dataType;

    public static EnumSet<SpecialDataTypes> OBJECT = EnumSet.of(USER_LIST,TOPIC_LIST);

    SpecialDataTypes(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public static boolean isObjectDataType(String value) {
        for (SpecialDataTypes tmp : values()) {
            if (tmp.name().equals(value)) {
                return tmp.isInObjectGroup(tmp);
            }
        }
        return false;
    }

    public boolean isInObjectGroup(SpecialDataTypes tmp) {
        return SpecialDataTypes.OBJECT.contains(tmp);
    }
}
