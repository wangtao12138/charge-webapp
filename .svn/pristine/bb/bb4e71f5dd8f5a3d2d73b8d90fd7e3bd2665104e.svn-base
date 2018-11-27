package cn.com.cdboost.charge.webapi.rabbitmq;

import cn.com.cdboost.charge.base.info.Afn19Object;
import cn.com.cdboost.charge.base.info.Afn23Object;
import cn.com.cdboost.charge.base.producer.RabbitmqProducer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitMQProducer {
    @Resource
    private RabbitmqProducer rabbitmqProducer;

    public void sendDirect() {
        Afn19Object afn19Object = new Afn19Object("120210",
                "19",
                "999999999",
                "42475858fffffa",
                "1111",
                "20000",
                "1",
                "on",
                "",
                "54da6s4da64da",
                "16516516",
                "60",
                "1",
                1,
                1,String.valueOf("250"));
        afn19Object.setChargingGuid("213215da3d1asd1");
        rabbitmqProducer.sendAfn19(afn19Object);
    }

}