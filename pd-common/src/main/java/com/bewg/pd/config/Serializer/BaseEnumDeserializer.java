package com.bewg.pd.config.Serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 
 * @author lizy
 */
public class BaseEnumDeserializer extends JsonDeserializer<BaseEnum> {
    @Override
    public BaseEnum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // JsonNode node = jp.getCodec().readTree(jp);
        // String currentName = jp.currentName();
        // Object currentValue = jp.getCurrentValue();
        // Class findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        // BaseEnum b = EnumUtil.likeValueOf(findPropertyType, node.intValue());
        return null;
    }

}
