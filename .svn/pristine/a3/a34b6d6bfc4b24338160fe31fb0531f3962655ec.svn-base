package cn.com.cdboost.charge.webapi.dto;

import cn.com.cdboost.charge.webapi.dto.info.TreeEntityProject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 解析树形数据工具类
 */
public class TreeParserProject {
    /**
     * 解析树形数据
     * @param entityList
     * @param <E>
     * @return
     */
    public static <E extends TreeEntityProject<E>> List<E> getTreeList(List<E> topList, List<E> entityList) {
        List<E> resultList=new ArrayList<>();
        //获取顶层元素集合

        resultList.addAll(topList);

        //获取每个顶层元素的子数据集合
        for (E entity : resultList) {
            List<E> subList = getSubList(entity.getId(), entityList);
            entity.setChildList(subList);
            if (subList != null) {
                // 汇总用户总数
                Long total = 0L;
                for (E e : subList) {
                    total += e.getTotal();
                }
                entity.setTotal(total);
                entity.setHashChild(true);
            }
        }

        return resultList;
    }

    /**
     * 获取子数据集合
     * @param id
     * @param entityList
     * @param <E>
     * @return
     */
    private static <E extends TreeEntityProject<E>>  List<E> getSubList(String id, List<E> entityList) {
        List<E> childList=new ArrayList<>();
        String parentId;
        //子集的直接子对象
        for (  Iterator<E> iterator = entityList.iterator();iterator.hasNext();) {
            E entity = iterator.next();
            parentId=entity.getParentId();
            if(id.equals(parentId)){
                childList.add(entity);
                iterator.remove();
            }
        }

        //子集的间接子对象
        for (E entity : childList) {
            List<E> subList = getSubList(entity.getId(), entityList);
            entity.setChildList(subList);
            if (subList != null) {
                // 汇总用户总数
                Long total = 0L;
                for (E e : subList) {
                    total += e.getTotal();
                }
                entity.setHashChild(true);
                entity.setTotal(total);
            }
        }

        //递归退出条件
        if(childList.size()==0){
            return null;
        }
        return childList;
    }
}
