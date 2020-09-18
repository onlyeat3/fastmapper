package io.github.onlyeat3.fastmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.onlyeat3.fastmapper.type.JavaType;
import io.github.onlyeat3.fastmapper.type.ClassReference;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapperBuilder;
import io.github.onlyeat3.fastmapper.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FastMapperTest {

    private static int PERFORMANCE_MAX_TIMES = 10_000;

    @Test
    public void testSimpleMapping() {
        Object result = FastMapper.map("1", Integer.class);
        assertEquals(result, 1);
    }

    @Test
    public void testMapObject() {
        StringModel stringModel = new StringModel();
        stringModel.setId("1");
        IntModel result = FastMapper.map(stringModel, IntModel.class);
        assertEquals(1, (int) result.getId());
    }

    @Test
    public void testMapToBean() {
        Map<String, Object> map = new HashMap<>();
        map.put("stringField", "xxxxx");
        map.put("timeStampField", System.currentTimeMillis());
        map.put("longField", 1L);
        Foo foo = FastMapper.map(map, Foo.class);
        assertNotNull(foo);
        assertNotNull(foo.getStringField());
        assertNotNull(foo.getTimeStampField());
        assertNotNull(foo.getLongField());
    }

    @Test
    public void testBeanToMap() {
        Foo foo = new Foo();
        foo.setStringField("abcdefg");
        foo.setTimeStampField(new Timestamp(System.currentTimeMillis()));
        foo.setLongField(99999L);

        Map<String, Object> map = FastMapper.map(foo, new ClassReference<Map<String, Object>>() {
        });
        assertNotNull(map);
        assertTrue(map.containsKey("stringField"));
        assertTrue(map.containsKey("timeStampField"));
        assertTrue(map.containsKey("longField"));
        assertFalse(map.get("longField") instanceof Optional);
    }

    @Test
    public void testBeanToBean() {
        Foo foo = new Foo();
        foo.setStringField("abcdefg");
        foo.setTimeStampField(new Timestamp(System.currentTimeMillis()));
        foo.setLongField(99999L);
        Bar bar = FastMapper.map(foo, Bar.class);
        assertNotNull(bar);
        assertNotNull(bar.getStringField());
        assertNotNull(bar.getLongField());
        assertNotNull(bar.getTimeStampField());
    }

    @Test
    public void listToList() {
        List<Foo> fooList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Foo foo = new Foo();
            foo.setStringField("abcdefg" + i);
            foo.setTimeStampField(new Timestamp(System.currentTimeMillis()));
            foo.setLongField((long) i);
            fooList.add(foo);
        }
        List<Bar> list = FastMapper.map(fooList, new ClassReference<List<Bar>>() {
        });
        assertNotNull(list);
        assertFalse(list.isEmpty());
        for (Bar bar : list) {
            assertNotNull(bar.getStringField());
            assertNotNull(bar.getLongField());
            assertNotNull(bar.getTimeStampField());
        }
    }

    @Test
    public void copyProperties() {
        Foo foo = new Foo();
        foo.setStringField("abcdefg");
        foo.setTimeStampField(new Timestamp(System.currentTimeMillis()));
        foo.setLongField(99999L);
        Bar bar = new Bar();
        FastMapper.populate(foo, bar);
        assertNotNull(bar.getLongField());
        assertNotNull(bar.getTimeStampField());
        assertNotNull(bar.getStringField());
    }

    @Test
    public void customTypeMapper() {
        FastMapper.registerTypeMapper(SomeEnum.class, Long.class, someEnum-> someEnum.getValue().longValue(), aLong -> SomeEnum.valueOf(aLong.intValue()));

        Foo foo = new Foo();
        foo.setStringField("abcdefg");
        foo.setTimeStampField(new Timestamp(System.currentTimeMillis()));
        foo.setLongField(1L);
        Fooo fooo = FastMapper.map(foo, Fooo.class);
        assertNotNull(fooo);
        assertNotNull(fooo.getLongField());
        assertNotNull(fooo.getTimeStampField());
        assertNotNull(fooo.getStringField());
    }

    @Test
    public void testDeepPropertyByFastMapper(){
        CascadeModel cascadeModel = new CascadeModel();
        cascadeModel.setName("parent");

        CascadeModel child = new CascadeModel();
        child.setName("child");
        cascadeModel.setChild(child);
        cascadeModel.setChildren(Collections.singletonList(child));

        CascadeModel result = FastMapper.map(cascadeModel, CascadeModel.class);
        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getChild());
        assertNotNull(result.getChildren());
    }

    @Test
    public void testDeepPropertyBySetter(){
        CascadeModel cascadeModel = new CascadeModel();
        cascadeModel.setName("parent");

        CascadeModel child = new CascadeModel();
        child.setName("child");
        cascadeModel.setChild(child);
        cascadeModel.setChildren(Collections.singletonList(child));

        CascadeModel result = new CascadeModel();
        result.setName(cascadeModel.getName());
        CascadeModel child1 = new CascadeModel();
        result.setChild(child1);
        cascadeModel.setChildren(Collections.singletonList(child1));

        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getChild());
        assertNotNull(result.getChildren());
    }

    @Test
    public void testDeepPropertyByBeanUtils(){
        CascadeModel cascadeModel = new CascadeModel();
        cascadeModel.setName("parent");

        CascadeModel child = new CascadeModel();
        child.setName("child");
        cascadeModel.setChild(child);
        cascadeModel.setChildren(Collections.singletonList(child));

        try {
            Class<?> clazz = Class.forName(CascadeModel.class.getName());
            CascadeModel result = (CascadeModel) clazz.newInstance();
            BeanUtils.copyProperties(result,cascadeModel);
            assertNotNull(result);
            assertNotNull(result.getName());
            assertNotNull(result.getChild());
            assertNotNull(result.getChildren());
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException e) {
        }
    }

    @Test
    public void testDeepProperty2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"pageInfo\":{\"totalNumber\":\"3\",\"page\":1,\"pageSize\":100,\"totalPage\":1},\"list\":[{\"status\":5,\"name\":\"5849854\",\"thumbnail\":\"https://x\",\"siteType\":\"0\",\"createTime\":\"2019-07-31 15:50:52\",\"siteId\":\"123zxcv\",\"pgmCount\":0,\"modifyTime\":\"2019-07-31 15:53:38\"},{\"status\":5,\"name\":\"pioupoiupo\",\"thumbnail\":\"https://x\",\"siteType\":\"0\",\"createTime\":\"2019-07-25 11:25:40\",\"siteId\":\"654\",\"pgmCount\":0,\"modifyTime\":\"2019-07-25 11:29:35\"},{\"status\":5,\"name\":\"ax\",\"thumbnail\":\"https://x\",\"siteType\":\"0\",\"createTime\":\"2019-07-25 11:15:35\",\"siteId\":\"123\",\"pgmCount\":0,\"modifyTime\":\"2019-07-25 11:20:46\"}]}";
        ToolsSiteGetResponse toolsSiteGetResponse = objectMapper.readValue(json, ToolsSiteGetResponse.class);
        ToolsSiteGetResponse anotherToolsSiteGetResponse = FastMapper.map(toolsSiteGetResponse, ToolsSiteGetResponse.class);
        assertNotNull(anotherToolsSiteGetResponse);
        assertTrue(objectMapper.writeValueAsString(anotherToolsSiteGetResponse).equals(objectMapper.writeValueAsString(toolsSiteGetResponse)));
    }

    @Test
    public void testMapMap(){
        TypeMapper<SomeEnum, Long> someEnumLongTypeMapper = TypeMapperBuilder.<SomeEnum, Long>of()
                .matcher(mappingInfo -> mappingInfo.getSourceType().getRawClass().equals(SomeEnum.class) && mappingInfo.getTargetType().getRawClass().equals(Long.class))
                .mapFunction(source -> Long.valueOf(source.getValue()))
                .build();

        TypeMapper<Long, SomeEnum> longSomeEnumTypeMapper = TypeMapperBuilder.<Long, SomeEnum>of()
                .matcher(mappingInfo -> mappingInfo.getTargetType().getRawClass().equals(SomeEnum.class) && mappingInfo.getSourceType().getRawClass().equals(Long.class))
                .mapFunction(source -> SomeEnum.valueOf(source.intValue()))
                .build();
        FastMapper.registerTypeMapper(someEnumLongTypeMapper);
        FastMapper.registerTypeMapper(longSomeEnumTypeMapper);

        Fooo foo = new Fooo();
        foo.setStringField("abcdefg");
        foo.setTimeStampField(new Timestamp(System.currentTimeMillis()));
        foo.setLongField(SomeEnum.ONE);

        Bar bar = new Bar();
        bar.setLongField("2");
        bar.setStringField("999992");

        foo.setBar(bar);
        Foo f = new Foo();
        f.setLongField(3L);
        foo.getFooBarMap().put(f,bar);

        Fooo fooo = FastMapper.map(foo, JavaType.from(Fooo.class));
        assertNotNull(fooo);
        assertNotNull(fooo.getFooBarMap());
        assertFalse(fooo.getFooBarMap().isEmpty());

        for (Map.Entry<Foo, Bar> entry : fooo.getFooBarMap().entrySet()) {
            assertNotSame(entry.getKey(),f);
            assertSame(entry.getKey().getClass(),Foo.class);
            assertNotSame(entry.getValue(),bar);
            assertSame(entry.getValue().getClass(),Bar.class);
        }
    }

    @Test
    public void testDeepMap(){
        Map<String,Object> map = new HashMap<>();
        HashMap<Object, Object> innerMap = new HashMap<>();
        Foo foo = new Foo();
        foo.setLongField(1L);
        innerMap.put("target", foo);
        map.put("inner", innerMap);

        HashMap<String, HashMap<Object, Object>> wow = FastMapper.map(map, new ClassReference<HashMap<String, HashMap<Object, Object>>>() {
        });
        System.out.println(wow);
    }

    @Test
    public void testSetterPerformance(){
        //setter
        for (int i = 0; i < PERFORMANCE_MAX_TIMES; i++) {
            testDeepPropertyBySetter();
        }
    }

    @Test
    public void testFastMapperPerformance(){
        //FastMapper
        for (int i = 0; i < PERFORMANCE_MAX_TIMES; i++) {
            testDeepPropertyByFastMapper();
        }
    }

    @Test
    public void testBeanUtilsPerformance(){
        //FastMapper
        for (int i = 0; i < PERFORMANCE_MAX_TIMES; i++) {
            testDeepPropertyByBeanUtils();
        }
    }

}
