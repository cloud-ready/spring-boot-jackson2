package top.infra.jackson2.tests.complexstupid.shit;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResultFields<Output> implements Serializable {

    private Output output;

    private Map<FieldNameEnum, Field> fields = new HashMap<>();

    public void put(final FieldNameEnum field, final Field result) {
        this.fields.put(field, result);
    }

    public Field get(final FieldNameEnum field) {
        return this.fields.get(field);
    }
}
