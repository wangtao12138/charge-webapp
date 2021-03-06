package cn.com.cdboost.charge.webapi.controller;

import cn.com.cdboost.charge.base.aop.SystemControllerLog;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.user.dto.info.MenuTreeInfo;
import cn.com.cdboost.charge.user.dubbo.MenuService;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wt
 * @desc
 * @create in  2018/11/23
 **/
@RestController
@Api(value ="导航菜单",tags = "导航菜单相关接口")
@RequestMapping("/api/webapi/v1/menu")
public class MenuController {


    @Reference(version = "1.0")
    MenuService menuService;
    // 查询系统中所有启用的菜单，并按照树形层级关系返回
    @SystemControllerLog(description = "查询系统中所有启用的菜单，并按照树形层级关系返回")
    @GetMapping("/queryAllEnabledMenus")
    @ApiOperation(value="查询系统中所有启用的菜单，并按照树形层级关系返回", notes="查询系统中所有启用的菜单，并按照树形层级关系返回")
    public ApiResult queryAllEnabledMenus() {
        ApiResult<List<MenuTreeInfo>> result = new ApiResult<>();
        List<MenuTreeInfo> menuTreeInfos = menuService.queryAllEnabledMenus();
        result.setData(menuTreeInfos);
        return result;
    }
}
