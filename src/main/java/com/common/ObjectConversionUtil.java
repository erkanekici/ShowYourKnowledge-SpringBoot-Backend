package com.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.controller.handler.SpecialDataTypes;
import com.entity.Topic;
import com.entity.User;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class ObjectConversionUtil {

    private static ObjectConversionUtil _this = null;

    public static ObjectConversionUtil getInstance() {
        if (_this == null)
            _this = new ObjectConversionUtil();
        return _this;
    }

    private static final Logger logger = LoggerFactory.getLogger(ObjectConversionUtil.class);

    public String serializeObject(Object data) {
        try {
            ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream ooOutputStream = new ObjectOutputStream(baOutputStream);
            ooOutputStream.writeObject(data);
            ooOutputStream.close();
            return Hex.encodeHexString(baOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public Object deserializeObject(String data) {
        try {
            byte[] bytes = Hex.decodeHex(data.toCharArray());
            if (bytes != null) {
                ObjectInputStream oInputStream = new ObjectInputStream(
                        new ByteArrayInputStream(bytes));
                return oInputStream.readObject();
            }
            return null;
        } catch (ClassNotFoundException | DecoderException | IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public String writeDataAsString(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public <T> T readDataByDataType(String data, String dataType) {
        if (SpecialDataTypes.isObjectDataType(dataType)) {
            try{
                if (SpecialDataTypes.USER_LIST.name().equals(dataType)) {
                    return (T) new ObjectMapper().readValue(data, new TypeReference<List<User>>(){});
                }
                if (SpecialDataTypes.TOPIC_LIST.name().equals(dataType)) {
                    return (T) new ObjectMapper().readValue(data, new TypeReference<List<Topic>>(){});
                }
            } catch (JsonProcessingException e){
                logger.error("ObjectConversionUtil ERROR - readDataByDataType - data: {}, dataType: {}, Exception: {}", data, dataType, e.getMessage());
                return null;
            }
        }
        return (T) data;
    }

}