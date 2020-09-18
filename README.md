# fastmapper
对象映射工具
## 特性
- [x] Java 8+
- [x] 自定义类型转换方式
- [x] 支持多层级对象拷贝(实验特性)
- [x] 泛型支持
## 使用说明
添加依赖到pom.xml
```xml
<dependency>
  <groupId>io.github.onlyeat3</groupId>
  <artifactId>fastmapper</artifactId>
  <version>0.0.1</version>
</dependency>
```

```java
//bean to map
Map<String, Object> map = FastMapper.map(foo, new ClassReference<Map<String, Object>>() {});
```

```java
//map to bean
Foo foo = FastMapper.map(map, Foo.class);
```
```java
//bean to bean
Bar bar = FastMapper.map(foo, Bar.class);
```
```java
//属性拷贝(忽略空的属性)
FastMapper.populate(foo,bar);
```

```java
//类方式写一个自定义TypeMapper（推荐）
@Order(5)//指定优先级，数字越小越靠前，但非预置转换器的优先级不会超过预置的
public class BigIntegerToLongTypeMapper implements TypeMapper<BigInteger, Long> {
    @Override
    public boolean match(MappingInfo<BigInteger, Long> mappingInfo) {
        return mappingInfo.getSourceType().getRawClass().equals(BigInteger.class) && mappingInfo.getTargetType().getRawClass().equals(Long.class);
    }

    @Override
    public Long map(MappingInfo<BigInteger, Long> mappingInfo) {
        return mappingInfo.getSource().longValue();
    }
}
FastMapper.registerTypeMapper(new BigIntegerToLongTypeMapper())
//lambda方式写自定义TypeMapper,从SomeEnum转换到Long，这样写会另外注册一个从Long到SomeEnum的TypeMapper
FastMapper.registerTypeMapper(SomeEnum.class, Long.class, someEnum-> someEnum.getValue().longValue(), aLong -> SomeEnum.valueOf(aLong.intValue()))
```

```java
//to list
List<Bar> list = FastMapper.map(fooList, new TypeReference<List<Bar>>() {});
```

```java
//泛型
List<Bar> list = FastMapper.map(fooList, new TypeReference<List<Bar>>() {});
```

以上就是全部功能,按照已有需求提前预置了一些`TypeMapper`，这些`TypeMapper`无法满足需要时可以自己添加`TypeMapper`，也可以在`ISSUE`提需求，由我来添加。

`fastmapper`不建议把bean写的嵌套过多，最好是单层的，但为了对付一些无奈的场景，还是提供了简单的深层拷贝支持.不需要额外配置，直接使用即可。