package com.zzz.o2o.service;

import com.zzz.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {
    List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;
}
