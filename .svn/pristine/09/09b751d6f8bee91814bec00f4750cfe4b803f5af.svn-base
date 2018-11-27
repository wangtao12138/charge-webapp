package cn.com.cdboost.charge.webapi.rabbitmq;

import cn.com.cdboost.charge.base.constant.GlobalConstant;
import cn.com.cdboost.charge.base.constant.WebSocketConstant;
import cn.com.cdboost.charge.base.handler.MyWebSocketHandler;
import cn.com.cdboost.charge.base.info.Afn27Object;
import cn.com.cdboost.charge.base.lisener.Afn27DataBackLisener;
import cn.com.cdboost.charge.base.vo.WebSocketResponse;
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
 * 清除ic卡档案
 */
@Component
@Slf4j
public class Afn27ResultDataBackLisener implements Afn27DataBackLisener {

    @Override
    public void onDataBack(String afn27ObjectStr) {
        log.info("下发读取ic卡档案 ：Afn27Object=" + afn27ObjectStr);

        // WEB推送
        JSONObject jsStr = JSONObject.parseObject(afn27ObjectStr);
        Afn27Object afn27Object = JSONObject.toJavaObject(jsStr, Afn27Object.class);
        this.sendWebUser(afn27Object);

    }

    private void sendWebUser(Afn27Object afn27Object) {
        try {
            CopyOnWriteArraySet<WebSocketSession> users = MyWebSocketHandler.users;
            if (CollectionUtils.isEmpty(users)) {
                log.info("webSocket 在线用户数为空");
                return;
            }
            String sessionId = afn27Object.getSessionId();
            for (WebSocketSession session :users){
                HttpSession httpSession = (HttpSession) session.getAttributes().get(GlobalConstant.HTTP_SESSION_OBJECT);
                if (httpSession != null){
                //if (sessionId.equals(httpSession.getId())) {
                    log.info("WebSocket中记录的sessionId=" + httpSession.getId());
                    WebSocketResponse<Afn26Response> response = new WebSocketResponse();
                    Afn26Response afn27Response = new Afn26Response();
                    response.setDataFlag(WebSocketConstant.DataFlagEnum.CLEAR_IC_CARD.getFlag());
                    afn27Response.setStatus(afn27Object.getState());
                    response.setData(afn27Response);
                    session.sendMessage(new TextMessage(JSON.toJSONString(response)));
                    log.info("清除ic卡档案 websocket send success: data=" + JSON.toJSONString(response));
                }
            }
        }catch (Exception e) {
            log.error("清除ic卡档案websocket推送数据异常",e);
        }
    }
}
