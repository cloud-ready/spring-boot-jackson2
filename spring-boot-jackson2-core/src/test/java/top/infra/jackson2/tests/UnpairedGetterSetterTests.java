package top.infra.jackson2.tests;

import static org.junit.Assert.assertEquals;
import static top.infra.jackson2.tests.complexstupid.SourceEnum.SOURCE1;
import static top.infra.jackson2.tests.complexstupid.shit.FieldNameEnum.FIELD1;
import static top.infra.jackson2.tests.complexstupid.shit.FieldNameEnum.FIELD2;
import static top.infra.jackson2.tests.complexstupid.shit.FieldStatusEnum.DELETED;
import static top.infra.jackson2.tests.complexstupid.shit.FieldStatusEnum.OK;

import com.google.common.collect.Lists;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.Jackson2Properties;
import top.infra.jackson2.Jackson2Utils;
import top.infra.jackson2.tests.complexstupid.Input;
import top.infra.jackson2.tests.complexstupid.Output;
import top.infra.jackson2.tests.complexstupid.SubResultFieldsMap;
import top.infra.jackson2.tests.complexstupid.shit.Field;
import top.infra.jackson2.tests.complexstupid.shit.ResultFields;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class UnpairedGetterSetterTests {

    @Autowired
    private Jackson2Properties jackson2Properties;
    private ObjectMapper customObjectMapper;
    private ObjectMapper rawObjectMapper;
    @Autowired
    private ObjectMapper springObjectMapper;

    @Before
    public void setUp() {
        this.customObjectMapper = Jackson2Utils.customize(this.jackson2Properties, new ObjectMapper());
        this.rawObjectMapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    public void testMissingSetter() {
        final Input input1 = new Input();
        input1.setName("input1");
        input1.setValue("input1Value");
        final Output.OutputDetail outputDetail1 = new Output.OutputDetail();
        outputDetail1.setField1("outputDetail1Field1");
        outputDetail1.setField2("outputDetail1Field2");
        final Output output1 = new Output();
        output1.setInputName(input1.getName());
        output1.setDetails(Lists.newArrayList(outputDetail1));

        final Field field1 = new Field();
        field1.setName(FIELD1);
        field1.setValue("value1");
        field1.setStatus(OK);

        final Field field2 = new Field();
        field2.setName(FIELD2);
        field2.setValue("value2");
        field2.setStatus(DELETED);

        final ResultFields<Output> resultFields = new ResultFields<>();
        resultFields.setOutput(output1);
        resultFields.put(FIELD1, field1);
        resultFields.put(FIELD2, field2);

        final SubResultFieldsMap data = new SubResultFieldsMap(input1);
        data.put(SOURCE1, resultFields);

        log.info("data customObjectMapper: {}", this.customObjectMapper.writeValueAsString(data));
        log.info("data rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(data));
        log.info("data springObjectMapper: {}", this.springObjectMapper.writeValueAsString(data));

        assertEquals(this.springObjectMapper.writeValueAsString(data), this.customObjectMapper.writeValueAsString(data));
        assertEquals(this.customObjectMapper.writeValueAsString(data), this.rawObjectMapper.writeValueAsString(data));

        final SubResultFieldsMap readed = this.springObjectMapper.readValue(
            this.springObjectMapper.writeValueAsString(data), SubResultFieldsMap.class);
        assertEquals(data, readed);
    }
}
