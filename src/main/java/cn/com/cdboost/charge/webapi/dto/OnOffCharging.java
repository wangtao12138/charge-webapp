package cn.com.cdboost.charge.webapi.dto;

/**
 * 停止充电或停用websocket返回对象
 */
public class OnOffCharging {
    private Integer runState;
    private Integer online;
    private Integer status;
    private String deviceNo;
    private String port;
    private String chargingPlieGuid;

    public Integer getRunState() {
        return runState;
    }

    public void setRunState(Integer runState) {
        this.runState = runState;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getChargingPlieGuid() {
        return chargingPlieGuid;
    }

    public void setChargingPlieGuid(String chargingPlieGuid) {
        this.chargingPlieGuid = chargingPlieGuid;
    }
}
