package top.infra.jackson2.tests.complexstupid;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Output implements Serializable {

    private String inputName;
    private List<OutputDetail> details;

    @Data
    public static class OutputDetail implements Serializable {

        private String field1;

        private String field2;
    }
}
