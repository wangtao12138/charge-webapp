package cn.com.cdboost.charge.webapi.service;

import cn.com.cdboost.charge.customer.vo.info.*;
import cn.com.cdboost.charge.merchant.vo.dto.CustomerInfoListMerchantInfo;
import cn.com.cdboost.charge.merchant.vo.dto.ChargingProjectDto;
import cn.com.cdboost.charge.merchant.vo.dto.MonitorDeviceDto;
import cn.com.cdboost.charge.merchant.vo.info.ChargingDeviceVo;
import cn.com.cdboost.charge.merchant.vo.info.TotalLineLossInfo;
import cn.com.cdboost.charge.webapi.dto.info.ChargingUseDetailedResp;
import cn.com.cdboost.charge.webapi.dto.info.ICCardResp;
import cn.com.cdboost.charge.webapi.dto.info.ProjectResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * excel文档生成服务接口
 */
public interface GenerateFileServiceWrapper {

	XSSFWorkbook generateChargingDeviceListExcel(String name, List<ChargingDeviceVo> chargingDevices);

	//项目列表
	XSSFWorkbook generateChargingProjectListExcel(String name, List<ChargingProjectDto> chargingProjectDtos);

	//方案使用列表
	XSSFWorkbook generateChargingSchemeUseListExcel(String name, List<UseDetailListInfo> useList);

	//IC卡列表下载
	XSSFWorkbook generateChargingICCardListExcel(String name, List<ICCardResp> chargingICCardDtos);

	//IC卡充值记录
	XSSFWorkbook generateChargingICPayListExcel(String name, List<CustomerPayCardInfo> chargingICPayDtos);

	//充电桩IC卡使用记录列表
	XSSFWorkbook generateChargingICUseListExcel(String name, List<IcCardUseDetailedVo> list);

	XSSFWorkbook customerInfoListDownload(String name, List<CustomerInfoListMerchantInfo> list);


	XSSFWorkbook generateMonitorDeviceListExcel(String name, List<MonitorDeviceDto> monitorDeviceDtos);

	XSSFWorkbook useRecordListDownload(String name, List<UseRecordListInfo> list);

	XSSFWorkbook chargeRecordListDownload(String name, List<ChargeRecordListInfo> chargeRecordListInfos);

	XSSFWorkbook totalLineLossDownload(String name, TotalLineLossInfo totalLineLossInfo);
}

