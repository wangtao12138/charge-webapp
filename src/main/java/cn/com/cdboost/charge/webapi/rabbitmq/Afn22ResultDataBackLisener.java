package cn.com.cdboost.charge.webapi.rabbitmq;

import cn.com.cdboost.charge.base.constant.GlobalConstant;
import cn.com.cdboost.charge.base.constant.WebSocketConstant;
import cn.com.cdboost.charge.base.handler.MyWebSocketHandler;
import cn.com.cdboost.charge.base.info.Afn22Object;
import cn.com.cdboost.charge.base.lisener.Afn22DataBackLisener;
import cn.com.cdboost.charge.base.vo.WebSocketResponse;
import cn.com.cdboost.charge.webapi.dto.Afn22Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 查询充电桩阈值回调
 */
@Slf4j
@Component
public class Afn22ResultDataBackLisener implements Afn22DataBackLisener {

    @Override
    public void onDataBack(String afn22ObjectStr) {
        log.info("查询充电桩阈值回调接口接收数据：AFN22Object=" +afn22ObjectStr);
        Afn22Object afn22Object = JSON.parseObject(afn22ObjectStr, Afn22Object.class);
        // WEB推送
        this.sendWebUser(afn22Object);

    }

    /**
     * WEB端推送
     * @param afn22Object
     */
    private void sendWebUser(Afn22Object afn22Object){
        try {
            CopyOnWriteArraySet<WebSocketSession> users = MyWebSocketHandler.users;
            if (CollectionUtils.isEmpty(users)) {
                log.info("webSocket 在线用户数为空");
                return;
            }

            for (WebSocketSession session :users){
                HttpSession httpSession = (HttpSession) session.getAttributes().get(GlobalConstant.HTTP_SESSION_OBJECT);
                if (httpSession != null){
                    log.info("WebSocket中记录的sessionId=" + httpSession.getId());
                    //主动推送
                    WebSocketResponse<Afn22Response> response = new WebSocketResponse();
                    Afn22Response res = new Afn22Response();
                    //设置值
                    if (!StringUtils.isEmpty(afn22Object.getOverCurrent())){
                        res.setOverCurrent(Float.parseFloat(afn22Object.getOverCurrent()));
                    }
                    if (!StringUtils.isEmpty(afn22Object.getOverVoltage())){
                        res.setOverVoltage(Float.parseFloat(afn22Object.getOverVoltage()));
                    }
                    if (!StringUtils.isEmpty(afn22Object.getUnderVoltage())){
                        res.setUnderVoltage(Float.parseFloat(afn22Object.getUnderVoltage()));
                    }
                    if (!StringUtils.isEmpty(afn22Object.getFullCurrent())){
                        res.setUnderCurrent(Float.parseFloat(afn22Object.getFullCurrent()));
                    }
                    if (!StringUtils.isEmpty(afn22Object.getFullTime())){
                        res.setUnderCurrentDelay(Integer.parseInt(afn22Object.getFullTime()));
                    }
                    res.setCommNo(afn22Object.getMoteEUIAES128());
                    res.setPort(afn22Object.getPort());
                    res.setStatus(afn22Object.getState());
                    response.setData(res);
                    response.setDataFlag(WebSocketConstant.DataFlagEnum.CHARGE_THRESHOLD.getFlag());
                    session.sendMessage(new TextMessage(JSON.toJSONString(response)));
                    log.info("查询充电桩阈值websocket send success: data=" + JSON.toJSONString(response));
                }
            }
        }catch (Exception e) {
            log.error("查询充电桩阈值WebSocket推送数据异常",e);
        }
    }
}
