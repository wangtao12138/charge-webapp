package cn.com.cdboost.charge.webapi.dto.info;

import java.math.BigDecimal;

public class ChargingICPayResp {

    /**
     * 实际支付金额
     */
    private BigDecimal payMoney;

    /**
     * 账户余额充值金额（pay_category=4时跟pay_money可能不一致）
     */
    private BigDecimal accountChargeMoney;

    /**
     * 支付成功后，账户余额需要扣减金额
     */
    private BigDecimal accountDeductMoney;


    /**
     * 充值流水号
     */
    private String serialNum;

    private String payFlag;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 支付状态 0-待支付 1-支付成功
     */
    private Integer payState;
    /**
     * 客户唯一标识
     */
    private String customerGuid;

    /**
     * 充值后剩余金额
     */
    private BigDecimal ICRemainAmount;

    /**
     * 支付类别（充电时购买，月卡页面购买，活动页面购买）
     */
    private Integer type;

    /**
     * 充值方式 1-微信 2-支付宝 3-现金 4-余额
     */
    private Integer payWay;
    //微信昵称
    private String customerName;
    //支付宝昵称
    private String alipayNickName;
    //操作人员
    private String payUser;

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getAccountChargeMoney() {
        return accountChargeMoney;
    }

    public void setAccountChargeMoney(BigDecimal accountChargeMoney) {
        this.accountChargeMoney = accountChargeMoney;
    }

    public BigDecimal getAccountDeductMoney() {
        return accountDeductMoney;
    }

    public void setAccountDeductMoney(BigDecimal accountDeductMoney) {
        this.accountDeductMoney = accountDeductMoney;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getCustomerGuid() {
        return customerGuid;
    }

    public void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
    }

    public BigDecimal getICRemainAmount() {
        return ICRemainAmount;
    }

    public void setICRemainAmount(BigDecimal ICRemainAmount) {
        this.ICRemainAmount = ICRemainAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAlipayNickName() {
        return alipayNickName;
    }

    public void setAlipayNickName(String alipayNickName) {
        this.alipayNickName = alipayNickName;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }
}
