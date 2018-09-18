package top.infra.jackson2.tests.complexstupid;

import lombok.Data;

import java.io.Serializable;

@Data
public class Input implements Serializable {

    private String name;

    private String value;
}
