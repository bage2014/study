package com.bage.study.shadow;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemRepository {
    /**
     * 创建表
     */
    void createTableIfNotExists();

    /**
     * 删除表
     */
    void dropTable();

    /**
     * 清空数据
     */
    void deleteTable();

    /**
     * 插入
     *
     * @param entity
     * @return
     */
    int insert(OrderItem entity);

    /**
     * 删除
     *
     * @param orderId
     */
    int delete(Long orderId);

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    int update(OrderItem entity);

    /**
     * 查询
     *
     * @param orderId
     * @return
     */
    OrderItem query(Long orderId);

    /**
     * 分页查询
     *
     * @param condition
     * @param targetPage
     * @param pageSize
     * @return
     */
    List<OrderItem> queryByPage(String condition, int targetPage, int pageSize);

}
