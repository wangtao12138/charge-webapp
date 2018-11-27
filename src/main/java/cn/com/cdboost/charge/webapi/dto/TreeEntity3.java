package cn.com.cdboost.charge.webapi.dto;

import java.util.List;

/**
 * 树形数据实体接口
 */
public interface TreeEntity3<E> {
    /**
     * 获取自身id
     * @return
     */
    Long getId();

    /**
     * 获取父节点id
     * @return
     */
    Long getParentId();

    /**
     * 设置孩子节点列表
     * @param childList
     */
    void setChildList(List<E> childList);

    /**
     * 设置拥有的户数
     * @param num
     */
    void setTotal(Long num);

    /**
     * 获取拥有的户数
     * @return
     */
    Long getTotal();
}
