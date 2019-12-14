package kz.greetgo.sandbox.controller.model;

import com.google.common.collect.Lists;

import java.util.List;

public enum DictType {
    REGION("Регион", false, null),
    COLLECTION("Коллекция", false, null),
    MEASURE("Обьем", false, null),
    USAGE("Назначение", false, null),
    FAMILY("Семейства", false, null),
    GENUS("Род", true, FAMILY),
    TYPE("Вид", true, GENUS),
    LIFE_FORM("Жизненная форм", false, null);

    public String title;
    public boolean hasParent = false;
    public DictType parent;

    DictType(String title, boolean hasParent, DictType parent){
        this.title = title;
        this.hasParent = hasParent;
        this.parent = parent;
    }

    public static List<DictRecord> listWithTitle(){
        List<DictRecord> ret = Lists.newArrayList();
        ret.add(new DictRecord(REGION.name(),REGION.title));
        ret.add(new DictRecord(COLLECTION.name(),COLLECTION.title));
        ret.add(new DictRecord(MEASURE.name(),MEASURE.title));
        ret.add(new DictRecord(USAGE.name(),USAGE.title));
        ret.add(new DictRecord(FAMILY.name(),FAMILY.title));
        ret.add(new DictRecord(GENUS.name(),GENUS.title));
        ret.add(new DictRecord(TYPE.name(),TYPE.title));
        ret.add(new DictRecord(LIFE_FORM.name(),LIFE_FORM.title));
        return ret;
    }

    public static boolean isHasParent(String code) {
        return DictType.valueOf(code).hasParent;
    }
}
