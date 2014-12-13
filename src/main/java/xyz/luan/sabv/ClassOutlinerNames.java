package xyz.luan.sabv;

import static xyz.luan.sabv.annotation.ToJson.strToJson;

public enum ClassOutlinerNames {

    CLASS_LEVEL_ANNOTATION('c'), FIELD_TYPE('t'), ARRAY_ELEMENT('e'), MAP_KEY('k'), MAP_VALUE('v'), ARRAY_TYPE('a'), MAP_TYPE('m');

    private String value;

    private ClassOutlinerNames(char c) {
        this.value = strToJson("[" + c + "]");
    }

    public String val() {
        return this.value;
    }
}
