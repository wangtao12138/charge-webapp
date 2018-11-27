package cn.com.cdboost.charge.webapi.rabbitmq;

import cn.com.cdboost.charge.base.constant.GlobalConstant;
import cn.com.cdboost.charge.base.constant.WebSocketConstant;
import cn.com.cdboost.charge.base.handler.MyWebSocketHandler;
import cn.com.cdboost.charge.base.lisener.Afn26DataBackLisener;
import cn.com.cdboost.charge.base.vo.WebSocketResponse;
import cn.com.cdboost.charge.webapi.dto.Afn26ObjectResponse;
import cn.com.cdboost.charge.webapi.dto.Afn26Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 下发读取ic卡档案回调
 */
@Slf4j
@Component
public class Afn26ResultDataBackLisener implements Afn26DataBackLisener {

    @Override
    public void onDataBack(String afn26ObjectStr) {
        log.info("下发读取ic卡档案 ：Afn26Object=" + afn26ObjectStr);

        // WEB推送
        JSONObject jsStr = JSONObject.parseObject(afn26ObjectStr);
        Afn26ObjectResponse afn26Object = JSONObject.toJavaObject(jsStr, Afn26ObjectResponse.class);
        this.sendWebUser(afn26Object);

    }

    /**
     * WEB端推送
     * @param afn26Object
     */
    private void sendWebUser(Afn26ObjectResponse afn26Object){
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
                    //主动推送
                    WebSocketResponse<Afn26Response> response = new WebSocketResponse();
                    Afn26Response res = new Afn26Response();
                    //设置值
                    res.setStatus(afn26Object.getState());
                    //判断指令类型 0:下发ic卡档案   1:读取ic卡档案
                    if (afn26Object.getSendFlag() == 0){
                        response.setDataFlag(WebSocketConstant.DataFlagEnum.SEND_IC_CARD.getFlag());
                    }else if (afn26Object.getSendFlag() == 1){
                        res.setExist(afn26Object.isExist());
                        response.setDataFlag(WebSocketConstant.DataFlagEnum.READ_IC_CARD.getFlag());
                    }
                    response.setData(res);
                    session.sendMessage(new TextMessage(JSON.toJSONString(response)));

                    log.info("下发读取ic卡档案 send success: data=" + JSON.toJSONString(response));
                }
            }
        }catch (Exception e) {
            log.error("读取或设置充电桩心跳间隔时间websocket推送数据异常",e);
        }
    }
}
