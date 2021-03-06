package cn.com.cdboost.charge.webapi.rabbitmq;

import cn.com.cdboost.charge.base.constant.GlobalConstant;
import cn.com.cdboost.charge.base.constant.WebSocketConstant;
import cn.com.cdboost.charge.base.handler.MyWebSocketHandler;
import cn.com.cdboost.charge.base.lisener.Afn21DataBackLisener;
import cn.com.cdboost.charge.base.vo.WebSocketResponse;
import cn.com.cdboost.charge.webapi.dto.Afn21ObjectResponse;
import cn.com.cdboost.charge.webapi.dto.ChargerStatusRes;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 设置充电桩阈值回调
 */
@Slf4j
@Component
public class Afn21ResultDataBackLisener implements Afn21DataBackLisener {

    @Override
    public void onDataBack(String Afn21ObjectStr) {
        log.info("设置充电桩阈值回调接口接收数据：AFN21Object=" + JSON.toJSONString(Afn21ObjectStr));

        Afn21ObjectResponse afn21Object = JSON.parseObject(Afn21ObjectStr, Afn21ObjectResponse.class);
        // WEB推送
        this.sendWebUser(afn21Object);

    }

    /**
     * WEB端推送
     * @param afn21Object
     */
    private void sendWebUser(Afn21ObjectResponse afn21Object){
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
                    WebSocketResponse<ChargerStatusRes> response = new WebSocketResponse();
                    ChargerStatusRes res = new ChargerStatusRes();
                    res.setStatus(afn21Object.getState());
                    res.setCommNo(afn21Object.getMoteEUI());
                    res.setPort(afn21Object.getPort());
                    response.setData(res);
                    response.setDataFlag(WebSocketConstant.DataFlagEnum.CHARGE_SET_THRESHOLD.getFlag());
                    session.sendMessage(new TextMessage(JSON.toJSONString(response)));
                    log.info("设置充电桩阈值websocket send success: data=" + JSON.toJSONString(response));
                }
            }
        }catch (Exception e) {
            log.error("设置充电桩阈值WebSocket推送数据异常",e);
        }
    }
}
