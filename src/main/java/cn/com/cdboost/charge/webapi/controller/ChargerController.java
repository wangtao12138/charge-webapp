package cn.com.cdboost.charge.webapi.controller;

import cn.com.cdboost.charge.base.aop.SystemControllerLog;
import cn.com.cdboost.charge.base.constant.UserInfoKey;
import cn.com.cdboost.charge.base.util.DateUtil;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.dubbo.CustomerToMerchantService;
import cn.com.cdboost.charge.customer.dubbo.CustomerWebService;
import cn.com.cdboost.charge.customer.vo.info.ElectricCountDto;
import cn.com.cdboost.charge.customer.vo.info.HeartDto;
import cn.com.cdboost.charge.customer.vo.info.SignalCurveInfo;
import cn.com.cdboost.charge.customer.vo.param.ChargerDeviceQueryVo;
import cn.com.cdboost.charge.merchant.dubbo.DeviceService;
import cn.com.cdboost.charge.merchant.vo.dto.*;
import cn.com.cdboost.charge.merchant.vo.info.*;
import cn.com.cdboost.charge.merchant.vo.info.OffOnChargeVo;
import cn.com.cdboost.charge.user.dubbo.OrgService;
import cn.com.cdboost.charge.webapi.dto.Device;
import cn.com.cdboost.charge.webapi.dto.info.ProjectResp;
import cn.com.cdboost.charge.webapi.dto.param.*;
import cn.com.cdboost.charge.webapi.service.GenerateFileServiceWrapper;
import cn.com.cdboost.charge.webapi.service.ProjectWrapper;
import cn.com.cdboost.charge.webapi.service.WebChargingWrapper;
import cn.com.cdboost.charge.webapi.util.DownLoadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 充电控制类
 */
@Api(value ="充电设备管理",tags = "充电设备管理相关接口")
@RestController
@Slf4j
@RequestMapping(value = "/api/webapi/v1/charger")

public class ChargerController {

    @Autowired
    WebChargingWrapper webChargingWrapper;
    @Autowired
    GenerateFileServiceWrapper generateFileServiceWrapper;
    @Autowired
    ProjectWrapper projectWrapper;

    @Reference(version="1.0")
    OrgService orgService;

    @Reference(version="1.0")
    CustomerToMerchantService customerToMerchantService;

    @Reference(version="1.0")
    DeviceService deviceService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Value(value = "${callback.url}")
    private String host;
    @Reference(version="1.0")
    CustomerWebService customerWebService;
    
    /**
     * 充电设备列表查询
     * @param
     * @return
     */
    @SystemControllerLog(description = "充电设备列表查询")
    @GetMapping(value = "/deviceList")
    @ApiOperation(value="充电设备列表查询")
    @ApiParam(name = "queryVo",value = "充电设备列表查询入参",required = true)
    public ApiResult deviceList(HttpServletRequest request, @ModelAttribute ChargerDeviceQueryWebParam chargerDeviceQueryWebParam){

        ApiResult<PageData<ChargingDeviceVo>> ApiResult=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;

        //查询数据库
        PageData pageData=webChargingWrapper.deviceList(chargerDeviceQueryWebParam, Integer.valueOf(userId));
        if(pageData==null){
            PageData pageData1=new PageData();
            pageData1.setList(new ArrayList());
            ApiResult.setData(pageData1);
        }else {
            ApiResult.setData(pageData);
        }
        return ApiResult;
    }
    /**
     * 充电设备列表下载
     * @param
     * @return
     */
    @SystemControllerLog(description = "充电设备列表下载")
    @GetMapping(value = "/deviceListDown")
    @ApiOperation(value="充电设备列表下载")
    @ApiParam(name = "queryVo",value = "充电设备列表下载入参",required = true)
    public void deviceListDown(HttpServletRequest request, HttpServletResponse response, ChargerDeviceQueryWebParam chargerDeviceQueryWebParam){
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        //查询数据库
        PageData pageData = webChargingWrapper.deviceList(chargerDeviceQueryWebParam, Integer.valueOf(userId));
        try {
            XSSFWorkbook workBook = generateFileServiceWrapper.generateChargingDeviceListExcel("充电桩列表",pageData.getList());
            DownLoadUtil.downExcel(response,workBook);
        } catch (Exception e) {
            String message = "充电桩列表";
            log.error(message,e);
        }
    }
    /**
     * 添加充电设备
     * @param
     * @return
     */

    @SystemControllerLog(description = "添加充电设备")
    @PostMapping(value = "/device")
    @ApiOperation(value="充电设备添加")
    @ApiParam(name = "param",value = "充电设备添加入参",required = true)
    public String addDevice(HttpServletRequest request, @Valid @RequestBody ChargerDeviceAddWebParam chargerDeviceAddWebParam){
        ApiResult ApiResult=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        Device device = new Device();
        BeanUtils.copyProperties(chargerDeviceAddWebParam,device);
        device.setInstallDate(DateUtil.parseDate(chargerDeviceAddWebParam.getInstallDate()));
        //调用业务层
        webChargingWrapper.addDevice(device, Integer.valueOf(userId));

        //添加ic卡下发表
        webChargingWrapper.addCardList(device);
       /* userLogService.create(currentUser.getId(), Action.ADD.getActionId(), "充电桩设备", "充电桩设备编号", param.getDeviceNo(),"新增["+param.getDeviceNo()+"]充电桩设备",JSON.toJSONString(param));*/
        ApiResult.setMessage("添加成功！");
        return JSON.toJSONString(ApiResult);
    }
    /**
     * 删除充电设备
     * @param deviceNos
     * @return
     */
    @SystemControllerLog(description = "删除充电设备")
    @DeleteMapping(value = "/device")
    @ApiOperation(value="删除充电设备")
    public ApiResult deleteDevice(HttpServletRequest request, @RequestBody List<String> deviceNos){
        ApiResult ApiResult=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        //调用业务层删除设备
        boolean flag = webChargingWrapper.deleteDevice(deviceNos, Integer.valueOf(userId));
        if (flag){
            ApiResult.setMessage("删除成功");
          /*  userLogService.create(currentUser.getId(), Action.DELETE.getActionId(), "充电桩设备", "充电桩设备编号", "","删除["+deviceNos.toString()+"]充电桩设备",JSON.toJSONString(deviceNos));*/
        } else {
            ApiResult.error("删除失败");
        }
        webChargingWrapper.deleteCardList(deviceNos);
        return ApiResult;
    }
    /**
     * 充电设备详情---基础信息
     * @param deviceNo
     * @return
     */
    @SystemControllerLog(description = "充电设备详情---基础信息")
    @GetMapping(value = "/deviceDetial/{deviceNo}")
    @ApiOperation(value="充电设备详情---基础信息")
    @ApiParam(name = "deviceNo",value = "充电设备详情---基础信息入参",required = true)
    public ApiResult deviceDetial(@PathVariable String deviceNo){
        ApiResult ApiResult=new ApiResult();
        //查询数据库
        ChargingDeviceVo chargingDeviceDto = webChargingWrapper.queryDeviceDetial(deviceNo);
        ApiResult.setData(chargingDeviceDto);
        return ApiResult;
    }


    /**
     * 生成设备二维码
     * @param res
     * @param deviceNo
     * @throws IOException
     */
    @SystemControllerLog(description = "生成设备二维码")
    @PostMapping(value = "/getQ")
    @ApiOperation(value="生成设备二维码")
    public void getqcode(HttpServletResponse res,String deviceNo,String port) throws IOException {
        String format = "png";
        String content = host+"/chargePay/index.html?deviceNo="+deviceNo+port;

        int width = 150;//图片的宽度
        int height = 150;//高度

        if(content != null && !"".equals(content)){
            ServletOutputStream stream = null;
            try {
                Hashtable hints = new Hashtable();
                //内容所使用编码
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                hints.put(EncodeHintType.MARGIN, 3);
                //生成二维码
                stream = res.getOutputStream();
                BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height , hints);
                MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
            } catch (WriterException e) {
                e.printStackTrace();
            }finally{
                if(stream != null){
                    stream.flush();
                    stream.close();
                }
            }

        }
    }

    /**
     * 编辑充电设备
     * @param param
     * @return
     */
    @SystemControllerLog(description = "编辑充电设备")
    @PutMapping(value = "/editDevice")
    @ApiOperation(value="编辑充电设备")
    public ApiResult editDevice(HttpServletRequest request, @RequestBody ChargerDeviceEditWebParam param){
        ApiResult ApiResult=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf( entries.get(UserInfoKey.USER_GUID));
        Device chargingDevice = new Device();
        BeanUtils.copyProperties(param,chargingDevice);

        chargingDevice.setInstallDate(DateUtil.parseDate(param.getInstallDate()));

        //查询修改前所属的项目
        ChargingDeviceVo chargingDevice1 = webChargingWrapper.queryByChargingPlieGuid(param.getChargingPlieGuid());
        //调用业务层
        boolean flag = webChargingWrapper.editDevice(chargingDevice, Integer.valueOf(userId));
        if (flag){
            ApiResult.setMessage("修改成功");
            /*userLogService.create(currentUser.getId(), Action.MODIFY.getActionId(), "充电桩设备", "chargingPlieGuid", String.valueOf(param.getChargingPlieGuid()),"修改充电桩设备["+param.getDeviceNo()+"]信息", JSON.toJSONString(param));*/
        } else {
            ApiResult.error("修改失败");
        }
        if (!chargingDevice1.getProjectGuid().equals(param.getProjectGuid())){
            //修改cardlist
            webChargingWrapper.editCardList(chargingDevice,chargingDevice1.getProjectGuid());
            if (chargingDevice.getResult() == 1){
                ApiResult.setMessage("修改成功！");
            }else if (chargingDevice.getResult() == 0){
                ApiResult.setMessage("修改ic卡下发表失败！");
            }
        }
        return ApiResult;
    }

    /**
     * 查询所有项目信息
     * @return
     */
    @SystemControllerLog(description = "查询所有项目信息")
    @GetMapping(value = "/projects")
    @ApiOperation(value = "查询所有项目信息")
    public ApiResult queryProject(HttpServletRequest request){
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID));
        ApiResult result=new ApiResult();
        //调用业务层
        List<ProjectResp> projectResps = projectWrapper.queryAllProject(Integer.valueOf(userId));
        result.setData(projectResps);
        return result;
    }

    @SystemControllerLog(description = "查询充电桩心跳间隔")
    @GetMapping(value = "/HeartStep")
    @ApiOperation(value = "查询充电桩心跳间隔")
    public ApiResult queryHeartStep(HttpSession session, @RequestParam String commNo){
        ApiResult result=new ApiResult("操作成功");
        //查询数据库
        webChargingWrapper.queryHeartStep(session.getId(), commNo);
        return result;
    }

    @SystemControllerLog(description = "设置充电桩心跳间隔")
    @PutMapping(value = "/HeartStep")
    @ApiOperation(value = "设置充电桩心跳间隔")
    public ApiResult setHeartStep(HttpServletRequest request,@Valid @RequestBody ChargeHeartStepParam param){
        ApiResult result=new ApiResult("操作成功");
  /*      String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = (String) entries.get(UserInfoKey.USER_GUID);*/
        String sessionId = request.getSession().getId();
        ChargeHeartStepVo chargeHeartStepVo = new ChargeHeartStepVo();
        BeanUtils.copyProperties(param,chargeHeartStepVo);
        webChargingWrapper.setHeartStep(sessionId, chargeHeartStepVo);
       /* userLogService.create(userId, Action.MODIFY.getActionId(), "充电桩设备端口", "commNo", param.getCommNo(),"设置充电桩["+param.getCommNo()+"]心跳间隔", JSON.toJSONString(param));*/
        return result;
    }

    @SystemControllerLog(description = "查询充电桩阈值")
    @GetMapping(value = "/Threshold")
    @ApiOperation(value = "查询充电桩阈值")
    public ApiResult queryThreshold(HttpSession session, @RequestParam String commNo,@RequestParam String port){
        ApiResult result=new ApiResult("操作成功");
        webChargingWrapper.queryThreshold(session.getId(), commNo, port);
        return result;
    }

    @SystemControllerLog(description = "设置充电桩阈值")
    @PutMapping(value = "/Threshold")
    @ApiOperation(value = "设置充电桩阈值")
    public ApiResult setThreshold(HttpSession session,@Valid @RequestBody ChargeThresholdParam param){
        ApiResult result=new ApiResult("操作成功");
        ChargeThresholdVo chargeThresholdVo = new ChargeThresholdVo();
        BeanUtils.copyProperties(param,chargeThresholdVo);
        webChargingWrapper.setThreshold(session.getId(), chargeThresholdVo);
      /*  userLogService.create(currentUser.getId(), Action.MODIFY.getActionId(), "充电桩设备端口", "commNo+port", param.getCommNo()+param.getPort(),"设置充电桩["+param.getCommNo()+param.getPort()+"]阈值", JSON.toJSONString(param));*/
        return result;
    }

    /**
     * 停用(启用)充电桩
     * @param offOnChargeVo
     * @return
     */
    @SystemControllerLog(description = "停用(启用)充电桩")
    @PutMapping(value = "/offOn")
    @ApiOperation(value = "停用(启用)充电桩")
    public ApiResult offOn(HttpSession session, @RequestBody OffOnChargeVo offOnChargeVo){
        ApiResult result=new ApiResult("操作成功");
        //停止充电操作
        offOnChargeVo.setSessionId(session.getId());
        webChargingWrapper.offOnCharge(offOnChargeVo);
    /*    if (offOnChargeVo.getOnOrOff() == 0){
            userLogService.create(currentUser.getId(), Action.MODIFY.getActionId(), "充电桩设备端口", "commNo+port", offOnChargeVo.getCommNo()+offOnChargeVo.getPort(),"停用充电桩设备["+offOnChargeVo.getCommNo()+"-"+offOnChargeVo.getPort()+"]信息", JSON.toJSONString(offOnChargeVo));
        }else if (offOnChargeVo.getOnOrOff() == 1){
            userLogService.create(currentUser.getId(), Action.MODIFY.getActionId(), "充电桩设备端口", "commNo+port", offOnChargeVo.getCommNo()+offOnChargeVo.getPort(),"启用充电桩设备["+offOnChargeVo.getCommNo()+"-"+offOnChargeVo.getPort()+"]信息", JSON.toJSONString(offOnChargeVo));
        }else if (offOnChargeVo.getOnOrOff() == -1){
            userLogService.create(currentUser.getId(), Action.MODIFY.getActionId(), "充电桩设备端口", "commNo+port", offOnChargeVo.getCommNo()+offOnChargeVo.getPort(),"停充设备["+offOnChargeVo.getCommNo()+"-"+offOnChargeVo.getPort()+"]信息", JSON.toJSONString(offOnChargeVo));
        }*/
        return result;
    }


    /**
     * 充电状态监测—设备统计
     * @return
     */
    @SystemControllerLog(description = "充电状态监测—设备统计")
    @GetMapping(value = "/monitorDevice")
    @ApiOperation(value = "充电状态监测—设备统计")
    public ApiResult monitorDevice(HttpSession session,HttpServletRequest request,@ModelAttribute ChargerDeviceVo queryVo){

        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;

        // 查询用户的组织数据权限
        List<Long> dataOrgNos = orgService.queryDataOrgList(Integer.valueOf(userId));

        if("1".equals(queryVo.getNodeType())){

            String orgNo = queryVo.getNodeId();
            List<Long> childOrgNos = orgService.queryChildren(Long.valueOf(orgNo));
            dataOrgNos.retainAll(childOrgNos);
        }

        queryVo.setOrgNoList(dataOrgNos);
        ApiResult result=new ApiResult();
        //调用业务层
        ChargingCountByRunState count = webChargingWrapper.monitorDeviceCount(queryVo);
        result.setData(count);
        return result;
    }
    /**
     * 充电状态监测—设备详细列表
     * @return
     */
    @SystemControllerLog(description = "充电状态监测—设备详细列表")
    @GetMapping(value = "/monitorDeviceList")
    @ApiOperation(value = "充电状态监测—设备详细列表")
    public ApiResult monitorDeviceList(HttpSession session,HttpServletRequest request,@ModelAttribute ChargerDeviceQueryMerchantVo queryVo){
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        // 查询用户的组织数据权限
        List<Long> dataOrgNos = orgService.queryDataOrgList(Integer.valueOf(userId));

        if("1".equals(queryVo.getNodeType())){

            String orgNo = queryVo.getNodeId();
            List<Long> childOrgNos = orgService.queryChildren(Long.valueOf(orgNo));
            dataOrgNos.retainAll(childOrgNos);
        }
        queryVo.setOrgNoList(new ArrayList(dataOrgNos));
        ApiResult<PageData<MonitorDeviceDto>> result=new ApiResult();
        //调用业务层
        PageData pageData = webChargingWrapper.monitorDeviceList(queryVo);
        result.setData(pageData);
        return result;
    }

    /**
     * 充电状态监测—设备详细列表下载
     * @return
     */
    @SystemControllerLog(description = "充电状态监测—设备详细列表下载")
    @GetMapping(value = "/monitorDeviceListDown")
    public void monitorDeviceListDown(HttpSession session, HttpServletResponse response, HttpServletRequest request, ChargerDeviceQueryMerchantVo queryVo) throws IOException {
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        // 查询用户的组织数据权限
        List<Long> dataOrgNos = orgService.queryDataOrgList(Integer.valueOf(userId));

        if(1==queryVo.getNodeType()){

            String orgNo = queryVo.getNodeId();
            List<Long> childOrgNos = orgService.queryChildren(Long.valueOf(orgNo));
            dataOrgNos.retainAll(childOrgNos);
        }
        queryVo.setOrgNoList(new ArrayList(dataOrgNos));
        //调用业务层
        PageData pageData = webChargingWrapper.monitorDeviceList(queryVo);

        XSSFWorkbook workBook = generateFileServiceWrapper.generateMonitorDeviceListExcel("设备实时数据列表",pageData.getList());
        DownLoadUtil.downExcel(response,workBook);

    }

    /**
     * 充电状态监测--设备统计曲线
     * @param chargingPlieGuid
     * @return
     */
    @SystemControllerLog(description = "充电状态监测--设备统计曲线")
    @GetMapping(value = "/curve")
    @ApiOperation(value = "充电状态监测--设备统计曲线")
    public ApiResult queryCurve(HttpSession session, @RequestParam String chargingPlieGuid,@RequestParam String chargingGuid){
        ApiResult result=new ApiResult();
        //调用业务层
        CurveQueryInfo curveQueryDto = webChargingWrapper.queryCurve(session.getId(),chargingPlieGuid ,chargingGuid,1);
        result.setData(curveQueryDto);
        return result;
    }
    /**
     * 电量电费统计--设备统计曲线
     * @param chargingPlieGuid
     * @return
     */
    @SystemControllerLog(description = "电量电费统计--设备统计曲线")
    @GetMapping(value = "/queryCurveCount")
    @ApiOperation(value = "充电状态监测--设备统计曲线")
    public ApiResult queryCurveCount(HttpSession session, @RequestParam String chargingPlieGuid,@RequestParam String chargingGuid){
        ApiResult result=new ApiResult();
        //调用业务层
        CurveQueryInfo curveQueryDto = webChargingWrapper.queryCurve(session.getId(),chargingPlieGuid ,chargingGuid,2);
        result.setData(curveQueryDto);
        return result;
    }
    @SystemControllerLog(description = "充电状态监测--设备信号质量曲线")
    @GetMapping(value = "/SignalCurve")
    @ApiOperation(value = "充电状态监测--设备信号质量曲线")
    public ApiResult querySignalCurve(@RequestParam String commNo,@RequestParam String signalDate,@RequestParam Integer dataType){
        ApiResult result=new ApiResult();
        //调用业务层
        SignalCurveInfo signalCurveInfo = customerToMerchantService.querySignalCurve(commNo,signalDate,dataType);
        result.setData(signalCurveInfo);
        return result;
    }

    /**
     * 查询每次使用记录心跳列表
     * @param queryVo
     * @return
     */
    @SystemControllerLog(description = "运行中心跳列表查询")
    @GetMapping(value = "/HeartBychargingGuid")
    @ApiOperation(value = "心跳列表查询")
    public ApiResult queryHeartBychargingGuid( ChargerDeviceQueryVo queryVo){
        ApiResult result=new ApiResult<>();
        //查询数据库

        List<HeartDto> heartDtos = customerToMerchantService.queryHeartBychargingGuid(queryVo);
        PageData pageData=new PageData();
        pageData.setList(heartDtos);
        pageData.setTotal(queryVo.getTotal());
        result.setData(pageData);
        return result;
    }

    /**
     * 心跳列表查询
     * @param chargingPlieGuid
     * @return
     */
    @SystemControllerLog(description = "非运行中心跳列表查询")
    @GetMapping(value = "/queryHeartList")
    @ApiOperation(value = "非运行中心跳列表查询")
    public ApiResult queryHeartList(@RequestParam String chargingPlieGuid){
        ApiResult result=new ApiResult();
        //查询数据库
        List<HeartDto> heartDtos = customerToMerchantService.queryHeartList(chargingPlieGuid);
        result.setData(heartDtos);

        return result;
    }


   /**
     * 用户列表
     * @param
     * @return
    */
    @SystemControllerLog(description = "用户列表查询")
    @GetMapping(value = "/customerInfoList")
    @ApiOperation(value = "用户列表查询")
    public ApiResult customerInfoList(HttpSession session,HttpServletRequest request,CustomerInfoListMerchantDto customerInfoListDto){
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        ApiResult result=new ApiResult();
        PageData pageData = deviceService.customerInfoList(customerInfoListDto, Integer.valueOf(userId));
        //查询数据库
        result.setData(pageData);
        return result;
    }

    /**
     * 用户列表
     * @param
     * @return
     */
    @SystemControllerLog(description = "用户列表下载")
    @GetMapping(value = "/customerInfoListDownload")
    @ApiOperation(value = "用户列表下载")
    public void customerInfoListDownload(HttpSession session,HttpServletRequest request,HttpServletResponse response,CustomerInfoListMerchantDto customerInfoListDto) throws IOException {
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        PageData customerInfoListInfo = deviceService.customerInfoList(customerInfoListDto, Integer.valueOf(userId));
        XSSFWorkbook xssfWorkbook = generateFileServiceWrapper.customerInfoListDownload("用户列表", customerInfoListInfo.getList());
        DownLoadUtil.downExcel(response,xssfWorkbook);
    }

    /**
     * 用户列表基础信息
     * @param
     * @return
     */
    @SystemControllerLog(description = "用户列表基础信息查询")
    @GetMapping(value = "/customerInfoDetail")
    @ApiOperation(value = "用户列表基础信息查询")
    public ApiResult customerInfoDetail(@RequestParam("customerGuid") String customerGuid){
        ApiResult result=new ApiResult();
        ChargeCustomerInfoDetailMerchantInfo chargeCustomerInfoDetailInfo = deviceService.customerInfoDetail(customerGuid);
        //查询数据库
        result.setData(chargeCustomerInfoDetailInfo);
        return result;
    }

    /**
     * 使用记录
     * @param
     * @return
     */
    @SystemControllerLog(description = "使用记录")
    @GetMapping(value = "/useRecordList")
    @ApiOperation(value = "使用记录")
    public ApiResult useRecordList( UseRecordListMerchantDto useRecordListDto){
        ApiResult result=new ApiResult();
        PageData useRecordListInfo = deviceService.useRecordList(useRecordListDto);
        result.setData(useRecordListInfo);
        return result;
    }

    /**
     * 使用记录
     * @param
     * @return
     */
    @SystemControllerLog(description = "使用记录下载")
    @GetMapping(value = "/useRecordListDownload")
    @ApiOperation(value = "使用记录下载")
    public void useRecordListDownload(HttpServletResponse response, UseRecordListMerchantDto useRecordListDto) throws IOException {
        PageData useRecordListInfo = deviceService.useRecordList(useRecordListDto);
        XSSFWorkbook xssfWorkbook = generateFileServiceWrapper.useRecordListDownload("使用记录", useRecordListInfo.getList());
        DownLoadUtil.downExcel(response,xssfWorkbook);
    }
    /**
     * 充值记录
     * @param
     * @return
     */
    @SystemControllerLog(description = "充值记录")
    @GetMapping(value = "/chargeRecordList")
    @ApiOperation(value = "充值记录")
    public ApiResult chargeRecordList(HttpSession session,HttpServletRequest request, ChargeRecordListMerchantDto chargeRecordListDto){
        ApiResult result=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        chargeRecordListDto.setUserId(Integer.valueOf(userId));
        PageData pageData = deviceService.chargeRecordList(chargeRecordListDto);
        result.setData(pageData);
        return result;
    }
    /**
     * 充值记录下载
     * @param
     * @return
     */
    @SystemControllerLog(description = "充值记录下载")
    @GetMapping(value = "/chargeRecordListDownload")
    @ApiOperation(value = "充值记录")
    public void chargeRecordListDownload( HttpServletRequest request,HttpSession session,HttpServletResponse response,ChargeRecordListMerchantDto chargeRecordListDto) throws IOException {

        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        chargeRecordListDto.setUserId(Integer.valueOf(userId));
        PageData pageData = deviceService.chargeRecordList(chargeRecordListDto);
        XSSFWorkbook xssfWorkbook = generateFileServiceWrapper.chargeRecordListDownload("充值记录", pageData.getList());
        DownLoadUtil.downExcel(response,xssfWorkbook);
    }
   /** *
     * 总表线损列表
     * @param
     * @return
     */
    @SystemControllerLog(description = "总表线损列表查询")
    @GetMapping(value = "/totalLineLoss")
    @ApiOperation(value = "总表线损列表查询")
    public ApiResult totalLineLoss(HttpSession session,HttpServletRequest request, TotalLineLossDto totalLineLossDto)  {
        ApiResult result=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        // 查询用户的组织数据权限
        List<Long> dataOrgNos = orgService.queryDataOrgList(Integer.valueOf(userId));

        totalLineLossDto.setOrgNoList(dataOrgNos);
        TotalLineLossInfo totalLineLossInfo = deviceService.totalLineLoss(totalLineLossDto);
        //查询数据库
        result.setData(totalLineLossInfo);
        return result;
    }
    /**
     * 总表线损列表
     * @param
     * @return
     */
    @SystemControllerLog(description = "总表线损列表查询下载")
    @GetMapping(value = "/totalLineLossDownload")
    public void totalLineLossDownload(HttpServletRequest request, HttpSession session,HttpServletResponse response, TotalLineLossDto totalLineLossDto) throws IOException {
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        // 查询用户的组织数据权限
        List<Long> dataOrgNos = orgService.queryDataOrgList(Integer.valueOf(userId));
        totalLineLossDto.setOrgNoList(new ArrayList(dataOrgNos));
        TotalLineLossInfo totalLineLossInfo = deviceService.totalLineLoss(totalLineLossDto);
        //查询数据库
        XSSFWorkbook xssfWorkbook = generateFileServiceWrapper.totalLineLossDownload("总表线损列表", totalLineLossInfo);
        DownLoadUtil.downExcel(response,xssfWorkbook);
    }

    /**
     * 拉黑用户
     * @param
     * @return
     */
    @SystemControllerLog(description = "拉黑用户")
    @PutMapping(value = "/setCustomerState")
    @ApiOperation(value = "拉黑用户")
    public ApiResult setCustomerState(@RequestParam("customerGuid") String customerGuid,
                                   @RequestParam("customerState")Integer customerState){
        ApiResult result=new ApiResult();
        Integer integer = customerToMerchantService.setCustomerState(customerGuid,customerState);
        if(integer!=1){
            result.error("更新失败");
        }
        return result;
    }
    /**
     * 提现记录
     * @param
     * @return
     */
    @SystemControllerLog(description = "提现记录")
    @GetMapping(value = "/withdrawCashList")
    @ApiOperation(value = "提现记录")
    public ApiResult withdrawCashList(HttpSession session,HttpServletRequest request, WithdrawCashListDto withdrawCashListDto){
        ApiResult result=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        withdrawCashListDto.setUserId(Integer.valueOf(userId));
        PageData useRecordListInfo = deviceService.withdrawCashList(withdrawCashListDto);
        result.setData(useRecordListInfo);
        return result;
    }




    /**
     * 每日充电线路分析详情
     * @param
     * @return
     */
    @SystemControllerLog(description = "每日充电线路分析详情")
    @GetMapping(value = "/dayLineLoss")
    @ApiOperation(value = "每日充电线路分析详情")
    public ApiResult dayLineLoss( DayLineLossMerchantDto dayLineLossDto) throws ParseException {
        ApiResult result=new ApiResult();
        DayLineLossMerchantInfo dayLineLossMerchantInfo = deviceService.dayLineLoss(dayLineLossDto);
        //查询数据库
        result.setData(dayLineLossMerchantInfo);
        return result;
    }

    /**
     * 充电电量电费统计-统计数据详情柱状图
     * @return
     */
    @SystemControllerLog(description = "充电电量电费统计-统计数据详情柱状图")
    @GetMapping(value = "/electricCount")
    @ApiOperation(value = "充电电量电费统计-统计数据详情柱状图")
    public ApiResult electricCount(HttpServletRequest request,HttpSession session, ElectricCountQueryMerchantVo queryVo){
        ApiResult result=new ApiResult();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        //调用业务层
        List<Long> longs = orgService.queryDataOrgList(Integer.valueOf(userId));
        queryVo.setOrgNoList(new ArrayList(longs));
        PowerAndFeeCountInfo powerAndFeeCountInfo = deviceService.queryPowerAndFeeCount(queryVo);
        result.setData(powerAndFeeCountInfo);
        return result;
    }

    /**
     * 电量电费统计列表
     * @param queryVo
     * @return
     */
    @SystemControllerLog(description = "电量电费统计列表")
    @GetMapping(value = "/electricCountList")
    @ApiOperation(value = "电量电费统计列表")
    public ApiResult electricCountList(HttpSession session, ChargerDeviceQueryVo queryVo,HttpServletRequest request){
        ApiResult result=new ApiResult();
        //查询数据库
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        //调用业务层
        List<Long> longs = orgService.queryDataOrgList(Integer.valueOf(userId));
        queryVo.setOrgNoList(new ArrayList(longs));
        ElectricCountDto electricCountDto = customerWebService.queryCountList(queryVo);
        result.setData(electricCountDto);
        return result;
    }

  /*  *//**
     * 电量电费统计--站点使用情况统计
     * @param queryVo
     * @return
     *//*
    @SystemControllerLog(description = "电量电费统计--站点使用情况统计")
    @RequestMapping(value = "/projectUseCountList")
    @ApiOperation(value = "电量电费统计--站点使用情况统计")
    public ApiResult projectUseCountList(HttpServletRequest request,HttpSession session,@RequestBody ChargerSchemeQueryVo queryVo){
        ApiResult result=new ApiResult();
        //查询数据库
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
        //调用业务层
        List<Long> longs = orgService.queryDataOrgList(Integer.valueOf(userId));
        queryVo.setOrgNoList(longs);
        ProjectUseCountDto projectUseCountDto = chargingDeviceService.projectUseCountList(queryVo);
        result.setData(projectUseCountDto);
        result.setTotal(queryVo.getTotal());
        return JSON.toJSONString(result);
    }*/
}
