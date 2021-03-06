package cn.com.cdboost.charge.webapi.controller;

import cn.com.cdboost.charge.base.aop.SystemControllerLog;
import cn.com.cdboost.charge.base.constant.UserInfoKey;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.customer.dubbo.CustomerToMerchantService;
import cn.com.cdboost.charge.customer.vo.param.ChargerDeviceQueryVo;
import cn.com.cdboost.charge.merchant.vo.info.ChargerSchemeQueryVo;
import cn.com.cdboost.charge.statistic.vo.dto.ProjectUseCountDto;
import cn.com.cdboost.charge.user.dubbo.OrgService;
import cn.com.cdboost.charge.webapi.dto.info.DeviceUseCountResp;
import cn.com.cdboost.charge.webapi.dto.info.PowerAndFeeCountResp;
import cn.com.cdboost.charge.webapi.dto.param.ElectricCountQueryParam;
import cn.com.cdboost.charge.webapi.service.ElectricFeeWrapper;
import cn.com.cdboost.charge.webapi.service.GenerateFileServiceWrapper;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Api(value = "电量电费统计",tags = "电量电费统计")
@RestController
@RequestMapping(value = "/api/webapi/v1")
public class ElectricFeeController {
    @Autowired
    private GenerateFileServiceWrapper generateFileService;
    @Autowired
    private ElectricFeeWrapper electricFeeWrapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Reference(version="1.0")
    private OrgService orgService;

    @Reference(version = "1.0")
    private CustomerToMerchantService customerToMerchantService;


    @SystemControllerLog(description = "统计数据详情柱状图")
    @ApiOperation(value = "统计数据详情柱状图")
    @ApiImplicitParam(name = "ElectricCountQueryParam", value = "查询对象", required = true)
    @GetMapping(value = "/electricCount")
    public ApiResult electricCount(HttpServletRequest request, @ModelAttribute ElectricCountQueryParam param){
        ApiResult result=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID));
        // 查询用户的组织数据权限
        List<Long> dataOrgNos = orgService.queryDataOrgList(Integer.valueOf(userId));
        if("1".equals(param.getNodeType())){
            String orgNo = param.getNodeId();
            List<Long> childOrgNos = orgService.queryChildren(Long.valueOf(orgNo));
            dataOrgNos.retainAll(childOrgNos);
        }
        param.setOrgNoList(new ArrayList(dataOrgNos));
        PowerAndFeeCountResp powerAndFeeCountResp = electricFeeWrapper.queryPowerAndFeeCount(param);
        result.setData(powerAndFeeCountResp);
        return result;
    }

    @SystemControllerLog(description = "站点使用情况统计")
    @ApiOperation(value = "站点使用情况统计")
    @GetMapping(value = "/projectUseCountList")
    public ApiResult projectUseCountList(HttpServletRequest request,@ModelAttribute ChargerSchemeQueryVo param){
        ApiResult result=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID));
        // 查询用户的组织数据权限
        List<Long> longs = orgService.queryDataOrgList(Integer.valueOf(userId));
        param.setOrgNoList(new ArrayList(longs));
        ProjectUseCountDto projectUseCountDto = electricFeeWrapper.projectUseCountList(param);
        result.setData(projectUseCountDto);
        return result;
    }

 /*   @SystemControllerLog(description = "站点使用情况统计列表下载")
    @ApiOperation(value = "站点使用情况统计列表下载")
    @GetMapping(value = "/downProjectUseCountList")
    public void downProjectUseCountList(HttpServletRequest request, HttpServletResponse response, ChargerSchemeQueryVo queryVo){
        LoginUser loginUser = (LoginUser) session.getAttribute(GlobalConstant.CURRENT_LOGIN_USER);
        //调用业务层
        Set<Long> longs = redisService.queryUserOrgNoByUserId(loginUser.getId());
        queryVo.setOrgNoList(new ArrayList(longs));
        ProjectUseCountDto projectUseCountDto = chargingDeviceService.projectUseCountList(queryVo);
        try {
            XSSFWorkbook workBook = generateFileService.generateProjectUseListExcel("站点使用情况统计列表",projectUseCountDto.getList());
            DownLoadUtil.downExcel(response,workBook);
        } catch (Exception e) {
            String message = "站点使用情况统计列表";
            logger.error(message,e);
        }
    }*/

    @SystemControllerLog(description = "设备使用情况统计")
    @ApiOperation(value = "设备使用情况统计")
    @GetMapping(value = "/deviceUseCountList")
    public ApiResult deviceUseCountList(@RequestBody ChargerDeviceQueryVo queryVo){
        ApiResult result=new ApiResult();
        //查询数据库
        DeviceUseCountResp deviceUseCountDto = electricFeeWrapper.deviceUseCountList(queryVo);
        result.setData(deviceUseCountDto);
        return result;
    }

  /*    @SystemControllerLog(description = "设备使用情况统计列表下载")
    @ApiOperation(value = "设备使用情况统计列表下载")
    @GetMapping(value = "/downDeviceUseCountList")
    public void downDeviceUseCountList(HttpServletResponse response, ChargerDeviceQueryVo queryVo){
        DeviceUseCountDto deviceUseCountDto = chargingDeviceService.deviceUseCountList(queryVo);
        try {
            XSSFWorkbook workBook = generateFileService.generateDeviceUseListExcel("设备使用情况统计列表",deviceUseCountDto.getList());
            DownLoadUtil.downExcel(response,workBook);
        } catch (Exception e) {
            String message = "设备使用情况统计列表下载";
            logger.error(message,e);
        }
    }

    *
     * 电量电费统计--设备使用情况详情
     * @param queryVo
     * @return

    @SystemControllerLog(description = "设备使用情况详情")
    @ApiOperation(value = "设备使用情况详情")
    @GetMapping(value = "/deviceUseCountDetail")
    public ApiResult deviceUseCountDetail(@RequestBody ChargerDeviceQueryVo queryVo){
        ApiResult result=new ApiResult();
        //查询数据库
        DeviceUseCountListDto deviceUseCountDto = chargingDeviceService.deviceUseCountDetail(queryVo);
        result.setData(deviceUseCountDto);
        return result;
    }

    @SystemControllerLog(description = "充电设备使用列表")
    @ApiOperation(value = "充电设备使用列表")
    @GetMapping(value = "/deviceDetialUseList")
    public ApiResult deviceDetialUseList(@RequestBody ChargerDeviceQueryVo queryVo){
        ApiResult result=new ApiResult();
        //查询数据库
        List<ChargingUseDetailedDto> chargingUseDetailedDtos = chargingDeviceService.deviceDetialUseList(queryVo);
        result.setData(chargingUseDetailedDtos);
        return result;
    }

    @SystemControllerLog(description = "充电设备使用列表下载")
    @ApiOperation(value = "充电设备使用列表下载")
    @GetMapping(value = "/deviceDetialUseDown")
    public void deviceDetialUseDown(HttpServletResponse response, ChargerDeviceQueryVo queryVo){
        //查询数据库
        List<ChargingUseDetailedDto> chargingUseDetailedDtos = chargingDeviceService.deviceDetialUseList(queryVo);
        try {
            XSSFWorkbook workBook = generateFileService.generateUseDetailedListExcel("设备使用列表",chargingUseDetailedDtos);
            DownLoadUtil.downExcel(response,workBook);
        } catch (Exception e) {
            String message = "使用列表";
            log.error(message,e);
        }
    }

    @SystemControllerLog(description = "电量电费统计列表")
    @ApiOperation(value = "电量电费统计列表")
    @GetMapping(value = "/electricCountList")
    public ApiResult electricCountList(HttpSession session,@RequestBody ChargerDeviceQueryVo queryVo){
        ApiResult result=new ApiResult();
        //查询数据库
        LoginUser loginUser = (LoginUser) session.getAttribute(GlobalConstant.CURRENT_LOGIN_USER);
        //调用业务层
        Set<Long> longs = redisService.queryUserOrgNoByUserId(loginUser.getId());
        queryVo.setOrgNoList(new ArrayList(longs));
        ElectricCountDto electricCountDto = chargingDeviceService.queryCountList(queryVo);
        result.setData(electricCountDto);
        return result;
    }

    @SystemControllerLog(description = "电量电费统计列表下载")
    @ApiOperation(value = "电量电费统计列表下载")
    @GetMapping(value = "/electricCountListDown")
    public void electricCountListDown(HttpSession session,HttpServletResponse response, ChargerDeviceQueryVo queryVo){
        //查询数据库
        LoginUser loginUser = (LoginUser) session.getAttribute(GlobalConstant.CURRENT_LOGIN_USER);
        //调用业务层
        Set<Long> longs = redisService.queryUserOrgNoByUserId(loginUser.getId());
        queryVo.setOrgNoList(new ArrayList(longs));
        //查询数据库
        ElectricCountDto electricCountDto = chargingDeviceService.queryCountList(queryVo);
        try {
            XSSFWorkbook workBook = generateFileService.generateElectricAndFeeListExcel("电量电费统计列表",electricCountDto.getElectricAndFeeDtoList());
            DownLoadUtil.downExcel(response,workBook);
        } catch (Exception e) {
            String message = "电量电费统计列表";
            log.error(message,e);
        }
    }*/
}
