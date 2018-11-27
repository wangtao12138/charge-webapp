package cn.com.cdboost.charge.webapi.service;



import cn.com.cdboost.charge.base.exception.BusinessException;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.vo.info.CustomerPayCardInfo;
import cn.com.cdboost.charge.webapi.dto.info.*;
import cn.com.cdboost.charge.webapi.dto.param.ChargerICCardAddParam;
import cn.com.cdboost.charge.webapi.dto.param.ChargerICCardEditParam;
import cn.com.cdboost.charge.webapi.dto.param.ICCardQueryParam;
import cn.com.cdboost.charge.webapi.dto.param.SendAndReadCardParam;

import javax.validation.Valid;
import java.util.List;

/**
 * ic卡接口
 */
public interface IcCardWrapper {

    /**
     * 添加ic卡
     * @param param
     */
    void addCard(ChargerICCardAddParam param) throws BusinessException;


    /**
     * 查询ic卡列表
     * @param param
     * @return
     */
    PageData<ICCardResp> queryList(ICCardQueryParam param, Integer userId);

    /**
     * 添加ic卡下发表
     * @param param
     * @param result
     */
    void addCardList(ChargerICCardAddParam param, ApiResult result);

    /**
     * 编辑ic卡
     * @param param
     */
    void editCard(ChargerICCardEditParam param) throws BusinessException;

    /**
     * 编辑ic卡下发表
     * @param param
     * @param projectGuid
     * @param result
     */
    void editCardList(ChargerICCardEditParam param, String projectGuid, ApiResult result);

    /**
     * 查询ic卡下发列表
     * @param queryVo
     * @return
     */
    PageData<CardListResp> queryCardSendList(ICCardQueryParam queryVo);

    /**
     * 查询ic卡充值记录
     * @param queryVo
     * @return
     */
    PageData<CustomerPayCardInfo> queryICCardPayList(ICCardQueryParam queryVo);

    /**
     * web端查询ic卡详情
     * @param cardId
     * @return
     */
    ICCardDetailResp queryCardCustomerDetail(String cardId);

    /**
     * 下发充电IC卡
     * @param param
     * @param sessionId
     */
    void sendCard(List<SendAndReadCardParam> param, String sessionId);

    /**
     * 读取IC卡情况
     * @param param
     * @param sessionId
     */
    void readCard(List<SendAndReadCardParam> param, String sessionId);

    /**
     * 清除设备ic卡信息
     * @param commNos
     * @param sessionId
     */
    void clearCard(List<String> commNos, String sessionId);
}
