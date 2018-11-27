package cn.com.cdboost.charge.webapi.dto.info;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 电量电费统计详情
 */
public class PowerAndFeeCountResp implements Serializable{
    /**
     * 汇总充电桩电量
     */
    private BigDecimal electricQuantityCount = BigDecimal.ZERO;
    /**
     * 汇总充电桩电费
     */
    private BigDecimal electricityFeesCount = BigDecimal.ZERO;
    /**
     * 汇总充电桩盈利
     */
    private BigDecimal profitCount = BigDecimal.ZERO;

    //总扣除费用
    private BigDecimal deductMoneyCount = BigDecimal.ZERO;
    //总表使用电量
    private BigDecimal meterUsePower = BigDecimal.ZERO;
    //总表电费
    private BigDecimal meterUseMoney = BigDecimal.ZERO;
    //实际盈利
    private BigDecimal meterProfit = BigDecimal.ZERO;
    private List<String> xData;
    private List<BigDecimal> yFeesData;
    private List<BigDecimal> yQuantityData;


    public BigDecimal getElectricQuantityCount() {
        return electricQuantityCount;
    }

    public void setElectricQuantityCount(BigDecimal electricQuantityCount) {
        this.electricQuantityCount = electricQuantityCount;
    }

    public BigDecimal getElectricityFeesCount() {
        return electricityFeesCount;
    }

    public void setElectricityFeesCount(BigDecimal electricityFeesCount) {
        this.electricityFeesCount = electricityFeesCount;
    }

    public BigDecimal getProfitCount() {
        return profitCount;
    }

    public void setProfitCount(BigDecimal profitCount) {
        this.profitCount = profitCount;
    }

    public List<String> getxData() {
        return xData;
    }

    public void setxData(List<String> xData) {
        this.xData = xData;
    }

    public List<BigDecimal> getyFeesData() {
        return yFeesData;
    }

    public void setyFeesData(List<BigDecimal> yFeesData) {
        this.yFeesData = yFeesData;
    }

    public List<BigDecimal> getyQuantityData() {
        return yQuantityData;
    }

    public void setyQuantityData(List<BigDecimal> yQuantityData) {
        this.yQuantityData = yQuantityData;
    }

    public BigDecimal getDeductMoneyCount() {
        return deductMoneyCount;
    }

    public void setDeductMoneyCount(BigDecimal deductMoneyCount) {
        this.deductMoneyCount = deductMoneyCount;
    }

    public BigDecimal getMeterUsePower() {
        return meterUsePower;
    }

    public void setMeterUsePower(BigDecimal meterUsePower) {
        this.meterUsePower = meterUsePower;
    }

    public BigDecimal getMeterUseMoney() {
        return meterUseMoney;
    }

    public void setMeterUseMoney(BigDecimal meterUseMoney) {
        this.meterUseMoney = meterUseMoney;
    }

    public BigDecimal getMeterProfit() {
        return meterProfit;
    }

    public void setMeterProfit(BigDecimal meterProfit) {
        this.meterProfit = meterProfit;
    }
}
