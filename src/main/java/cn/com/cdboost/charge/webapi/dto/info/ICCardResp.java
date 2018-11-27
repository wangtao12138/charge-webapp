package cn.com.cdboost.charge.webapi.dto.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 充电桩IC卡列表
 */
@Setter
@Getter
public class ICCardResp {
    private Integer cardState;
    private String cardId;
    private BigDecimal icRemainAmount = BigDecimal.ZERO;
    private BigDecimal initAmount;
    private BigDecimal remainAmount;
    private Integer useCnt;
    private Integer payCnt = 0;
    private String createTime;
    private String updateTime;
    private String remark;
    private String customerName;
    private String customerContact;
    private String customerType;
    private String customerGuid;
    private String webcharNo;
    private String alipayUserId;
    private Integer sendFlag;

    private String cardGuid;
    private String projectGuid;
    private String projectName;
}
