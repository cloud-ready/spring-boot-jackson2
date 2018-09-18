package top.infra.jackson2.tests.complexstupid.shit;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResultFieldsMap<Source, Input, Output> implements Serializable {

    private Input input;

    private Map<Source, ResultFields<Output>> resultFields = new HashMap<>();

    public void put(final Source source, final ResultFields<Output> result) {
        this.resultFields.put(source, result);
    }

    public ResultFields<Output> get(final Source source) {
        return this.resultFields.get(source);
    }

    public ResultFieldsMap(final Input input) {
        this.input = input;
    }

    public ResultFields<Output> getFirst() {
        if (this.resultFields != null && this.resultFields.size() > 0) {
            return this.resultFields.values().iterator().next();
        }
        return null;
    }
}
