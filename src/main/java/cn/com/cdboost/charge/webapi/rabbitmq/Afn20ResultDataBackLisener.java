package cn.com.cdboost.charge.webapi.rabbitmq;

import cn.com.cdboost.charge.base.constant.GlobalConstant;
import cn.com.cdboost.charge.base.constant.WebSocketConstant;
import cn.com.cdboost.charge.base.handler.MyWebSocketHandler;
import cn.com.cdboost.charge.base.info.Afn20Object;
import cn.com.cdboost.charge.base.lisener.Afn20DataBackLisener;
import cn.com.cdboost.charge.base.vo.WebSocketResponse;
import cn.com.cdboost.charge.customer.dubbo.CustomerToMerchantService;
import cn.com.cdboost.charge.customer.vo.info.DeviceUseDetailedVo;
import cn.com.cdboost.charge.merchant.dubbo.DeviceService;
import cn.com.cdboost.charge.merchant.vo.info.DeviceVo;
import cn.com.cdboost.charge.webapi.dto.MonitorDeviceRes;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
public class Afn20ResultDataBackLisener implements Afn20DataBackLisener {
    @Reference(version = "1.0")
    private DeviceService chargingDeviceService;
    @Reference(version = "1.0")
    private CustomerToMerchantService chargingUseDetailedService;


    @Override
    public void onDataBack(String afn20ObjectStr) {

        Afn20Object afn20Object = JSON.parseObject(afn20ObjectStr, Afn20Object.class);

//        log.info("实时充电回调接口接收数据：AFN20Object=" + JSON.toJSONString(afn20Object));
        // 查询使用记录,可能为空，所以后面逻辑需要针对使用记录判断是否非空
        DeviceUseDetailedVo useDetailed = chargingUseDetailedService.queryChargingRecordByChargingGuid(afn20Object.getChargingGuid());

        DeviceVo chargingDevice = chargingDeviceService.queryByCommon(afn20Object.getMoteEUI(), afn20Object.getPort());


        // WEB推送
        this.sendWebUser(afn20Object,chargingDevice,useDetailed);

    }

    /**
     * WEB端推送
     * @param afn20Object
     */
    private void sendWebUser(Afn20Object afn20Object, DeviceVo device, DeviceUseDetailedVo useDetailed){
        try {
            CopyOnWriteArraySet<WebSocketSession> users = MyWebSocketHandler.users;
            if (CollectionUtils.isEmpty(users)) {
//                log.info("webSocket 在线用户数为空");
                return;
            }
            //端口号
            String port = afn20Object.getPort();
            for (WebSocketSession session :users){
                if (!session.isOpen()) {
                    users.remove(session);
                    continue;
                }

                HttpSession httpSession = (HttpSession) session.getAttributes().get(GlobalConstant.HTTP_SESSION_OBJECT);
                if (httpSession != null){
//                    log.info("WebSocket中记录的sessionId=" + httpSession.getId());
                    //主动推送
                    WebSocketResponse<MonitorDeviceRes> response = new WebSocketResponse<>();
                    MonitorDeviceRes monitorDeviceRes = new MonitorDeviceRes();
                    /*//设备信号强度
                    monitorDeviceRes.setSignalStrength(Integer.parseInt(afn20Object.getSignalStrength()));*/
                    //设备cpu温度
                    monitorDeviceRes.setDevTemperature(afn20Object.getDevTemperature());
                    //功率
                    if (!StringUtil.isEmpty(afn20Object.getCurPower())) {
                        monitorDeviceRes.setPower(new BigDecimal(afn20Object.getCurPower()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }else {
                        monitorDeviceRes.setPower(null);
                    }
                    //电压
                    if (!StringUtil.isEmpty(afn20Object.getVoltage())) {
                        monitorDeviceRes.setVoltage(new BigDecimal(afn20Object.getVoltage()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }else {
                        monitorDeviceRes.setPower(null);
                    }
                    //电流
                    if (!StringUtil.isEmpty(afn20Object.getCurrent())) {
                        monitorDeviceRes.setCurrent(new BigDecimal(afn20Object.getCurrent()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }else {
                        monitorDeviceRes.setCurrent(null);
                    }
                    //操作时间
                    monitorDeviceRes.setTime(afn20Object.getTime().substring(11,16));
                    //百分比
                    monitorDeviceRes.setPercent(afn20Object.getPercent());
                    //端口号
                    if (!StringUtil.isEmpty(port)){
                        monitorDeviceRes.setPort(port);
                    }
                    //设备编号
                    monitorDeviceRes.setDeviceNo(device.getDeviceNo());
                    //设备信号状态
                    monitorDeviceRes.setOnline(device.getOnline());
                    monitorDeviceRes.setRunState(device.getRunState());
                    if (useDetailed != null){
                        monitorDeviceRes.setCarCategory(1);
                        monitorDeviceRes.setPayCategory(useDetailed.getPayCategory());
                        monitorDeviceRes.setChargingWay(useDetailed.getChargingWay());
                        monitorDeviceRes.setChargingTime(useDetailed.getChargingTime());
                        //剩余时长
                        if (!StringUtil.isEmpty(afn20Object.getTimeRemain())){
                            monitorDeviceRes.setRemainTime(Integer.parseInt(afn20Object.getTimeRemain()));
                            monitorDeviceRes.setUseTime(Integer.parseInt(afn20Object.getCumulativeTime()));
                        }else {
                            monitorDeviceRes.setRemainTime(useDetailed.getChargingTime() * 60);
                        }
                        //实际充电时长
                        if (!StringUtil.isEmpty(afn20Object.getCumulativeTime())){
                            monitorDeviceRes.setUseTime(Integer.parseInt(afn20Object.getCumulativeTime()));
                        }else {
                            monitorDeviceRes.setUseTime(0);
                        }
                        //累计使用电量
                        if (!StringUtil.isEmpty(afn20Object.getCumulativePower())){
                            BigDecimal userPower = BigDecimal.valueOf(Double.parseDouble(afn20Object.getCumulativePower()));
                            monitorDeviceRes.setUsePower(userPower);
                        }
                    }

                    response.setData(monitorDeviceRes);
                    response.setDataFlag(WebSocketConstant.DataFlagEnum.MONITOR_DEVICE_LIST.getFlag());
                    session.sendMessage(new TextMessage(JSON.toJSONString(response)));

                }
            }
        }catch (Exception e) {
            log.error("充电桩WebSocket推送数据异常",e);
        }
    }
}
