package cn.com.cdboost.charge.webapi.dto.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ICCardDetailResp extends ICCardResp{
    /**
     * 账户余额
     */
    private BigDecimal cusRemainAmount;

    /**
     * 微信昵称
     */
    private String customerName;

    /**
     * 微信openId
     */
    private String webcharNo;

    /**
     * 阿里支付用户id
     */
    private String alipayUserId;

    /**
     * 阿里支付用户昵称
     */
    private String alipayNickName;
}
