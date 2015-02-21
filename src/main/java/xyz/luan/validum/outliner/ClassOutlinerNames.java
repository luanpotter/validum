package xyz.luan.validum.outliner;

public enum ClassOutlinerNames {

    CLASS_DESCRIPTION('c'), // represents the main block of the class object where we find the kind, type and class level annotations
    KIND('k'), // the kind can be class or enum
    TYPE('t'), // the type is the canonical name of the type 

    ARRAY_ELEMENT('e'), // in an array type, refers to the elements
    MAP_KEY('k'), // in an map type, refers to the keys
    MAP_VALUE('v'), // in an map type, refers to the values

    ARRAY_TYPE('a'), // type represents an array
    MAP_TYPE('m'); // type represents a map

    private String value;

    private ClassOutlinerNames(char c) {
        this.value = "[" + c + "]";
    }

    public String val() {
        return this.value;
    }
}
