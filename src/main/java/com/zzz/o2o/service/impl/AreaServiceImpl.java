package com.zzz.o2o.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzz.o2o.cache.JedisUtil;
import com.zzz.o2o.dao.AreaDao;
import com.zzz.o2o.entity.Area;
import com.zzz.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService{
private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
@Autowired
private AreaDao areaDao;
@Autowired
private JedisUtil.Keys jedisKeys;
@Autowired
private JedisUtil.Strings jedisStrings;
private static String AREALISTKEY = "areaList";
    public List<Area  > getAreaList() {
        String key ="AREALISTKEY";
        List<Area> areaList = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (!jedisKeys.exists(key)) {
                areaList = areaDao.queryArea();
                String jsonString;
                try {
                    jsonString = mapper.writeValueAsString(areaList);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
                jedisStrings.set(key, jsonString);
            } else {
                String jsonString = jedisStrings.get(key);
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
                try {
                    areaList = mapper.readValue(jsonString, javaType);
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logger.info("---------连接redis成功，从radis取数据--------");
            return areaList;
        }catch(JedisConnectionException e) {
            logger.info("---------连接redis失败，从mysql取数据--------");
            return areaDao.queryArea();
        }
    }
}
