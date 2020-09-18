package io.github.onlyeat3.fastmapper;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.WeakHashMap;

public class ReflectionUtils {
    public static final WeakHashMap CACHE = new WeakHashMap();
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";

    public static Object getFieldValue(Object obj, String fieldName) {
        String capitalisedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String getName = GET_PREFIX + capitalisedFieldName;
        MethodAccess methodAccess = MethodAccess.get(obj.getClass());
        if (methodAccess.getIndex(getName) > 0) {
            return methodAccess.invoke(obj,getName);
        }else{
            return null;
        }
    }

    public static void setFieldValue(Object targetObj, Object fieldValue, Field field) {
        String fieldName = field.getName();

        String capitalisedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        String setName = SET_PREFIX + capitalisedFieldName;
        String isName = IS_PREFIX + capitalisedFieldName;
        MethodAccess methodAccess = MethodAccess.get(targetObj.getClass());
        if (methodAccess.getIndex(setName) > 0) {
            methodAccess.invoke(targetObj,setName,fieldValue);
            return;
        }
        if (methodAccess.getIndex(isName) > 0) {
            methodAccess.invoke(targetObj,isName,fieldValue);
        }
    }

    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new FastMapperException(e);
        }
    }

    public static Field[] getBeanInfo(Class clazz) {
        return FieldAccess.get(clazz).getFields();
    }

    public static <T> T newInstance(Class<T> clazz) throws IllegalAccessException {
        if (clazz == null) {
            return null;
        }
//        try {
//            return clazz.newInstance();
//        } catch (InstantiationException e) {
//            throw new IllegalArgumentException(" class "+clazz.getName()+"Miss default Constructor");
//        }
        return ConstructorAccess.get(clazz).newInstance();
    }

    public static Type getFieldClass(Object targetObj, String fieldName) {
        FieldAccess fieldAccess = FieldAccess.get(targetObj.getClass());
        int idx = fieldAccess.getIndex(fieldName);
        if (idx < 0) {
            return null;
        }
        Field field = fieldAccess.getFields()[idx];
        Type resultType = field.getGenericType();
        if (resultType == null) {
            resultType = field.getType();
        }
        return resultType;
    }
}
