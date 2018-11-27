package cn.com.cdboost.charge.webapi.service.Impl;

import cn.com.cdboost.charge.base.exception.BusinessException;
import cn.com.cdboost.charge.merchant.vo.dto.ProjectInfoDto;
import cn.com.cdboost.charge.merchant.vo.dto.ProjectQueryParam;
import cn.com.cdboost.charge.user.dto.info.OrgInfo;
import cn.com.cdboost.charge.user.dubbo.OrgService;
import cn.com.cdboost.charge.webapi.dto.TreeParserProject;
import cn.com.cdboost.charge.webapi.dto.info.ProjectTreeInfo;
import cn.com.cdboost.charge.webapi.dto.param.ProjectTreeParam;
import cn.com.cdboost.charge.webapi.service.ProjectWrapper;
import cn.com.cdboost.charge.webapi.service.TreeServiceWrapper;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Function;
import com.google.common.collect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 树形服务接口实现类
 */
@Service
public class TreeServiceImpl implements TreeServiceWrapper {
    @Autowired
    private ProjectWrapper projectWrapper;

    @Reference(version = "1.0")
    OrgService orgService;

    @Override
    public List<ProjectTreeInfo> projectfuzzyQueryTree(ProjectTreeParam projectTreeParam) throws BusinessException {
        // 查询用户的组织数据权限

        List<Long> dataOrgNos = orgService.queryDataOrgList(projectTreeParam.getUserId());
        // 查询项目信息
        List<ProjectInfoDto> projectInfoDtos = projectWrapper.queryProjectTreeByName(new HashSet<>(dataOrgNos),projectTreeParam.getProjectName());
        // 查询项目信息
        Set<Long> orgNoSet=Sets.newHashSet();
        for (ProjectInfoDto projectInfoDto : projectInfoDtos) {
            orgNoSet.add(projectInfoDto.getOrgNo());
        }
        // 需要将当前组织所在的父组织都查出来
        Set<Long> parentOrgSet = Sets.newHashSet();
        for (Long orgNo : orgNoSet) {
            List<OrgInfo> orgs = orgService.queryParent(orgNo);
            List<Long> orgList = Lists.newArrayList();
            for (OrgInfo org : orgs) {
                orgList.add(org.getOrgNo());
            }

            // 加入返回列表
            parentOrgSet.addAll(orgList);
        }
        // 查询组织所在树的所有节点
        parentOrgSet.addAll(orgNoSet);
        parentOrgSet.retainAll(dataOrgNos);
        List<OrgInfo> orgCacheVos = orgService.batchQueryByOrgNos(parentOrgSet);

        List<ProjectTreeInfo> treeInfos = this.convertProjectTree(orgCacheVos);
        f1: for (ProjectTreeInfo treeInfo : treeInfos) {
            for (ProjectInfoDto projectInfoDto : projectInfoDtos) {
                if(treeInfo.getNodeId().equals(String.valueOf(projectInfoDto.getOrgNo()))){
                    treeInfo.setHasChild(true);
                    continue f1;
                }
            }
        }
        // 按orgNo分组
        ImmutableListMultimap<Long, ProjectInfoDto> buildInfoMap = Multimaps.index(projectInfoDtos, new Function<ProjectInfoDto, Long>() {
            @Nullable
            @Override
            public Long apply(@Nullable ProjectInfoDto projectInfoDto) {
                return projectInfoDto.getOrgNo();
            }
        });

        // 构造最末级节点
        List<ProjectTreeInfo> buildList = Lists.newArrayList();
        for (Map.Entry<Long, Collection<ProjectInfoDto>> entry : buildInfoMap.asMap().entrySet()) {
            Long key = entry.getKey();
            Collection<ProjectInfoDto> value = entry.getValue();
            for (ProjectInfoDto infoDto : value) {
                ProjectTreeInfo treeInfo = new ProjectTreeInfo();
                treeInfo.setNodeType(2);
                treeInfo.setNodeName(infoDto.getProjectName());
                treeInfo.setpNodeNo(String.valueOf(key));
                treeInfo.setNodeId(infoDto.getProjectGuid());
                treeInfo.setProjectHolds(infoDto.getProjectHolds());
                treeInfo.setTotal(1L);
                treeInfo.setHasChild(false);
                buildList.add(treeInfo);
            }
        }

        if(treeInfos.size()==0){
          return null;
        }
        List<ProjectTreeInfo> projectTreeInfos = getProjectTreeInfos(treeInfos);
        // 合并
        treeInfos.addAll(buildList);
        // 树形结构返回
        List<ProjectTreeInfo> treeList = TreeParserProject.getTreeList(projectTreeInfos, treeInfos);
        return treeList;
    }

    @Override
    public List<ProjectTreeInfo> projectTreeMainSubTree(ProjectQueryParam projectQueryParam) {
        Set<Long> dataOrgNos = new HashSet(Collections.singleton(projectQueryParam.getNodeId()));
        // 查询项目信息
        List<ProjectInfoDto> projectInfoDtos = projectWrapper.queryProjectTreeByName(dataOrgNos,null);

        // 按orgNo分组
        ImmutableListMultimap<Long, ProjectInfoDto> buildInfoMap = Multimaps.index(projectInfoDtos, new Function<ProjectInfoDto, Long>() {
            @Nullable
            @Override
            public Long apply(@Nullable ProjectInfoDto projectInfoDto) {
                return projectInfoDto.getOrgNo();
            }
        });

        // 构造最末级节点
        List<ProjectTreeInfo> buildList = Lists.newArrayList();
        for (Map.Entry<Long, Collection<ProjectInfoDto>> entry : buildInfoMap.asMap().entrySet()) {
            Long key = entry.getKey();
            Collection<ProjectInfoDto> value = entry.getValue();
            for (ProjectInfoDto infoDto : value) {
                ProjectTreeInfo treeInfo = new ProjectTreeInfo();
                treeInfo.setNodeType(2);
                treeInfo.setNodeName(infoDto.getProjectName());
                treeInfo.setpNodeNo(String.valueOf(key));
                treeInfo.setNodeId(infoDto.getProjectGuid());
                treeInfo.setProjectHolds(infoDto.getProjectHolds());
                treeInfo.setTotal(1L);
                treeInfo.setHasChild(false);
                buildList.add(treeInfo);
            }
        }

        // 合并
        //treeInfos.addAll(buildList);
        // 树形结构返回
       // List<ProjectTreeInfo> treeList = TreeParserProject.getTreeList("0", treeInfos);
       // return treeList;
        return  buildList;
    }

    @Override
    public List<ProjectTreeInfo> queryProjectTree(Integer userId) {
        // 查询用户的组织数据权限
        List<Long> dataOrgNos = orgService.queryDataOrgList(userId);
        // 查询项目信息
        List<ProjectInfoDto> projectInfoDtos = projectWrapper.queryProjectTreeByName(new HashSet<>(dataOrgNos),null);

        // 查询组织所在树的所有节点
        Set<OrgInfo> orgCacheVos = queryAllTreeNode(new HashSet<>(dataOrgNos));

        // 转换成前端需要的值
        List<OrgInfo> orgCacheList = Lists.newArrayList(orgCacheVos);

        List<ProjectTreeInfo> treeInfos = this.convertProjectTree(orgCacheList);
       f1: for (ProjectTreeInfo treeInfo : treeInfos) {
           treeInfo.setOpen(false);
            for (ProjectInfoDto projectInfoDto : projectInfoDtos) {
                   if(treeInfo.getNodeId().equals(String.valueOf(projectInfoDto.getOrgNo()))){
                       treeInfo.setHasChild(true);
                       continue f1;
                   }
            }
        }
        List<ProjectTreeInfo> projectTreeInfos = getProjectTreeInfos(treeInfos);
        List<ProjectTreeInfo> treeList = TreeParserProject.getTreeList(projectTreeInfos, treeInfos);
        return treeList;
    }

    private List<ProjectTreeInfo> getProjectTreeInfos(List<ProjectTreeInfo> treeInfos) {
        ImmutableListMultimap<Integer,ProjectTreeInfo> immutableListMultimap= Multimaps.index(treeInfos, new Function<ProjectTreeInfo, Integer>() {

            @Nullable
            @Override
            public Integer apply(@Nullable ProjectTreeInfo projectTreeInfo) {
                return projectTreeInfo.getLevel();
            }
        });
        ImmutableSet<Integer> integers = immutableListMultimap.asMap().keySet();
        List<Integer> integers1 = Lists.newArrayList(integers);
        integers1.sort(Integer::compareTo);
        Integer first = integers1.iterator().next();
        List<ProjectTreeInfo> projectTreeInfos = new ArrayList<>(immutableListMultimap.asMap().get(first));
        // 树形结构返回
        treeInfos.removeAll(projectTreeInfos);
        return projectTreeInfos;
    }
    private List<ProjectTreeInfo> convertProjectTree(List<OrgInfo> orgs) {
        List<ProjectTreeInfo> dataList = Lists.newArrayList();
        for (OrgInfo org : orgs) {
            ProjectTreeInfo  info = new ProjectTreeInfo();
            info.setNodeId(String.valueOf(org.getOrgNo()));
            info.setpNodeNo(String.valueOf(org.getPOrgNo()));
            info.setNodeName(org.getOrgName());
            info.setNodeType(1);
            info.setOpen(true);
            info.setLevel(org.getLevel());
            dataList.add(info);
        }
        return dataList;
    }
    public Set<OrgInfo> queryAllTreeNode(Set<Long> orgNoSet) {
        Set<OrgInfo> dataSet = Sets.newHashSet();
        for (Long orgNo : orgNoSet) {
            List<OrgInfo> orgCacheVoList = this.queryOrgTree(orgNo);
            dataSet.addAll(orgCacheVoList);
        }
        return dataSet;
    }
    public List<OrgInfo> queryOrgTree(Long orgNo) {
        List<OrgInfo> orgs = orgService.queryTreeByOrgNo(orgNo);
        return orgs;
    }

}
