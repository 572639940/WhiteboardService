package com.whiteboard.common.dozer.service;

import com.google.common.collect.Lists;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className EJBGenerator
 * @since 2022/11/2 00:04
 */
public class EJBGenerator implements IGenerator {

    @Autowired
    protected Mapper dozerMapper;

    @Override
    public <T, L> List<T> convert(List<L> l, Class<T> clz) {
        return CollectionUtils.isEmpty(l) ? Lists.newArrayList()
            : l.stream().map(vs -> this.dozerMapper.map(vs, clz)).collect(Collectors.toList());
    }
}
