package cn.com.cdboost.charge.webapi.service;

import cn.com.cdboost.charge.base.exception.BusinessException;
import cn.com.cdboost.charge.merchant.vo.dto.ProjectQueryParam;
import cn.com.cdboost.charge.webapi.dto.info.ProjectTreeInfo;
import cn.com.cdboost.charge.webapi.dto.param.ProjectTreeParam;

import java.util.List;

/**
 * 树形服务接口
 */
public interface TreeServiceWrapper {
    /**
     * 项目树模糊查询
     * @return
     * @throws BusinessException
     */
    List<ProjectTreeInfo> projectfuzzyQueryTree(ProjectTreeParam projectTreeParam) throws BusinessException;

    List<ProjectTreeInfo> projectTreeMainSubTree(ProjectQueryParam projectQueryParam);

    List<ProjectTreeInfo> queryProjectTree(Integer userId);

}
