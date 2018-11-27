package cn.com.cdboost.charge.webapi.dto.info;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 智慧应用，用户选择项目的树形结构
 */
public class ProjectTreeInfo implements TreeEntityProject<ProjectTreeInfo> {
    /**
     * 节点类型 1标识组织，2标识项目
     */
    private Integer nodeType;

    /**
     * 节点编号
     * nodeType=1时，表示组织编号
     * nodeType=2时，表示项目projectGuid
     */
    private String nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 上级节点编号
     */
    private String pNodeNo;

    /**
     * 对应节点下拥有的项目数,默认设置为0
     */
    private Long projectHolds = 0L;
    /**
     * 是否有孩子节点
     */
    private Boolean hasChild=false ;
    /**
     * 是否展开
     */
    private Boolean open=false;

    /**
     * 该节点的孩子节点
     */
    @JSONField(serialize = false)
    private Integer level;

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    private List<ProjectTreeInfo> children;

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }


    @Override
    @JSONField(serialize=false)
    public String getId() {
        return nodeId;
    }

    @Override
    @JSONField(serialize=false)
    public String getParentId() {
        return pNodeNo;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public void setChildList(List<ProjectTreeInfo> childList) {
        this.children = childList;
    }

    @Override
    public void setTotal(Long num) {
        this.projectHolds = num;
    }

    @Override
    @JSONField(serialize=false)
    public Long getTotal() {
        return projectHolds;
    }

    @Override
    public void setHashChild(Boolean hashChild) {
        this.hasChild=hashChild;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public Long getProjectHolds() {
        return projectHolds;
    }

    public void setProjectHolds(Long projectHolds) {
        this.projectHolds = projectHolds;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getpNodeNo() {
        return pNodeNo;
    }

    public void setpNodeNo(String pNodeNo) {
        this.pNodeNo = pNodeNo;
    }

    public List<ProjectTreeInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectTreeInfo> children) {
        this.children = children;
    }
}
