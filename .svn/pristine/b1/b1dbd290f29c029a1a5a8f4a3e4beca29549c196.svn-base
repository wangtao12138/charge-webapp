package cn.com.cdboost.charge.webapi.controller;

import cn.com.cdboost.charge.base.aop.SystemControllerLog;
import cn.com.cdboost.charge.base.constant.GlobalConstant;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.user.dto.param.SysConfigPara;
import cn.com.cdboost.charge.user.dubbo.SysConfigService;
import cn.com.cdboost.charge.webapi.dto.info.SystemConfigInfo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wt
 * @desc
 * @create in  2018/11/20
 **/
@Api(value = "系统",tags = "系统参数")
@RestController
@RequestMapping(value = "/api/webapi/v1/system")
public class SystemController {


    @Reference(version = "1.0")
    private SysConfigService sysConfigService;


    @Value("${version}")
    private String version;

    @SystemControllerLog(description = "查询系统配置信息")
    @GetMapping("/querySystemConfig")
    @ApiOperation(value="查询系统配置信息")
    public ApiResult querySystemConfig() {
        ApiResult<SystemConfigInfo> result = new ApiResult<>();
        SystemConfigInfo configInfo = new SystemConfigInfo();

        SysConfigPara sysConfig = sysConfigService.getSysConfigPara();
        if (sysConfig != null) {
            configInfo.setSysName(sysConfig.getName());
            configInfo.setBackground(sysConfig.getBackgroundPicPath());
            configInfo.setBalanceDate(sysConfig.getBalanceDate());
            configInfo.setVersion(version);
        }

        // webSocket地址
        String webSocketUrl = sysConfigService.getWebSocketUrl();
        configInfo.setWebSocketUrl(webSocketUrl);
        // 版权信息
        String copRight = sysConfigService.getcopyRight();
        configInfo.setCopyright(copRight);
        result.setData(configInfo);

        return result;
    }
}
