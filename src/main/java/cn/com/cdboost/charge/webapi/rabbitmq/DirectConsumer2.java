package cn.com.cdboost.charge.webapi.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

//@Component
public class DirectConsumer2 {
    /**
     * 消息消费
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     */
    //@RabbitListener(queues = "direct.charge1")
    public void recieved(byte[] msg) {
        String bodyStr = null;
        try {
            bodyStr = new String(msg, "UTF-8");
            System.out.println(bodyStr);
            System.out.println("Receiverresult directData:" + bodyStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
