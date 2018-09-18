package top.infra.jackson2.tests.complexstupid.shit;

import lombok.Data;

import java.io.Serializable;

@Data
public class Field implements Serializable {

    private FieldNameEnum name;

    private String value;

    private FieldStatusEnum status;
}
