package cn.com.cdboost.charge.webapi.dto.info;

import cn.com.cdboost.charge.webapi.dto.TreeEntity3;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 用户档案添加时，用户选择楼栋的树形结构
 */
public class BuildingTreeInfo implements TreeEntity3<BuildingTreeInfo> {
    /**
     * 节点类型 1标识组织，2标识楼栋
     */
    private Integer nodeType;

    /**
     * 节点编号
     * nodeType=1时，表示组织编号
     * nodeType=2时，表示楼栋静态字典表value
     */
    private Long nodeNo;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 上级节点编号
     */
    private Long pNodeNo;

    /**
     * 对应节点下拥有的户数,默认设置为0
     */
    private Long houseHolds = 0L;

    /**
     * 该节点的孩子节点
     */
    private List<BuildingTreeInfo> children;

    @Override
    @JSONField(serialize=false)
    public Long getId() {
        return nodeNo;
    }

    @Override
    @JSONField(serialize=false)
    public Long getParentId() {
        return pNodeNo;
    }

    @Override
    public void setChildList(List<BuildingTreeInfo> childList) {
        this.children = childList;
    }

    @Override
    public void setTotal(Long num) {
        this.houseHolds = num;
    }

    @Override
    @JSONField(serialize=false)
    public Long getTotal() {
        return houseHolds;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public Long getNodeNo() {
        return nodeNo;
    }

    public void setNodeNo(Long nodeNo) {
        this.nodeNo = nodeNo;
    }

    public String getNodeName() {
        return nodeName + "(" + houseHolds + "户)";
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Long getpNodeNo() {
        return pNodeNo;
    }

    public void setpNodeNo(Long pNodeNo) {
        this.pNodeNo = pNodeNo;
    }

    public Long getHouseHolds() {
        return houseHolds;
    }

    public void setHouseHolds(Long houseHolds) {
        this.houseHolds = houseHolds;
    }

    public List<BuildingTreeInfo> getChildren() {
        return children;
    }

    public void setChildren(List<BuildingTreeInfo> children) {
        this.children = children;
    }
}
