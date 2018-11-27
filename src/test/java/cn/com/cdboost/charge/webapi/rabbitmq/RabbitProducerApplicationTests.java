package cn.com.cdboost.charge.webapi.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitProducerApplicationTests {

    @Autowired
    private RabbitMQProducer producer;

    @Test
    public void sendDirect() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        producer.sendDirect();
        stopWatch.stop();
        System.out.println("发送总耗时：" + stopWatch.getTotalTimeMillis());
    }
}
