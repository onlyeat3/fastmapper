package io.github.onlyeat3.fastmapper;

import io.github.onlyeat3.fastmapper.type.ClassReference;
import io.github.onlyeat3.fastmapper.type.JavaType;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.SimpleTypeMapper;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;
import io.github.onlyeat3.fastmapper.type.mapper.complex.*;
import io.github.onlyeat3.fastmapper.type.mapper.simple.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FastMapper {
    private static final LinkedList<TypeMapper> SIMPLE_MAPPERS = new LinkedList<>();
    private static final LinkedList<TypeMapper> EXTRA_MAPPERS = new LinkedList<>();
    private static final LinkedList<TypeMapper> COMPLEX_MAPPERS = new LinkedList<>();
    private static final Comparator<TypeMapper> TYPE_MAPPER_COMPARATOR = new Comparator<TypeMapper>() {
        @Override
        public int compare(TypeMapper o1, TypeMapper o2) {
            int o1OrderValue = Integer.MAX_VALUE;
            Order o1Order = o1.getClass().getDeclaredAnnotation(Order.class);
            if (o1Order != null) {
                o1OrderValue = o1Order.value();
            }

            int o2OrderValue = Integer.MAX_VALUE;
            Order o2Order = o2.getClass().getDeclaredAnnotation(Order.class);
            if (o2Order != null) {
                o2OrderValue = o2Order.value();
            }
            return o1OrderValue - o2OrderValue;
        }
    };

    private static final LinkedList<TypeMapper> ALL_MAPPER = new LinkedList<>();

    static {
        SIMPLE_MAPPERS.add(new BigIntegerToLongTypeMapper());
        SIMPLE_MAPPERS.add(new LongToStringTypeMapper());
        SIMPLE_MAPPERS.add(new LongToTimeStampTypeMapper());
        SIMPLE_MAPPERS.add(new StringToIntegerTypeMapper());
        SIMPLE_MAPPERS.add(new StringToTimestampTypeMapper());
        SIMPLE_MAPPERS.add(new TimeStampToLongTypeMapper());
        SIMPLE_MAPPERS.add(new TimestampToStringTypeMapper());
        SIMPLE_MAPPERS.add(new TimestampToTimestampTypeMapper());
        SIMPLE_MAPPERS.add(new DateToTimeStampTypeMapper());
        SIMPLE_MAPPERS.add(new TimeStampToDateTypeMapper());
        SIMPLE_MAPPERS.add(new BigDecimalToBigDecimalTypeMapper());
        SIMPLE_MAPPERS.sort(TYPE_MAPPER_COMPARATOR);

        //------------------//

        COMPLEX_MAPPERS.add(new ArrayToArrayTypeMapper());
        COMPLEX_MAPPERS.add(new CollectionToListTypeMapper());
        COMPLEX_MAPPERS.add(new MapToMapTypeMapper());
        COMPLEX_MAPPERS.add(new MapToObjectTypeMapper());
        COMPLEX_MAPPERS.add(new ObjectToMapTypeMapper());
        COMPLEX_MAPPERS.add(new ObjectToObjectTypeMapper());
        COMPLEX_MAPPERS.add(new SameToSameTypeMapper());
        COMPLEX_MAPPERS.sort(TYPE_MAPPER_COMPARATOR);

        ALL_MAPPER.addAll(SIMPLE_MAPPERS);
        ALL_MAPPER.addAll(COMPLEX_MAPPERS);
        ALL_MAPPER.addAll(EXTRA_MAPPERS);
    }

    public static void registerTypeMapper(TypeMapper typeMapper) {
        ALL_MAPPER.removeAll(EXTRA_MAPPERS);
        EXTRA_MAPPERS.add(typeMapper);
        EXTRA_MAPPERS.sort(TYPE_MAPPER_COMPARATOR);
        ALL_MAPPER.addAll(EXTRA_MAPPERS);
    }

    public static <A,B> void registerTypeMapper(@NonNull Class<A> aClass, @NonNull Class<B> bClass, @NonNull Function<A, B> bToAFn, @NonNull Function<B, A> aToBFn) {
        List<TypeMapper> typeMappers = create(aClass, bClass, bToAFn, aToBFn);
        for (TypeMapper typeMapper : typeMappers) {
            registerTypeMapper(typeMapper);
        }
    }

    public static <S, T> void registerTypeMapper(Predicate<MappingInfo<S, T>> matcher, Function<MappingInfo<S, T>, T> mappingFunction) {
        TypeMapper<S, T> typeMapper = new TypeMapper<S, T>() {
            @Override
            public boolean match(MappingInfo<S, T> mappingInfo) {
                return matcher.test(mappingInfo);
            }

            @Override
            public T map(MappingInfo<S, T> mappingInfo) {
                return mappingFunction.apply(mappingInfo);
            }
        };
        registerTypeMapper(typeMapper);
    }

    private static <A,B> List<TypeMapper> create(@NonNull Class<A> aClass, @NonNull Class<B> bClass, @NonNull Function<A, B> bToAFn, @NonNull Function<B, A> aToBFn) {
        SimpleTypeMapper<B,A> bToATypeMapper = new SimpleTypeMapper<B,A>() {
            @Override
            public A map(MappingInfo<B, A> mappingInfo) {
                if (mappingInfo.getSourceType().getRawClass().equals(bClass) && mappingInfo.getTargetType().getRawClass().equals(aClass)) {
                    return aToBFn.apply(mappingInfo.getSource());
                }
                return null;
            }
        };
        bToATypeMapper.setSClass(bClass);
        bToATypeMapper.setTClass(aClass);

        SimpleTypeMapper<A,B> aToBTypeMapper = new SimpleTypeMapper<A,B>() {
            @Override
            public B map(MappingInfo<A,B> mappingInfo) {
                if (mappingInfo.getSourceType().getRawClass().equals(aClass) && mappingInfo.getTargetType().getRawClass().equals(bClass)) {
                    return bToAFn.apply(mappingInfo.getSource());
                }
                return null;
            }
        };
        aToBTypeMapper.setSClass(aClass);
        aToBTypeMapper.setTClass(bClass);
        List<TypeMapper> typeMappers = new ArrayList<>();
        typeMappers.add(aToBTypeMapper);
        typeMappers.add(bToATypeMapper);
        return typeMappers;
    }

    public static <S, T> T map(MappingInfo<S, T> mappingInfo) {
        List<TypeMapper> typeMappers = getMappers(mappingInfo);
        for (TypeMapper<S,T> typeMapper : typeMappers) {
            if (log.isDebugEnabled()) {
                log.debug("use {}",typeMapper);
            }
            T targetValue = typeMapper.map(mappingInfo);
            if (targetValue != null) {
                if (log.isDebugEnabled()) {
                    log.debug("after map.current value is {}",targetValue);
                }
                return targetValue;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <S, T> List<TypeMapper> getMappers(MappingInfo<S, T> mappingInfo) {
        return ALL_MAPPER.stream()
                .filter(e -> {
                    return e.match(mappingInfo);
                }).collect(Collectors.toList());
    }

    public static <S, T> List<TypeMapper> getAllMappers(MappingInfo<S, T> mappingInfo) {
        return ALL_MAPPER;
    }

    @SuppressWarnings("unchecked")
    public static <S, T> T map(S source, JavaType<T> targetType) {
        if (source == null) {
            return null;
        }
        if (targetType == null) {
            return null;
        }
        MappingInfo<S, T> mappingInfo = MappingInfo.of(source, targetType);
        return map(mappingInfo);
    }


    public static <T> T map(Object source, ClassReference<T> classReference) {
        return map(source, JavaType.from(classReference));
    }

    public static <T> T map(Object source, Class<T> targetClass) {
        return map(source, JavaType.from(targetClass));
    }

    public static <T> T map(Object source, Type targetClass) {
        return map(source, JavaType.from(targetClass));
    }

    @SuppressWarnings("unchecked")
    public static void populate(Object source, Object target) {
        if (source == null) {
            return;
        }
        if (target == null) {
            return;
        }
        map(MappingInfo.of(source, JavaType.from(target.getClass()), target));
    }
}
