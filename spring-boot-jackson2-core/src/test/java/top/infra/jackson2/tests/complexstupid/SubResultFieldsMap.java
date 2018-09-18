package top.infra.jackson2.tests.complexstupid;

import java.io.Serializable;

import top.infra.jackson2.tests.complexstupid.shit.ResultFieldsMap;

public class SubResultFieldsMap extends ResultFieldsMap<SourceEnum, Input, Output> implements Serializable {

    public SubResultFieldsMap(final Input input) {
        super(input);
    }

    public SubResultFieldsMap() {
        super(null);
    }
}
