package cn.com.cdboost.charge.webapi.controller;

import cn.com.cdboost.charge.trade.service.SmsService;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/8/28 0028.
 */
@Api(value = "短信管理",tags = "短信相关操作接口")
@RestController
@RequestMapping(value = "/api/trade/v1/smss")
public class SmsController {

    @Reference
    private SmsService smsService;

    @ApiOperation(value="发送短信", notes="发送短信和设置redis")
    @ApiImplicitParam(name = "phoneNumber", value = "手机号码", required = true)
    @GetMapping(value = "/{phoneNumber}")
    public void queryByParam(@PathVariable("phoneNumber") String phoneNumber) {
        smsService.sendSms(phoneNumber);
    }
}
