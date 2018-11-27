package cn.com.cdboost.charge.webapi.service.Impl;

import cn.com.cdboost.charge.base.util.DateUtil;
import cn.com.cdboost.charge.base.util.MathUtil;
import cn.com.cdboost.charge.customer.dubbo.CustomerToMerchantService;
import cn.com.cdboost.charge.customer.vo.param.ChargerDeviceQueryVo;
import cn.com.cdboost.charge.merchant.dubbo.ProjectService;
import cn.com.cdboost.charge.merchant.vo.info.ChargerSchemeQueryVo;
import cn.com.cdboost.charge.merchant.vo.info.ProjectVo;
import cn.com.cdboost.charge.statistic.dubbo.StatisticService;
import cn.com.cdboost.charge.statistic.vo.dto.ListElectricDto;
import cn.com.cdboost.charge.statistic.vo.dto.PowerAndFeeDto;
import cn.com.cdboost.charge.statistic.vo.dto.ProjectUseCountDto;
import cn.com.cdboost.charge.statistic.vo.dto.ProjectUseCountListDto;
import cn.com.cdboost.charge.statistic.vo.info.ProjectUseCountQueryVo;
import cn.com.cdboost.charge.user.dto.info.OrgInfo;
import cn.com.cdboost.charge.user.dubbo.OrgService;
import cn.com.cdboost.charge.webapi.dto.info.DeviceUseCountResp;
import cn.com.cdboost.charge.webapi.dto.info.PowerAndFeeCountResp;
import cn.com.cdboost.charge.webapi.dto.param.ElectricCountQueryParam;
import cn.com.cdboost.charge.webapi.service.ElectricFeeWrapper;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Function;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ElectricFeeWrapperImpl implements ElectricFeeWrapper {

    @Reference(version = "1.0")
    private CustomerToMerchantService customerToMerchantService;

    @Reference(version = "1.0")
    private StatisticService statisticService;

    @Reference(version = "1.0")
    private ProjectService projectService;

    @Reference(version = "1.0")
    private OrgService orgService;

    @Override
    public PowerAndFeeCountResp queryPowerAndFeeCount(ElectricCountQueryParam param) {
        PowerAndFeeCountResp powerAndFeeCountResp = new PowerAndFeeCountResp();
        ChargerSchemeQueryVo chargerSchemeQueryVo = new ChargerSchemeQueryVo();
        BeanUtils.copyProperties(param,chargerSchemeQueryVo);
        List<ProjectVo> projectVos = this.getTreeProject(chargerSchemeQueryVo);

        List<String> projectGuids = Lists.newArrayList();
        List<String> meterNos = Lists.newArrayList();
        for (ProjectVo projectVo : projectVos) {
            projectGuids.add(projectVo.getProjectGuid());
            meterNos.add(projectVo.getMeterNo());
        }
        if(param.getMark() != null){
            //设置年份
            int year = Integer.parseInt(param.getMark().substring(0,4));
            //设置月份
            int month = Integer.parseInt(param.getMark().substring(5,7));
            int monthMaxDay = DateUtil.getMonthMaxDay(year, month);

            // 按月查询
            String monthBeginTime = DateUtil.getMonthBeginTime(year, month);
            String monthEndTime = DateUtil.getMonthEndTime(year, month);
            List<ListElectricDto> list = statisticService.queryMonthMaterElectric(monthBeginTime, monthEndTime, meterNos);

            List<String> xData = Lists.newArrayList();
            List<BigDecimal> yFeesData  = Lists.newArrayList();
            List<BigDecimal> yQuantityData  = Lists.newArrayList();
            if (CollectionUtils.isEmpty(list)) {
                // 自己组装横坐标，纵坐标
                BigDecimal totalFee = BigDecimal.ZERO;
                BigDecimal totalquantity = BigDecimal.ZERO;
                for (int i = 1; i <= monthMaxDay; i++) {
                    String key = String.valueOf(i);
                    if (i < 10) {
                        key = "0" + i;
                    }
                    xData.add(key);
                    yFeesData.add(totalFee);
                    yQuantityData.add(totalquantity);
                }
            } else {
                // 按天分组
                ImmutableMap<String, ListElectricDto> multimap = Maps.uniqueIndex(list, new Function<ListElectricDto, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable ListElectricDto listElectricDto) {
                        return listElectricDto.getDayStr();
                    }
                });

                // 按天统计电费，电量
                for (int i = 1; i <= monthMaxDay; i++) {
                    String key = String.valueOf(i);
                    if (i < 10) {
                        key = "0" + i;
                    }
                    ListElectricDto listElectricDto = multimap.get(key);
                    /*BigDecimal totalFee = BigDecimal.ZERO;
                    BigDecimal totalquantity = BigDecimal.ZERO;
                    for (ListElectricDto listElectricDto : listElectricDtos) {
                        totalFee = totalFee.add(listElectricDto.getYFeesData());
                        //totalFee = totalFee.add(MathUtil.setPrecision(listElectricDto.getyQuantityData().multiply(new BigDecimal(0.82))));
                        totalquantity = totalquantity.add(listElectricDto.getYQuantityData());
                    }*/

                    xData.add(key);
                    yFeesData.add(listElectricDto.getElecMoney());
                    yQuantityData.add(listElectricDto.getYQuantityData());
                }
            }

            //汇总该月
            param.setBeginTime(monthBeginTime);
            param.setEndTime(monthEndTime);
            PowerAndFeeDto powerAndFeeDto = new PowerAndFeeDto();
            BigDecimal meterUsePower = new BigDecimal(0);
            BigDecimal meterUseMoney = new BigDecimal(0);
            BigDecimal profitMoney = new BigDecimal(0);
            for (ListElectricDto listElectricDto : list) {
                meterUsePower = meterUsePower.add(listElectricDto.getYQuantityData());
                meterUseMoney = meterUseMoney.add(listElectricDto.getYFeesData());
                profitMoney = profitMoney.add(listElectricDto.getProfitMoney());
            }
            /*for (BigDecimal userPower : yQuantityData) {
                meterUsePower = meterUsePower.add(userPower);
            }
            for (BigDecimal userMoney : yFeesData) {
                meterUseMoney = meterUseMoney.add(userMoney);
            }*/

            if (powerAndFeeDto != null){
                powerAndFeeDto.setMeterUsePower(meterUsePower);
                powerAndFeeDto.setMeterUseMoney(MathUtil.setPrecision(meterUseMoney));
                powerAndFeeDto.setMeterProfit(profitMoney);
                BeanUtils.copyProperties(powerAndFeeDto,powerAndFeeCountResp);
            }

            powerAndFeeCountResp.setxData(xData);
            powerAndFeeCountResp.setyFeesData(yFeesData);
            powerAndFeeCountResp.setyQuantityData(yQuantityData);
        }
        return powerAndFeeCountResp;
    }

    //判断点击树逻辑得到站点集合
    private List<ProjectVo> getTreeProject(ChargerSchemeQueryVo param){
        List<ProjectVo> projectVos = Lists.newArrayList();
        if (param.getNodeType() != null){//判断是否点击树
            if (param.getNodeType() == 1){//判断点击树上的组织或站点：1--组织；2--站点
                List<Long> childOrgNos = orgService.queryChildren(Long.valueOf(param.getNodeId()));
                param.setOrgNoList(childOrgNos);
                List<ProjectVo> projectVos1 = projectService.batchQueryProject(param.getOrgNoList(),param.getProjectName());
                projectVos.addAll(projectVos1);
            }else if (param.getNodeType() == 2){
                List<Long> childOrgNos = orgService.queryChildren(Long.valueOf(param.getNodeId()));
                param.setOrgNoList(childOrgNos);
                ProjectVo projectVo = projectService.queryProjectDetail(param.getNodeId());
                projectVos.add(projectVo);
            }
        }else {
            List<ProjectVo> projectVos1 = projectService.batchQueryProject(param.getOrgNoList(),param.getProjectName());
            projectVos.addAll(projectVos1);
        }
        return projectVos;
    }
    @Override
    public ProjectUseCountDto projectUseCountList(ChargerSchemeQueryVo param) {
        ProjectUseCountQueryVo queryVo = new ProjectUseCountQueryVo();
        List<ProjectVo> projectVos = this.getTreeProject(param);
        BeanUtils.copyProperties(param,queryVo);
        Map<String,ProjectVo> projectVoMap = Maps.newHashMap();
        List<String> projectGuids = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(projectVos)){
            for (ProjectVo projectVo : projectVos) {
                projectVoMap.put(projectVo.getProjectGuid(),projectVo);
                projectGuids.add(projectVo.getProjectGuid());
            }
        }
        queryVo.setProjectGuids(projectGuids);
        ProjectUseCountDto projectUseCountDto = statisticService.projectUseCountList(queryVo);

        //查询站点所属组织
        List<OrgInfo> orgInfos = orgService.batchQuery(param.getOrgNoList());
        Map<String,OrgInfo> orgInfoMap = Maps.newHashMap();
        for (OrgInfo orgInfo : orgInfos) {
            orgInfoMap.put(orgInfo.getOrgName(),orgInfo);
        }
        List<ProjectUseCountListDto> list = projectUseCountDto.getPageData().getList();
        if (!CollectionUtils.isEmpty(list)){
            for (ProjectUseCountListDto projectUseCountListDto : list) {
                ProjectVo projectVo = projectVoMap.get(projectUseCountListDto.getProjectGuid());
                OrgInfo orgInfo = orgInfoMap.get(projectVo.getOrgNo());
                projectUseCountListDto.setOrgName(orgInfo.getOrgName());
                //TODO 设备使用率
                //projectUseCountListDto.setProjectDeviceUseRate(projectUseCountListDto.getChargingNum()/);
            }
        }
        return projectUseCountDto;
    }

    @Override
    public DeviceUseCountResp deviceUseCountList(ChargerDeviceQueryVo queryVo) {
        return null;
    }
}
