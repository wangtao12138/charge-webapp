package cn.com.cdboost.charge.webapi.dto.param;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 设置充电桩阈值
 */
public class ChargeThresholdParam {
    /**
     *通信编号
     */
    @NotBlank(message = "commNo不能为空")
    @Valid
    private String commNo;
    /**
     *端口号
     */
    @NotBlank(message = "port不能为空")
    private String port;
    /**
     *过流阈值（单位A）范围：2-5
     */
    @NotNull(message = "port不能为null")
    private Float overCurrent;
    /**
     *过压阈值（单位V）范围：250-280
     */
    @NotNull(message = "port不能为null")
    private Float overVoltage;
    /**
     *欠压阈值（单位V）范围：120-220
     */
    @NotNull(message = "port不能为null")
    private Float underVoltage;
    /**
     *涓流阈值（单位A）范围：0.1-0.5
     */
    @NotNull(message = "port不能为null")
    private Float underCurrent;
    /**
     * 判断进去涓流状态时延 单位：秒
     */
    @NotNull(message = "port不能为null")
    private Integer underCurrentDelay;

    public String getCommNo() {
        return commNo;
    }

    public void setCommNo(String commNo) {
        this.commNo = commNo;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Float getOverCurrent() {
        return overCurrent;
    }

    public void setOverCurrent(Float overCurrent) {
        this.overCurrent = overCurrent;
    }

    public Float getOverVoltage() {
        return overVoltage;
    }

    public void setOverVoltage(Float overVoltage) {
        this.overVoltage = overVoltage;
    }

    public Float getUnderVoltage() {
        return underVoltage;
    }

    public void setUnderVoltage(Float underVoltage) {
        this.underVoltage = underVoltage;
    }

    public Float getUnderCurrent() {
        return underCurrent;
    }

    public void setUnderCurrent(Float underCurrent) {
        this.underCurrent = underCurrent;
    }

    public Integer getUnderCurrentDelay() {
        return underCurrentDelay;
    }

    public void setUnderCurrentDelay(Integer underCurrentDelay) {
        this.underCurrentDelay = underCurrentDelay;
    }
}
