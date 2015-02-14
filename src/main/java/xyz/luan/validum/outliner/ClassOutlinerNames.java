package xyz.luan.validum.outliner;

public enum ClassOutlinerNames {

    CLASS_LEVEL_ANNOTATION('c'),
    FIELD_TYPE('t'),
    ARRAY_ELEMENT('e'),
    MAP_KEY('k'),
    MAP_VALUE('v'),
    ARRAY_TYPE('a'),
    MAP_TYPE('m');

    private String value;

    private ClassOutlinerNames(char c) {
        this.value = "[" + c + "]";
    }

    public String val() {
        return this.value;
    }
}
