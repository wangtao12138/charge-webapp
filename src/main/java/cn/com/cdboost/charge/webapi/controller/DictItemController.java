package cn.com.cdboost.charge.webapi.controller;


import cn.com.cdboost.charge.base.aop.SystemControllerLog;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.user.dto.info.DictItemInfo;
import cn.com.cdboost.charge.user.dubbo.DictService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wt
 * @desc 数据字典
 * @create 2017/7/12 0012
 **/
@RestController
@Api(value ="数据字典",tags = "数据字典相关接口")
@RequestMapping("/api/webapi/v1/dictItem")
@Slf4j
public class DictItemController {


    @Reference(version = "1.0")
    private DictService dictItemService;


    /**
     * 通过字典类别，查询静态字典明细记录
     * @param dictCode
     * @return
     */
    @SystemControllerLog(description = "通过字典类别，查询静态字典明细记录")
    @ApiOperation(value="通过字典类别，查询静态字典明细记录")
    @GetMapping(value = "/{dictCode}")
    public ApiResult queryByDictCode(@RequestParam Integer dictCode){
        ApiResult<List<DictItemInfo>> result = new ApiResult<>();
        List<DictItemInfo> list = dictItemService.findByDictCode(String.valueOf(dictCode));
        result.setData(list);
        return result;
    }

}
