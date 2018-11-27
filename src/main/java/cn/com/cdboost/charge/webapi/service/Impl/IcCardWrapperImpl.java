package cn.com.cdboost.charge.webapi.service.Impl;


import cn.com.cdboost.charge.base.exception.BusinessException;
import cn.com.cdboost.charge.base.info.Afn26Object;
import cn.com.cdboost.charge.base.info.Afn27Object;
import cn.com.cdboost.charge.base.producer.RabbitmqProducer;
import cn.com.cdboost.charge.base.util.UuidUtil;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.dubbo.CustomerService;
import cn.com.cdboost.charge.customer.dubbo.CustomerToMerchantService;
import cn.com.cdboost.charge.customer.vo.info.CustomerBaseInfo;
import cn.com.cdboost.charge.customer.vo.info.CustomerPayCardInfo;
import cn.com.cdboost.charge.customer.vo.param.CustomerQueryParam;
import cn.com.cdboost.charge.customer.vo.param.IcCardCustomerParam;
import cn.com.cdboost.charge.merchant.dubbo.IcCardService;
import cn.com.cdboost.charge.merchant.dubbo.ProjectService;
import cn.com.cdboost.charge.merchant.vo.dto.IcCardListDto;
import cn.com.cdboost.charge.merchant.vo.info.*;
import cn.com.cdboost.charge.merchant.vo.param.ChargerICCardListParam;
import cn.com.cdboost.charge.merchant.vo.param.IcCardParam;
import cn.com.cdboost.charge.settlement.dubbo.AccountService;
import cn.com.cdboost.charge.settlement.vo.info.UserAccountInfo;
import cn.com.cdboost.charge.settlement.vo.param.AccountQueryVo;
import cn.com.cdboost.charge.webapi.dto.info.*;
import cn.com.cdboost.charge.webapi.dto.param.ChargerICCardAddParam;
import cn.com.cdboost.charge.webapi.dto.param.ChargerICCardEditParam;
import cn.com.cdboost.charge.webapi.dto.param.ICCardQueryParam;
import cn.com.cdboost.charge.webapi.dto.param.SendAndReadCardParam;
import cn.com.cdboost.charge.webapi.service.IcCardWrapper;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class IcCardWrapperImpl implements IcCardWrapper {
    @Reference(version = "1.0")
    private IcCardService icCardService;
    @Reference(version = "1.0")
    private CustomerToMerchantService customerToMerchantService;
    @Reference(version = "1.0")
    private ProjectService projectService;
    @Reference(version = "1.0")
    private AccountService accountService;
    @Reference(version = "1.0")
    private CustomerService customerService;
    @Resource
    private RabbitmqProducer rabbitmqProducer;


    @Override
    public void addCard(ChargerICCardAddParam param) throws BusinessException {
        ChargerICCardAddVo addVo = new ChargerICCardAddVo();
        BeanUtils.copyProperties(param,addVo);
        icCardService.addCard(addVo);
    }

    @Override
    public void addCardList(ChargerICCardAddParam param, ApiResult result) {
        //调用cardlist存储过程
        ChargerICCardListParam listParam = new ChargerICCardListParam();
        listParam.setProjectGuid(param.getProjectGuid());
        listParam.setCardId(param.getCardId());
        icCardService.addCardList(listParam);
    }

    @Override
    public PageData<ICCardResp> queryList(ICCardQueryParam param, Integer userId) {
        IcCardParam cardParam = new IcCardParam();
        BeanUtils.copyProperties(param,cardParam);
        //查询组织下所有站点
        List<ProjectVo> projectVos = projectService.queryAllProject(userId);
        Map<String,ProjectVo> projectVoMap = Maps.newHashMap();
        List<String> projectGuids = Lists.newArrayList();
        for (ProjectVo projectVo : projectVos) {
            projectGuids.add(projectVo.getProjectGuid());
            projectVoMap.put(projectVo.getProjectGuid(),projectVo);
        }
        cardParam.setProjectGuids(projectGuids);

        PageData<IcCardListDto> icCardListDtoPageData = icCardService.queryIcList(cardParam);
        PageData<ICCardResp> respPageData = new PageData<>();
        List<ICCardResp> respList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(icCardListDtoPageData.getList())){
            for (IcCardListDto dto : icCardListDtoPageData.getList()) {
                ProjectVo projectVo = projectVoMap.get(dto.getProjectGuid());
                ICCardResp resp = new ICCardResp();
                BeanUtils.copyProperties(dto,resp);
                resp.setProjectName(projectVo.getProjectName());
                respList.add(resp);
            }
            respPageData.setList(respList);
            respPageData.setTotal(icCardListDtoPageData.getTotal());
        }
        return respPageData;
    }

    @Override
    public void editCard(ChargerICCardEditParam param) throws BusinessException {
        ChargerICCardEditVo editVo = new ChargerICCardEditVo();
        BeanUtils.copyProperties(param,editVo);
        icCardService.editCard(editVo);
    }

    @Override
    public void editCardList(ChargerICCardEditParam param, String projectGuid, ApiResult result) {
        ChargerICCardListParam listParam = new ChargerICCardListParam();
        icCardService.editCardList(listParam);
    }

    @Override
    public PageData<CardListResp> queryCardSendList(ICCardQueryParam queryVo) {
        PageData<CardListResp> pageData = new PageData<>();
        IcCardParam param = new IcCardParam();
        BeanUtils.copyProperties(queryVo,param);
        PageData<CardSendListVo> cardSendListVoPageData = icCardService.queryIcSendList(param);
        List<CardListResp> respList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cardSendListVoPageData.getList())){
            for (CardSendListVo cardSendListVo : cardSendListVoPageData.getList()) {
                CardListResp resp = new CardListResp();
                BeanUtils.copyProperties(cardSendListVo,resp);
                respList.add(resp);
            }
            pageData.setTotal(cardSendListVoPageData.getTotal());
            pageData.setList(respList);
        }
        return pageData;
    }

    @Override
    public PageData<CustomerPayCardInfo> queryICCardPayList(ICCardQueryParam queryVo) {
        IcCardCustomerParam param = new IcCardCustomerParam();
        BeanUtils.copyProperties(queryVo,param);
        PageData<CustomerPayCardInfo> customerPayCardInfoPageData = customerToMerchantService.queryICCardPayList(param);
        return customerPayCardInfoPageData;
    }

    @Override
    public ICCardDetailResp queryCardCustomerDetail(String cardId) {
        ICCardDetailResp cardCustomerDetailVo = new ICCardDetailResp();
        IcCardDetailVo cardDetailVo = icCardService.queryIcDetail(cardId);
        BeanUtils.copyProperties(cardDetailVo,cardCustomerDetailVo);
        AccountQueryVo accountQueryVo = new AccountQueryVo();
        if (!StringUtils.isEmpty(cardDetailVo.getCustomerGuid())){
            accountQueryVo.setUserGuid(cardDetailVo.getCustomerGuid());
            UserAccountInfo userAccountInfo = accountService.queryAccountInfo(accountQueryVo);
            cardCustomerDetailVo.setCusRemainAmount(userAccountInfo.getRemainAmount());
            CustomerQueryParam queryParam = new CustomerQueryParam();
            queryParam.setCustomerGuid(cardDetailVo.getCustomerGuid());
            CustomerBaseInfo customerBaseInfo = customerService.queryBaseInfo(queryParam);
            if (StringUtils.isEmpty(customerBaseInfo.getWebcharNo())) {
                cardCustomerDetailVo.setWebcharNo(customerBaseInfo.getWebcharNo());
            }
            if (StringUtils.isEmpty(customerBaseInfo.getCustomerName())) {
                cardCustomerDetailVo.setCustomerName(customerBaseInfo.getCustomerName());
            }
            if (StringUtils.isEmpty(customerBaseInfo.getAlipayUserId())) {
                cardCustomerDetailVo.setAlipayUserId(customerBaseInfo.getAlipayUserId());
            }
            if (StringUtils.isEmpty(customerBaseInfo.getAlipayNickName())) {
                cardCustomerDetailVo.setAlipayNickName(customerBaseInfo.getAlipayNickName());
            }
        }
        return cardCustomerDetailVo;
    }

    @Override
    public void sendCard(List<SendAndReadCardParam> param, String sessionId) {
        SendAndReadCardParam sendAndReadCardParam = param.get(0);
        Afn26Object afn26Object = new Afn26Object(UUID.randomUUID().toString(),
                "999999999",
                "0042475858FFFFEB",
                sendAndReadCardParam.getCommNo(),
                sessionId,
                "",
                sendAndReadCardParam.getPointCode(),
                sendAndReadCardParam.getCardGuid(),
                sendAndReadCardParam.getState(),
                0);
        //下发指令
        int result  = rabbitmqProducer.sendAfn26(afn26Object);
        if (result != 1) {
            throw new BusinessException("下发IC卡mq队列发送失败");
        }
    }

    @Override
    public void readCard(List<SendAndReadCardParam> param, String sessionId) {
        //调用中间件查询ic下发信息
        SendAndReadCardParam sendAndReadCardParam = param.get(0);
        //调用中间件下发档案指令
        Afn26Object afn26Object = new Afn26Object(UUID.randomUUID().toString(),
                "999999999",
                "0042475858FFFFEB",
                sendAndReadCardParam.getCommNo(),
                sessionId,
                "",
                sendAndReadCardParam.getPointCode(),
                sendAndReadCardParam.getCardGuid(),
                sendAndReadCardParam.getState(),
                1);
        int result = rabbitmqProducer.sendAfn26(afn26Object);
        if (result != 1) {
            throw new BusinessException("读取IC卡中间件指令失败");
        }
    }

    @Override
    public void clearCard(List<String> commNos, String sessionId) {
        //调用中间件查询ic下发信息
        String commNo = commNos.get(0);
        //调用中间件下发档案指令
        Afn27Object afn27Object = new Afn27Object(UUID.randomUUID().toString(),
                "999999999",
                "0042475858FFFFEB",
                commNo,
                sessionId,
                "");
        int result = rabbitmqProducer.sendAfn27(afn27Object);
        if (result != 1) {
            throw new BusinessException("清除IC卡中间件指令失败");
        }
    }
}
