package cn.com.cdboost.charge.webapi.dto.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 方案营收分析返回参数
 */
@Setter
@Getter
public class SchemePofitableResp {
    private String schemeGuid;
    private Integer payCategory;
    private Integer useNum;
    private BigDecimal usePower;
    private BigDecimal consumptionMoney;
    private BigDecimal profitable;
    private BigDecimal deductMoney;
    private BigDecimal money;
    private Integer chargingTime;
}
