package com.whiteboard.common.dozer.service;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className IGenerator
 * @since 2022/11/2 00:03
 */
public interface IGenerator {

    /**
     * 转换
     *
     * @param l 数据对象
     * @param clz 复制目标类型
     * @return {@link List <T>}
     * @Description: list深度复制
     * @author tangzb
     * @Time 2022/11/2 00:03
     */
    <T, L> List<T> convert(List<L> l, Class<T> clz);
}
