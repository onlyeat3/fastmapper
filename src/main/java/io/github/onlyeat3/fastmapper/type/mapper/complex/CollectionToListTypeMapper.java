package io.github.onlyeat3.fastmapper.type.mapper.complex;

import io.github.onlyeat3.fastmapper.FastMapper;
import io.github.onlyeat3.fastmapper.MappingInfo;
import io.github.onlyeat3.fastmapper.type.annotation.Order;
import io.github.onlyeat3.fastmapper.type.mapper.TypeMapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(5000)
public class CollectionToListTypeMapper implements TypeMapper<List<Object>, List<Object>> {
    @Override
    public boolean match(MappingInfo<List<Object>, List<Object>> mappingInfo) {
        return mappingInfo.getSourceType().isCollectionLikeType() && mappingInfo.getTargetType().isListLikeType();
    }

    @Override
    public List<Object> map(MappingInfo<List<Object>, List<Object>> mappingInfo) {
        List<Object> targetList = new ArrayList<>();
        Type listComponentType = mappingInfo.getTargetType().getTypeBindings().firstValue();
        for (Object o : mappingInfo.getSource()) {
            Object t = FastMapper.map(o, listComponentType);
            if (t != null) {
                targetList.add(t);
            }
        }
        return targetList;
    }
}
