package cn.com.cdboost.charge.webapi.service.Impl;

import cn.com.cdboost.charge.base.exception.BusinessException;
import cn.com.cdboost.charge.base.info.Afn19Object;
import cn.com.cdboost.charge.base.info.Afn21Object;
import cn.com.cdboost.charge.base.info.Afn22Object;
import cn.com.cdboost.charge.base.info.Afn23Object;
import cn.com.cdboost.charge.base.producer.RabbitmqProducer;
import cn.com.cdboost.charge.base.util.UuidUtil;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.dubbo.CustomerToMerchantService;
import cn.com.cdboost.charge.customer.vo.info.DeviceUseDetailedVo;
import cn.com.cdboost.charge.merchant.dubbo.DeviceService;
import cn.com.cdboost.charge.merchant.dubbo.WebChargingDeviceService;
import cn.com.cdboost.charge.merchant.vo.dto.ChargingCountByRunState;
import cn.com.cdboost.charge.merchant.vo.dto.MonitorDeviceDto;
import cn.com.cdboost.charge.merchant.vo.info.*;
import cn.com.cdboost.charge.merchant.vo.param.ChargerDeviceQueryParam;
import cn.com.cdboost.charge.merchant.vo.param.DeviceParam;
import cn.com.cdboost.charge.webapi.constants.ChargeConstant;
import cn.com.cdboost.charge.webapi.constants.ChargingEnum;
import cn.com.cdboost.charge.webapi.dto.Device;
import cn.com.cdboost.charge.webapi.dto.param.ChargerDeviceQueryWebParam;
import cn.com.cdboost.charge.webapi.rabbitmq.RabbitMQProducer;
import cn.com.cdboost.charge.webapi.service.WebChargingWrapper;
import com.alibaba.dubbo.config.annotation.Reference;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author wt
 * @desc
 * @create in  2018/11/5
 **/
@Service
public class WebChargingWrapperImpl implements WebChargingWrapper {

    @Reference(version = "1.0")
    private WebChargingDeviceService webChargingDeviceService;
    @Reference(version = "1.0")
    private DeviceService deviceService;
    @Reference(version = "1.0")
    CustomerToMerchantService customerToMerchantService;
    @Resource
    private RabbitmqProducer rabbitmqProducer;

    @Override
    public PageData deviceList(ChargerDeviceQueryWebParam chargerDeviceQueryWebParam, Integer userId) {
        ChargerDeviceQueryParam chargerDeviceQueryParam =new ChargerDeviceQueryParam();
        BeanUtils.copyProperties(chargerDeviceQueryWebParam,chargerDeviceQueryParam);
        return  webChargingDeviceService.deviceList(chargerDeviceQueryParam,userId);
    }

    @Override
    public void addDevice(Device deviceWebParam, Integer userId) {
        DeviceParam deviceParam=new DeviceParam();
        BeanUtils.copyProperties(deviceWebParam,deviceParam);
        webChargingDeviceService.addDevice(deviceParam,userId);
    }

    @Override
    public void addCardList(Device deviceWebParam) {

    }

    @Override
    public boolean deleteDevice(List<String> chargingPlieGuids, Integer userId) {
        return  webChargingDeviceService.deleteDevice(chargingPlieGuids,userId);
    }

    @Override
    public void deleteCardList(List<String> deviceNos) {
        webChargingDeviceService.deleteCardList(deviceNos);
    }

    @Override
    public ChargingDeviceVo queryDeviceDetial(String chargingPlieGuid) {
        return  webChargingDeviceService.queryDeviceDetial(chargingPlieGuid);
    }

    @Override
    public ChargingDeviceVo queryByChargingPlieGuid(String chargingPlieGuid) {
        return webChargingDeviceService.queryByChargingPlieGuid(chargingPlieGuid);
    }

    @Override
    public boolean editDevice(Device chargingDevice, Integer userId) {

        DeviceVo deviceVo=new DeviceVo();
        BeanUtils.copyProperties(chargingDevice,deviceVo);
        return webChargingDeviceService.editDevice(deviceVo,userId);
    }

    @Override
    public void editCardList(Device chargingDevice, String oldProjectGuid) {
        DeviceVo deviceVo=new DeviceVo();
        BeanUtils.copyProperties(chargingDevice,deviceVo);
        Integer integer = webChargingDeviceService.editCardList(deviceVo, oldProjectGuid);
        chargingDevice.setResult(integer);
    }

    @Override
    public void queryHeartStep(String id, String commNo) {
        if (StringUtils.isEmpty(commNo)) {
            throw new BusinessException("commNo不能为空");
        }
        Afn23Object afn23Object = new Afn23Object(UuidUtil.getUuid(),"23","999999999","0042475858fffaa",commNo,
                id,null,"0","");
        //下发指令
        int result  =  rabbitmqProducer.sendAfn23(afn23Object);
        if (result != 1) {
            throw new BusinessException("查询充电桩心跳间隔中间件指令发送失败");
        }

    }

    @Override
    public void setHeartStep(String id, ChargeHeartStepVo chargeHeartStepVo) {

        Afn23Object afn23Object = new Afn23Object(UuidUtil.getUuid(),"23","999999999","0042475858fffaa",chargeHeartStepVo.getCommNo(),
                id,null,"1",String.valueOf(chargeHeartStepVo.getHeartStep()));
        //下发指令
        int result  = rabbitmqProducer.sendAfn23(afn23Object);
        if (result != 1) {
            throw new BusinessException("设置充电桩心跳间隔中间件指令发送失败");
        }
    }

    @Override
    public void queryThreshold(String sessionId, String commNo, String port) {

        if (StringUtil.isEmpty(commNo)){
            throw new BusinessException("commNo不能为空");
        }
        if (StringUtil.isEmpty(port)){
            throw new BusinessException("port不能为空");
        }
        Afn22Object afn22Object = new Afn22Object(UuidUtil.getUuid(),"22","999999999","0042475858FFFFDF",commNo,
                sessionId,null,port);
        //下发指令
        int result = rabbitmqProducer.sendAfn22(afn22Object);
        if (result != 1) {
            throw new BusinessException("查询充电桩阈值中间件指令发送失败");
        }
        //webChargingDeviceService.queryThreshold( sessionId,  commNo,  port);
    }

    @Override
    public void setThreshold(String sessionId, ChargeThresholdVo chargeThresholdVo) {
        Afn21Object afn21Object = new Afn21Object(UuidUtil.getUuid(),"21","999999999","0042475858fffaa",
                chargeThresholdVo.getCommNo(),chargeThresholdVo.getPort(),sessionId,null,
                String.valueOf(chargeThresholdVo.getOverCurrent()),
                String.valueOf(chargeThresholdVo.getOverVoltage()),
                String.valueOf(chargeThresholdVo.getUnderVoltage()),
                String.valueOf(chargeThresholdVo.getUnderCurrent()),
                String.valueOf(chargeThresholdVo.getUnderCurrentDelay()));
        //下发指令
        int result =  rabbitmqProducer.sendAfn21(afn21Object);
        if (result != 1) {
            throw new BusinessException("设置充电桩阈值中间件指令发送失败");
        }
      //  webChargingDeviceService.setThreshold(sessionId,chargeThresholdVo);
    }

    @Override
    public void offOnCharge(OffOnChargeVo offOnChargeVo) {
        String errorMsg = "停用充电桩中间件指令成功";
        Afn19Object afn19Object = null;
        if (offOnChargeVo.getOnOrOff() == 0){// web端停用
            afn19Object = new Afn19Object(UUID.randomUUID().toString(),
                    "19",
                    "999999999",
                    "0042475858fffaa",
                    offOnChargeVo.getCommNo(),
                    "0",
                    offOnChargeVo.getPort(),
                    "off",
                    offOnChargeVo.getSessionId(),
                    "",
                    "",
                    "0",
                    "2",0,0,"");
            afn19Object.setNonUseFlag(String.valueOf(offOnChargeVo.getOnOrOff()));

            // web端停电（中间件停电成功会修改为空闲状态，修改为停用只能在回调函数内进行）
            this.updateRunState(offOnChargeVo.getCommNo(), ChargingEnum.OFF_STATE.getKey(),offOnChargeVo.getPort());
            //下发数据
            int result =  rabbitmqProducer.sendAfn19(afn19Object);
            if (result != 1) {
                errorMsg = "停用充电桩中间件指令失败";
                throw new BusinessException(errorMsg);
            }
        } else if (offOnChargeVo.getOnOrOff() == 1){
            // web端启用
            this.updateRunState(offOnChargeVo.getCommNo(), ChargingEnum.FREE_STATE.getKey(),offOnChargeVo.getPort());
        }else if (offOnChargeVo.getOnOrOff() == -1){// web端停电
            if (!StringUtil.isEmpty(offOnChargeVo.getChargingGuid())){
                DeviceUseDetailedVo chargingUseDetailed = customerToMerchantService.queryChargingRecordByChargingGuid(offOnChargeVo.getChargingGuid());
                if (chargingUseDetailed.getState().equals(ChargeConstant.ChargeState.COMPLETED.getState())){//判断该充电订单是否关闭
                    throw new BusinessException("该人员已完成充电，请刷新页面！");
                }
            }
            afn19Object = new Afn19Object(UUID.randomUUID().toString(),
                    "19",
                    "999999999",
                    "0042475858fffaa",
                    offOnChargeVo.getCommNo(),
                    "0",
                    offOnChargeVo.getPort(),
                    "off",
                    offOnChargeVo.getSessionId(),
                    "",
                    "",
                    "0",
                    "2",0,0,"");
            afn19Object.setNonUseFlag(String.valueOf(offOnChargeVo.getOnOrOff()));
            //下发数据
            int result =  rabbitmqProducer.sendAfn19(afn19Object);
            if (result != 1) {
                errorMsg = "停电充电桩中间件指令失败";
                throw new BusinessException(errorMsg);
            }
        }


       // webChargingDeviceService.offOnCharge(offOnChargeVo);
    }
    public void updateRunState(String commNo, Integer runState, String port) {
        deviceService.updateRunState(commNo,runState,port);
    }
    @Override
    public ChargingCountByRunState monitorDeviceCount(ChargerDeviceVo queryVo) {
        return webChargingDeviceService.monitorDeviceCount(queryVo);
    }

    @Override
    public PageData monitorDeviceList(ChargerDeviceQueryMerchantVo queryVo) {
        return webChargingDeviceService.monitorDeviceList(queryVo);
    }

    @Override
    public CurveQueryInfo queryCurve(String sessionId, String chargingPlieGuid, String chargingGuid, Integer queryFlag) {
        return webChargingDeviceService.queryCurve(sessionId,chargingPlieGuid,chargingGuid,queryFlag);
    }
}
