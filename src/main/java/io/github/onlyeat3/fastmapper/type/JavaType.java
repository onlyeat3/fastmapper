package io.github.onlyeat3.fastmapper.type;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import io.github.onlyeat3.fastmapper.ReflectionUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.*;
import java.util.*;

@Data
public class JavaType<T> {
    public static final WeakHashMap<Type,JavaType> CACHE = new WeakHashMap<>();
    public static final JavaType NO_TYPE = new JavaType();
    private static final List<Class> WELL_KNOWN_TYPES = new ArrayList<>();

    static {
        WELL_KNOWN_TYPES.add(Byte.class);
        WELL_KNOWN_TYPES.add(Integer.class);
        WELL_KNOWN_TYPES.add(Long.class);
        WELL_KNOWN_TYPES.add(Character.class);
        WELL_KNOWN_TYPES.add(String.class);
        WELL_KNOWN_TYPES.add(Boolean.class);
    }

    private TypeBindings typeBindings = new TypeBindings();
    private Class<T> rawClass;
    @Setter
    @Getter
    private T instance;
    private Type[] actualTypeArguments;
    protected JavaType parent;

    public JavaType() {
        this(null);
    }

    public JavaType(Class<T> rootType) {
        this(null, rootType);
    }

    private JavaType(JavaType parent, Class<T> curr) {
        this.parent = parent;
        this.rawClass = curr;
    }

    public <S> JavaType<S> child(Class<S> cls) {
        return new JavaType<>(this, cls);
    }

//    public void addSelfReference(ResolvedRecursiveType ref) {
//        if (this._selfRefs == null) {
//            this._selfRefs = new ArrayList();
//        }
//
//        this._selfRefs.add(ref);
//    }

    public static <T> JavaType<T> from(ClassReference<T> classReference) {
        Type type = classReference.getType();
        return fromAny(type);
    }

    public static <A> JavaType<A> from(Type clazz) {
        JavaType cachedJavaType = CACHE.get(clazz);
        if (cachedJavaType != null) {
            return cachedJavaType;
        }
        JavaType<A> aJavaType = fromAny(clazz);
        CACHE.put(clazz,aJavaType);
        return aJavaType;
    }

    private static <A> JavaType<A> fromAny(Type type) {
        JavaType resultType;
        // simple class?
        TypeBindings bindings = TypeBindings.empty();
        if (type instanceof Class<?>) {
            // Important: remove possible bindings since this is type-erased thingy
            resultType = _fromClass((Class<?>) type, bindings);
        }
        // But if not, need to start resolving.
        else if (type instanceof ParameterizedType) {
            resultType = _fromParamType((ParameterizedType) type, bindings);
        } else if (type instanceof JavaType) { // [databind#116]
            // no need to modify further if we already had JavaType
            return (JavaType) type;
        } else if (type instanceof GenericArrayType) {
            resultType = _fromArrayType((GenericArrayType) type, bindings);
        } else if (type instanceof TypeVariable<?>) {
            resultType = _fromVariable((TypeVariable<?>) type, bindings);
        } else if (type instanceof WildcardType) {
            resultType = _fromWildcard((WildcardType) type, bindings);
        } else {
            // sanity check
            throw new IllegalArgumentException("Unrecognized Type: " + ((type == null) ? "[null]" : type.toString()));
        }
        return resultType;
    }

    private static JavaType _fromClass(Class<?> type, TypeBindings parentBindings) {
        JavaType javaType = new JavaType();
        javaType.rawClass = type;
        javaType.getTypeBindings().merge(parentBindings);
        return javaType;
    }

    @SuppressWarnings("unchecked")
    private static JavaType _fromParamType(ParameterizedType type, TypeBindings parentBindings) {
        JavaType javaType = new JavaType();
        javaType.setRawClass((Class) type.getRawType());
        javaType.setActualTypeArguments(type.getActualTypeArguments());

        TypeBindings typeBindings = new TypeBindings();

        TypeVariable[] typeParameters = ((Class) type.getRawType()).getTypeParameters();
        for (int i = 0; i < typeParameters.length; i++) {
            TypeVariable typeParameter = typeParameters[i];
            typeBindings.put(typeParameter.getTypeName(), type.getActualTypeArguments()[i]);
        }
        javaType.setTypeBindings(typeBindings);
        return javaType;
    }

    private static JavaType _fromArrayType(GenericArrayType type, TypeBindings parentBindings) {
        throw new RuntimeException("Not Implement!");
    }

    private static JavaType _fromVariable(TypeVariable<?> type, TypeBindings parentBindings) {
        throw new RuntimeException("Not Implement!");
    }

    private static JavaType _fromWildcard(WildcardType type, TypeBindings parentBindings) {
        throw new RuntimeException("Not Implement!");
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        if (this.instance != null) {
            return this.instance;
        }
        Class<T> clazz = this.getRawClass();
        if (clazz.isArray()) {
            return (T) Array.newInstance(clazz.getComponentType(), 0);//Arrays.copyOf(array,newSize);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            if (List.class.isAssignableFrom(clazz)) {
                return (T) new ArrayList();
            } else if (Set.class.isAssignableFrom(clazz)) {
                return (T) new HashSet();
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            return (T) new HashMap();
        }else if(Enum.class.isAssignableFrom(clazz)){
            //skip
        }else{
            try {
                return ReflectionUtils.newInstance(clazz);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public boolean rawClassEquals(Class clazz) {
        return this.getRawClass().equals(clazz);
    }

    public boolean isArray() {
        return this.rawClass.isArray();
    }

    public boolean isMapLikeType() {
        return Map.class.isAssignableFrom(this.rawClass);
    }

    public boolean isCollectionLikeType() {
        return Collection.class.isAssignableFrom(this.rawClass);
    }

    public boolean isListLikeType() {
        return List.class.isAssignableFrom(this.rawClass);
    }

    public boolean isRootObject() {
        return this.rawClass == Object.class;
    }

    public boolean isPrimitive() {
        return this.rawClass.isPrimitive();
    }

    public boolean isEnum() {
        return this.rawClass.isEnum();
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.rawClass.getModifiers());
    }

    public boolean isWellKnownType() {
        return WELL_KNOWN_TYPES.contains(this.getRawClass());
    }


}
