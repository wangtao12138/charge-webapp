package cn.com.cdboost.charge.webapi.dto.info;

import java.util.List;

/**
 * 树形数据实体接口
 */
public interface TreeEntityProject<E> {

    /**
     * 获取层级深度
     * @return
     */
    Integer getLevel();

    /**
     * 获取自身id
     * @return
     */
    String getId();

    /**
     * 获取父节点id
     * @return
     */
    String getParentId();

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

    void setHashChild(Boolean hashChild);
}
