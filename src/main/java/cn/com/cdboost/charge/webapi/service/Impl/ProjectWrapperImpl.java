package cn.com.cdboost.charge.webapi.service.Impl;


import cn.com.cdboost.charge.base.exception.BusinessException;
import cn.com.cdboost.charge.base.util.DateUtil;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.dubbo.CustomerToMerchantService;
import cn.com.cdboost.charge.customer.vo.info.UseDetailListInfo;
import cn.com.cdboost.charge.customer.vo.param.IncomePageQueryParam;
import cn.com.cdboost.charge.merchant.dubbo.PaySchemeService;
import cn.com.cdboost.charge.merchant.dubbo.ProjectService;
import cn.com.cdboost.charge.merchant.vo.info.*;
import cn.com.cdboost.charge.merchant.vo.param.ProjectQueryParam;
import cn.com.cdboost.charge.webapi.dto.info.*;
import cn.com.cdboost.charge.webapi.dto.param.ChargingSchemeDto;
import cn.com.cdboost.charge.webapi.dto.param.ProjectParam;
import cn.com.cdboost.charge.webapi.dto.param.ProjectSchemeQuery;
import cn.com.cdboost.charge.webapi.dto.param.SchemeEditParam;
import cn.com.cdboost.charge.webapi.service.ProjectWrapper;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.com.cdboost.charge.merchant.vo.dto.ProjectInfoDto;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 站点相关接口实现
 */
@Service
public class ProjectWrapperImpl implements ProjectWrapper {
    @Reference(version = "1.0")
    private ProjectService projectService;

    @Reference(version = "1.0")
    private PaySchemeService paySchemeService;

    @Reference(version = "1.0")
    private CustomerToMerchantService customerToMerchantService;

    @Override
    public void addProject(ProjectParam projectParam) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(projectParam,projectVo);
        projectService.addProject(projectVo);
    }

    @Override
    public void updateProject(ProjectParam projectParam) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(projectParam,projectVo);
        projectService.updateProject(projectVo);
    }

    @Override
    public void deleteProjectByProjectGuids(List<String> projectGuids) {
        projectService.deleteProjectByProjectGuids(projectGuids);
    }

    @Override
    public PageData<ProjectResp> queryProjects(ProjectSchemeQuery param) {
        ChargerSchemeQueryVo queryParam = new ChargerSchemeQueryVo();
        BeanUtils.copyProperties(param,queryParam);
        PageData pageData = projectService.queryList(queryParam,param.getUserId());
        return pageData;
    }

    @Override
    public ProjectDetailResp queryProjectDetail(String projectGuid) {
        ProjectDetailResp resp = new ProjectDetailResp();
        ProjectVo projectVo = projectService.queryProjectDetail(projectGuid);
        BeanUtils.copyProperties(projectVo,resp);
        return resp;
    }

    @Override
    public void offOnScheme(String schemeGuid, Integer onOrOff) {
        paySchemeService.offOnScheme(schemeGuid,onOrOff);
    }

    @Override
    public void editScheme(SchemeEditParam param) {
        SchemeEditVo schemeEditVo = new SchemeEditVo();
        List<ChargingSchemeVo> schemeList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(param.getSchemeList())){
            for (ChargingSchemeDto chargingSchemeDto : param.getSchemeList()) {
                ChargingSchemeVo vo = new ChargingSchemeVo();
                BeanUtils.copyProperties(chargingSchemeDto,vo);
                schemeList.add(vo);
            }
        }
        schemeEditVo.setSchemeList(schemeList);
        paySchemeService.editScheme(schemeEditVo);
    }

    @Override
    public List<SchemePofitableResp> countProfitable(ProjectSchemeQuery queryVo) {
        //转换成开始和结束时间
        this.getTime(queryVo);
        List<SchemePofitableResp> respList = Lists.newArrayList();
        // 客户服务查询使用记录
        List<UseDetailListInfo> useDetailListInfos = customerToMerchantService.querySchemeUseList(queryVo.getProjectGuid(),queryVo.getBeginTime(),queryVo.getEndTime());
        BeanUtils.copyProperties(useDetailListInfos,respList);
        ProjectVo projectVo = projectService.queryProjectDetail(queryVo.getProjectGuid());
        //查询方案信息
        List<ChargerSchemeVo> chargerSchemeVos = paySchemeService.querySchemeList(queryVo.getProjectGuid(), projectVo.getSchemeType());
        Map<String,cn.com.cdboost.charge.merchant.vo.info.ChargingSchemeDto> chargerSchemeVoMap = Maps.newHashMap();
        List<cn.com.cdboost.charge.merchant.vo.info.ChargingSchemeDto> chargingSchemeDtos = Lists.newArrayList();
        for (ChargerSchemeVo chargerSchemeVo : chargerSchemeVos) {
            chargingSchemeDtos.addAll(chargerSchemeVo.getSchemeList());
        }
        for (cn.com.cdboost.charge.merchant.vo.info.ChargingSchemeDto chargingSchemeDto : chargingSchemeDtos) {
            chargerSchemeVoMap.put(chargingSchemeDto.getSchemeGuid(),chargingSchemeDto);
        }
        for (SchemePofitableResp schemePofitableResp : respList) {
            cn.com.cdboost.charge.merchant.vo.info.ChargingSchemeDto chargingSchemeDto = chargerSchemeVoMap.get(schemePofitableResp.getSchemeGuid());
            schemePofitableResp.setMoney(chargingSchemeDto.getRealMoney());
            schemePofitableResp.setChargingTime(chargingSchemeDto.getChargingTime());
            schemePofitableResp.setPayCategory(chargingSchemeDto.getPayCategory());
        }
        return respList;
    }

    /**
     * 将前端传的统计类型转换成开始和结束时间
     * @param queryVo
     * @return
     */
    private ProjectSchemeQuery getTime(ProjectSchemeQuery queryVo){
        Integer countType = queryVo.getCountType();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        c.setTime(now);
        queryVo.setEndTime(DateUtil.formatDate(now));
        switch (countType){
            case 1:
                //过去七天
                c.add(Calendar.DATE, - 7);
                Date d = c.getTime();
                String day = format.format(d);
                queryVo.setBeginTime(day);
                break;
            case 2:
                //过去一月
                c.add(Calendar.MONTH, -1);
                Date m = c.getTime();
                String mon = format.format(m);
                queryVo.setBeginTime(mon);
                break;
            case 3:
                //过去三个月
                c.add(Calendar.MONTH, -3);
                Date m3 = c.getTime();
                String mon3 = format.format(m3);
                queryVo.setBeginTime(mon3);
        }
        return queryVo;
    }

    @Override
    public PageData<UseDetailListInfo> shemeUseList(ProjectSchemeQuery queryVo) {
        if (StringUtil.isEmpty(queryVo.getSchemeGuid())){
            throw new BusinessException("schemeGuid不能为空");
        }
        //转换成开始和结束时间
        this.getTime(queryVo);
        IncomePageQueryParam param = new IncomePageQueryParam();
        BeanUtils.copyProperties(queryVo,param);
        PageData<UseDetailListInfo> useDetailListInfoPageData = customerToMerchantService.queryUseDetailList(param);
        /*PageData<ChargingUseDetailedResp> pageData = new PageData<>();
        if (!CollectionUtils.isEmpty(useDetailListInfoPageData.getList())){
            List<ChargingUseDetailedResp> respList = Lists.newArrayList();
            for (UseDetailListInfo info : useDetailListInfoPageData.getList()) {
                ChargingUseDetailedResp resp = new ChargingUseDetailedResp();
                BeanUtils.copyProperties(info,resp);
                respList.add(resp);
            }
            pageData.setList(respList);
            pageData.setTotal(useDetailListInfoPageData.getTotal());
        }*/
        return useDetailListInfoPageData;
    }

    @Override
    public PageData<UseDetailListInfo> projectUseList(ProjectSchemeQuery queryVo) {
        if (StringUtil.isEmpty(queryVo.getProjectGuid())){
            throw new BusinessException("projectGuid不能为空");
        }
        IncomePageQueryParam param = new IncomePageQueryParam();
        BeanUtils.copyProperties(queryVo,param);
        PageData<UseDetailListInfo> useDetailListInfoPageData = customerToMerchantService.queryUseDetailList(param);
        return useDetailListInfoPageData;
    }

    @Override
    public List<ChargerSchemeResp> shemeList(ProjectSchemeQuery queryVo) {
        List<ChargerSchemeVo> chargerSchemeVos = paySchemeService.querySchemeList(queryVo.getProjectGuid(), queryVo.getSchemeType());
        List<ChargerSchemeResp> respList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(chargerSchemeVos)){
            for (ChargerSchemeVo chargerSchemeVo : chargerSchemeVos) {
                ChargerSchemeResp resp = new ChargerSchemeResp();
                BeanUtils.copyProperties(chargerSchemeVo,resp);
                if (!CollectionUtils.isEmpty(chargerSchemeVo.getSchemeList())){
                    List<ChargingSchemeDto> chargingSchemeDtos = Lists.newArrayList();
                    for (cn.com.cdboost.charge.merchant.vo.info.ChargingSchemeDto chargingSchemeDto : chargerSchemeVo.getSchemeList()) {
                        ChargingSchemeDto dto = new ChargingSchemeDto();
                        BeanUtils.copyProperties(chargingSchemeDto,dto);
                        chargingSchemeDtos.add(dto);
                    }
                    resp.setSchemeList(chargingSchemeDtos);
                    respList.add(resp);
                }
            }
        }
        return respList;
    }

    @Override
    public List<ProjectResp> queryAllProject(Integer userId) {
        List<ProjectVo> projectVos = projectService.queryAllProject(userId);
        List<ProjectResp>projectResps= Lists.newArrayList();
        for (ProjectVo projectVo : projectVos) {
            ProjectResp resp=new ProjectResp();
            BeanUtils.copyProperties(projectVo,resp);
            projectResps.add(resp);
        }
        return projectResps;
    }

    @Override
    public List<ProjectInfoDto> queryProjectTreeByName(Set<Long> dataOrgNos, String projectName) {
        return projectService.queryProjectTreeByName(dataOrgNos, projectName);
    }
}
