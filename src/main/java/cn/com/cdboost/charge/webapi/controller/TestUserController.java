package cn.com.cdboost.charge.webapi.controller;

import cn.com.cdboost.charge.trade.service.TestUserService;
import cn.com.cdboost.charge.trade.vo.TestUserVo;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/8/27 0027.
 */

@Api(value = "测试用户管理",tags = "测试用户管理操作接口")
@RestController
@RequestMapping(value = "/api/trade/v1/testUsers")
public class TestUserController {

    @Reference
    private TestUserService testUserService;

    @ApiOperation(value="更新余额", notes="更新测试用户的余额")
    @PostMapping(value = "/")
    public void addMenu(@ApiParam(name = "testUserVo",value = "测试用户更新入参") @RequestBody TestUserVo testUserVo) {
        testUserService.update(testUserVo);
        System.out.println("更新用户余额结束");
    }
}
