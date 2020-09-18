package io.github.onlyeat3.fastmapper.type;

import io.github.onlyeat3.fastmapper.model.Foo;
import io.github.onlyeat3.fastmapper.model.Fooo;
import io.github.onlyeat3.fastmapper.model.GenericModel;
import org.junit.jupiter.api.Test;

public class JavaTypeTest {

    @Test
    public void test() {
        JavaType.from(new ClassReference<GenericModel<String, Integer, Foo>>() {
        });
    }

    @Test
    public void from() {
        JavaType<Object> javaType = JavaType.from(Fooo.class);
        System.out.println(javaType);
    }
}