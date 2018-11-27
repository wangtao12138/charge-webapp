package cn.com.cdboost.charge.webapi.service.Impl;


import cn.com.cdboost.charge.customer.vo.info.*;
import cn.com.cdboost.charge.merchant.vo.dto.CustomerInfoListMerchantInfo;
import cn.com.cdboost.charge.merchant.vo.dto.ChargingProjectDto;
import cn.com.cdboost.charge.merchant.vo.dto.MonitorDeviceDto;
import cn.com.cdboost.charge.merchant.vo.info.ChargingDeviceVo;
import cn.com.cdboost.charge.merchant.vo.info.LineLossList;
import cn.com.cdboost.charge.merchant.vo.info.TotalLineLossInfo;
import cn.com.cdboost.charge.webapi.constants.ChargeConstant;
import cn.com.cdboost.charge.webapi.constants.ChargingEnum;
import cn.com.cdboost.charge.webapi.dto.info.ChargingUseDetailedResp;
import cn.com.cdboost.charge.webapi.dto.info.ICCardResp;
import cn.com.cdboost.charge.webapi.dto.info.ProjectResp;
import cn.com.cdboost.charge.webapi.service.GenerateFileServiceWrapper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * excel文档生成服务接口实现类
 */
@Service
public class GenerateFileServiceImpl implements GenerateFileServiceWrapper {

    @Override
    public XSSFWorkbook generateChargingDeviceListExcel(String name, List<ChargingDeviceVo> chargingDevices) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备编号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备程序版本"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("端口1"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("端口2"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("通信方式"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("通信编号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("安装时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("安装地址"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("备注"));
        cellIndex++;


        XSSFRow row;
        XSSFCell cell;
        ChargingDeviceVo chargingDeviceDto;
        if(!CollectionUtils.isEmpty(chargingDevices)){
            for (int i = 0; i < chargingDevices.size(); i++) {
                chargingDeviceDto = chargingDevices.get(i);
                row = sheet.createRow(i + 2);

                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingDeviceDto.getDeviceNo()));

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingDeviceDto.getVer())));

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                if (chargingDeviceDto.getOnline().equals(ChargeConstant.DeviceOnlineStatus.ONLINE.getStatus())){
                    if(chargingDeviceDto.getRunState1().equals(ChargingEnum.FREE_STATE.getKey())){
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.FREE_STATE.getMessage()));
                    }else if (chargingDeviceDto.getRunState1().equals(ChargingEnum.ON_STATE.getKey())){
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.ON_STATE.getMessage()));
                    }else if (chargingDeviceDto.getRunState1().equals(ChargingEnum.OFF_STATE.getKey())){
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.OFF_STATE.getMessage()));
                    }else if (chargingDeviceDto.getRunState1().equals(ChargingEnum.ERROR_STATE.getKey())){
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.ERROR_STATE.getMessage()));
                    }
                }else {
                    cell.setCellValue(new XSSFRichTextString("离线"));
                }


                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                if (chargingDeviceDto.getOnline().equals(ChargeConstant.DeviceOnlineStatus.ONLINE.getStatus())) {
                    if (chargingDeviceDto.getRunState2().equals(ChargingEnum.FREE_STATE.getKey())) {
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.FREE_STATE.getMessage()));
                    } else if (chargingDeviceDto.getRunState2().equals(ChargingEnum.ON_STATE.getKey())) {
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.ON_STATE.getMessage()));
                    } else if (chargingDeviceDto.getRunState2().equals(ChargingEnum.OFF_STATE.getKey())) {
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.OFF_STATE.getMessage()));
                    } else if (chargingDeviceDto.getRunState2().equals(ChargingEnum.ERROR_STATE.getKey())) {
                        cell.setCellValue(new XSSFRichTextString(ChargingEnum.ERROR_STATE.getMessage()));
                    }
                }else {
                    cell.setCellValue(new XSSFRichTextString("离线"));
                }

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingDeviceDto.getComMethodName()));

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingDeviceDto.getCommNo()));

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingDeviceDto.getInstallDate()));

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingDeviceDto.getInstallAddr()));

                cell = row.createCell(8);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingDeviceDto.getRemark()));

            }
        }
        return workbook;
    }
    @Override
    public XSSFWorkbook generateChargingProjectListExcel(String name, List<ChargingProjectDto> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("站点名称"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备数量"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("小区名称"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("组织机构"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("物业公司"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("创建时间"));
        cellIndex++;


        XSSFRow row;
        XSSFCell cell;
        ChargingProjectDto chargingProjectDto;

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                chargingProjectDto = list.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingProjectDto.getProjectName()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingProjectDto.getDeviceNum()==null?0:chargingProjectDto.getDeviceNum())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingProjectDto.getCommunityName()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingProjectDto.getOrgName()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingProjectDto.getCompanyName()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingProjectDto.getCreateTime()));
                index++;
            }
        }
        return workbook;
    }
    @Override
    public XSSFWorkbook generateChargingSchemeUseListExcel(String name, List<UseDetailListInfo> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备编号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("端口号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("计费方式"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值费用(元)"));
        cellIndex++;

        /*cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("使用电量(KW·H)"));
        cellIndex++;*/

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("盈利费用"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("状态"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充电时长(分钟)"));
        cellIndex++;


        XSSFRow row;
        XSSFCell cell;
        UseDetailListInfo useDetailedDto;

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                useDetailedDto = list.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(useDetailedDto.getDeviceNo()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(useDetailedDto.getPort()));
                index++;


                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                if (useDetailedDto.getPayCategory() == ChargeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getDesc()));
                } else if (useDetailedDto.getPayCategory() == ChargeConstant.SchemePayCategory.MONTH_RECHARGE.getType()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.MONTH_RECHARGE.getDesc()));
                } else if (useDetailedDto.getPayCategory() == ChargeConstant.SchemePayCategory.RECHARGE_FULL.getType()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.RECHARGE_FULL.getDesc()));
                }else if (useDetailedDto.getPayCategory() == ChargeConstant.SchemePayCategory.BALANCE_RECHARGE.getType()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.BALANCE_RECHARGE.getDesc()));
                }else if (useDetailedDto.getPayCategory() == ChargeConstant.SchemePayCategory.IC_RECHARGE.getType()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.IC_RECHARGE.getDesc()));
                }
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                if (useDetailedDto.getDeductMoney() != null){
                    cell.setCellValue(new XSSFRichTextString(String.valueOf(useDetailedDto.getDeductMoney())));
                }else {
                    cell.setCellValue(new XSSFRichTextString("0"));
                }

                index++;

                /*cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useDetailedDto.getUsePower())));
                index++;*/

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useDetailedDto.getProfitable())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                if (useDetailedDto.getState() == ChargeConstant.ChargeState.CHARGING.getState()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.ChargeState.CHARGING.getDesc()));
                }else if (useDetailedDto.getState() == ChargeConstant.ChargeState.COMPLETED.getState()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.ChargeState.COMPLETED.getDesc()));
                }
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useDetailedDto.getUseTime())));
                index++;
            }
        }
        return workbook;
    }
    @Override
    public XSSFWorkbook generateChargingICCardListExcel(String name, List<ICCardResp> chargingICCardDtos) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("序号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("状态"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("卡号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("微信ID"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("支付宝ID"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("剩余金额（元）"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("使用次数"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值次数"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("创建时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("更新时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("备注"));
        cellIndex++;

        XSSFRow row;
        XSSFCell cell;
        ICCardResp chargingICCardDto;

        if (!CollectionUtils.isEmpty(chargingICCardDtos)) {
            for (int i = 0; i < chargingICCardDtos.size(); i++) {
                chargingICCardDto = chargingICCardDtos.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(i+1)));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                if (chargingICCardDto.getCardState() == 0){
                    cell.setCellValue(new XSSFRichTextString("初始"));
                } else if (chargingICCardDto.getCardState() == 1){
                    cell.setCellValue(new XSSFRichTextString("启用"));
                }else if (chargingICCardDto.getCardState() == 2){
                    cell.setCellValue(new XSSFRichTextString("停用"));
                }
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingICCardDto.getCardId()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingICCardDto.getWebcharNo()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingICCardDto.getAlipayUserId()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICCardDto.getRemainAmount())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICCardDto.getUseCnt())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICCardDto.getPayCnt())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICCardDto.getCreateTime())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICCardDto.getUpdateTime())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingICCardDto.getRemark()));
                index++;
            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook generateChargingICPayListExcel(String name, List<CustomerPayCardInfo> chargingICPayDtos) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("序号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值金额"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("账户余额"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值方式"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("操作人员"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值时间"));
        cellIndex++;


        XSSFRow row;
        XSSFCell cell;
        CustomerPayCardInfo chargingICPayDto;

        if (!CollectionUtils.isEmpty(chargingICPayDtos)) {
            for (int i = 0; i < chargingICPayDtos.size(); i++) {
                chargingICPayDto = chargingICPayDtos.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(i+1)));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICPayDto.getPayMoney())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICPayDto.getAfterRemainAmount())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                if (chargingICPayDto.getPayWay() == 1){
                    cell.setCellValue(new XSSFRichTextString("微信"));
                }else if (chargingICPayDto.getPayWay() == 2){
                    cell.setCellValue(new XSSFRichTextString("支付宝"));
                }else if (chargingICPayDto.getPayWay() == 3){
                    cell.setCellValue(new XSSFRichTextString("现金"));
                }else if (chargingICPayDto.getPayWay() == 4){
                    cell.setCellValue(new XSSFRichTextString("余额"));
                }
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingICPayDto.getCustomerGuid()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICPayDto.getCreateTime())));
                index++;

            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook generateChargingICUseListExcel(String name, List<IcCardUseDetailedVo> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("序号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备编号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("端口号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("时长（分钟）"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("开始时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("结束时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("扣费金额（元）"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("余额（元）"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("地点"));
        cellIndex++;

        XSSFRow row;
        XSSFCell cell;
        IcCardUseDetailedVo chargingICUseDto;

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                chargingICUseDto = list.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(i+1)));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingICUseDto.getDeviceNo()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargingICUseDto.getPort()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICUseDto.getChargingTime())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICUseDto.getStartTime())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICUseDto.getEndTime())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICUseDto.getDeductMoney())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICUseDto.getAfterRemainAmount())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargingICUseDto.getInstallAddr())));
                index++;
            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook customerInfoListDownload(String name, List<CustomerInfoListMerchantInfo> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("序号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("状态"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("手机号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("账户余额(元)"));
        cellIndex++;

        /*cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("月卡剩余次数"));
        cellIndex++;*/

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充电次数"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("支付宝(昵称)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("微信(昵称)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("IC卡"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("更新时间"));
        cellIndex++;



        XSSFRow row;
        XSSFCell cell;
        CustomerInfoListMerchantInfo customerInfoListInfo;

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                customerInfoListInfo = list.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(i+1)));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(customerInfoListInfo.getCustomerState()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(customerInfoListInfo.getCustomerContact()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(customerInfoListInfo.getRemainAmount())));
                index++;

                /*cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(customerInfoListInfo.getRemainCnt())));
                index++;*/

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(customerInfoListInfo.getChargeCount())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(customerInfoListInfo.getAlipayNickName())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(customerInfoListInfo.getCustomerName())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(customerInfoListInfo.getCardId())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(customerInfoListInfo.getUpdateTime())));
                index++;
            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook generateMonitorDeviceListExcel(String name, List<MonitorDeviceDto> monitorDeviceDtos) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备编号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("端口号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备通信状态"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备运行状态"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充电时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充电时长(分钟)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("剩余时长(分钟)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值方式"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("车辆类别"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("上行信号强度"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("上行信噪比"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("下行信号强度"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("下行信噪比"));
        cellIndex++;


        XSSFRow row;
        XSSFCell cell;
        MonitorDeviceDto monitorDeviceDto;
        if(!CollectionUtils.isEmpty(monitorDeviceDtos)){
            for (int i = 0; i < monitorDeviceDtos.size(); i++) {
                monitorDeviceDto = monitorDeviceDtos.get(i);
                row = sheet.createRow(i + 2);

                /*cell = row.createCell(0);
                cell.setCellStyle(cellStyle);*/

                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(monitorDeviceDto.getDeviceNo()));


                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(monitorDeviceDto.getPort()));

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getOnline() == ChargeConstant.DeviceOnlineStatus.ONLINE.getStatus()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.DeviceOnlineStatus.ONLINE.getDesc()));
                }else if (monitorDeviceDto.getOnline() == ChargeConstant.DeviceOnlineStatus.OFFLINE.getStatus()){
                    cell.setCellValue(new XSSFRichTextString(ChargeConstant.DeviceOnlineStatus.OFFLINE.getDesc()));
                }

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                if(monitorDeviceDto.getRunState() == ChargingEnum.FREE_STATE.getKey()){
                    cell.setCellValue(new XSSFRichTextString(ChargingEnum.FREE_STATE.getMessage()));
                }else if (monitorDeviceDto.getRunState() == ChargingEnum.ON_STATE.getKey()){
                    cell.setCellValue(new XSSFRichTextString(ChargingEnum.ON_STATE.getMessage()));
                }else if (monitorDeviceDto.getRunState() == ChargingEnum.OFF_STATE.getKey()){
                    cell.setCellValue(new XSSFRichTextString(ChargingEnum.OFF_STATE.getMessage()));
                }else if (monitorDeviceDto.getRunState() == ChargingEnum.ERROR_STATE.getKey()){
                    cell.setCellValue(new XSSFRichTextString(ChargingEnum.ERROR_STATE.getMessage()));
                }


                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(monitorDeviceDto.getStartTime()));


                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getUseTime() == null){
                    cell.setCellValue(new XSSFRichTextString("0"));
                }else {
                    cell.setCellValue(new XSSFRichTextString(monitorDeviceDto.getUseTime().toString()));
                }


                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getRemainTime() != null){
                    cell.setCellValue(new XSSFRichTextString(String.valueOf(monitorDeviceDto.getRemainTime().setScale(0, BigDecimal.ROUND_HALF_DOWN))));
                }else {
                    cell.setCellValue(new XSSFRichTextString(""));
                }


                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getPayCategory() != null){
                    if (monitorDeviceDto.getPayCategory() == ChargeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType()){
                        cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getDesc()));
                    } else if (monitorDeviceDto.getPayCategory() == ChargeConstant.SchemePayCategory.MONTH_RECHARGE.getType()){
                        cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.MONTH_RECHARGE.getDesc()));
                    }else if (monitorDeviceDto.getPayCategory() == ChargeConstant.SchemePayCategory.RECHARGE_FULL.getType()){
                        cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.RECHARGE_FULL.getDesc()));
                    }else if (monitorDeviceDto.getPayCategory() == ChargeConstant.SchemePayCategory.BALANCE_RECHARGE.getType()){
                        cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.BALANCE_RECHARGE.getDesc()));
                    }else if (monitorDeviceDto.getPayCategory() == ChargeConstant.SchemePayCategory.IC_RECHARGE.getType()){
                        cell.setCellValue(new XSSFRichTextString(ChargeConstant.SchemePayCategory.IC_RECHARGE.getDesc()));
                    }
                } else {
                    cell.setCellValue(new XSSFRichTextString(""));
                }


                cell = row.createCell(8);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getCarCategory() != null){
                    if (monitorDeviceDto.getCarCategory() ==1){
                        cell.setCellValue(new XSSFRichTextString("电瓶车"));
                    } else if (monitorDeviceDto.getCarCategory() ==2){
                        cell.setCellValue(new XSSFRichTextString("电动车"));
                    }
                }else {
                    cell.setCellValue(new XSSFRichTextString("电瓶车"));
                }

                cell = row.createCell(9);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getUpRssi() == null){
                    cell.setCellValue(new XSSFRichTextString(""));
                }else {
                    cell.setCellValue(new XSSFRichTextString(String.valueOf(monitorDeviceDto.getUpRssi())));
                }

                cell = row.createCell(10);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getUpSnr() == null){
                    cell.setCellValue(new XSSFRichTextString(""));
                }else {
                    cell.setCellValue(new XSSFRichTextString(String.valueOf(monitorDeviceDto.getUpSnr())));
                }

                cell = row.createCell(11);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getDownRssi() == null){
                    cell.setCellValue(new XSSFRichTextString(""));
                }else {
                    cell.setCellValue(new XSSFRichTextString(String.valueOf(monitorDeviceDto.getDownRssi())));
                }

                cell = row.createCell(12);
                cell.setCellStyle(cellStyle);
                if (monitorDeviceDto.getDownSnr() == null){
                    cell.setCellValue(new XSSFRichTextString(""));
                }else {
                    cell.setCellValue(new XSSFRichTextString(String.valueOf(monitorDeviceDto.getDownSnr())));
                }

            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook useRecordListDownload(String name, List<UseRecordListInfo> list) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("序号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("状态"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("方案"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("方式"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备编号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("端口号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("电量"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充电时长"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("开始时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("结束时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备地址"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("电价"));
        cellIndex++;

        XSSFRow row;
        XSSFCell cell;
        UseRecordListInfo useRecordListInfo;

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                useRecordListInfo = list.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(i+1)));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(useRecordListInfo.getDate()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(useRecordListInfo.getDeviceState()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getPayCategory())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getPayMethod())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getDeviceNo())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getPort())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getDeviceElect())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getUseTime())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getStartDate())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getEndDate())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getInstallAddress())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(useRecordListInfo.getPrice())));
                index++;
            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook chargeRecordListDownload(String name, List<ChargeRecordListInfo> chargeRecordListInfos) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("序号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("状态"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值时间"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值方式"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充值金额"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("账户余额"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("人员ID"));
        cellIndex++;

        XSSFRow row;
        XSSFCell cell;
        ChargeRecordListInfo chargeRecordListInfo;

        if (!CollectionUtils.isEmpty(chargeRecordListInfos)) {
            for (int i = 0; i < chargeRecordListInfos.size(); i++) {
                chargeRecordListInfo = chargeRecordListInfos.get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(i+1)));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargeRecordListInfo.getPayState()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargeRecordListInfo.getDate()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(chargeRecordListInfo.getPayMethod()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargeRecordListInfo.getPayMoney())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargeRecordListInfo.getRemainAmount())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(chargeRecordListInfo.getUserId())));
                index++;

            }
        }
        return workbook;
    }

    @Override
    public XSSFWorkbook totalLineLossDownload(String name, TotalLineLossInfo totalLineLossInfo) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle cellStyleTitle = workbook.createCellStyle();
        cellStyleTitle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleTitle.setWrapText(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        cellStyleTitle.setFont(font);


        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, name);

        XSSFRow row1 = sheet.createRow(1);
        int cellIndex = 0;
        XSSFCell cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("日期"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("总表表号"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("总表起度"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("总表止度"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("电表电量(kW·h)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("设备电量(kW·h)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("损耗电量(kW·h)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("损耗率(%)"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("充电次数"));
        cellIndex++;

        cell1 = row1.createCell(cellIndex);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("位置"));
        cellIndex++;

        XSSFRow row;
        XSSFCell cell;
        LineLossList lineLossList;
        int total=0;
        if (!CollectionUtils.isEmpty(totalLineLossInfo.getList())) {
            for (int i = 0; i < totalLineLossInfo.getList().size(); i++) {
                lineLossList = totalLineLossInfo.getList().get(i);
                row = sheet.createRow(i + 2);
                int index = 0;
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getDate())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(lineLossList.getDeviceNo()));

                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(lineLossList.getLastReadValue()));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getReadValue())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getMeterElect())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getDeviceElect())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getLossPower())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getLossRate())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getChargeTime())));
                index++;

                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(new XSSFRichTextString(String.valueOf(lineLossList.getInstallAddr())));
                index++;
                total++;
            }
        }
        row1 = sheet.createRow(total+2);
        CellRangeAddress callRangeAddress = new CellRangeAddress(total+2,total+2,0,3);
        sheet.addMergedRegion(callRangeAddress);

        cell1 = row1.createCell(0);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString("总计"));

        cell1 = row1.createCell(4);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString(String.valueOf(totalLineLossInfo.getStatistics().getMeterElect())));

        cell1 = row1.createCell(5);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString(String.valueOf(totalLineLossInfo.getStatistics().getDeviceElect())));

        cell1 = row1.createCell(6);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString(String.valueOf(totalLineLossInfo.getStatistics().getLossPower())));

        cell1 = row1.createCell(7);
        cell1.setCellStyle(cellStyleTitle);
        cell1.setCellValue(new XSSFRichTextString(String.valueOf(totalLineLossInfo.getStatistics().getLossRate())));

        return workbook;
    }
}
