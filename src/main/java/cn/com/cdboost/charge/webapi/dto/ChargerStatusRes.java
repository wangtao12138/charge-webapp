package cn.com.cdboost.charge.webapi.dto;

public class ChargerStatusRes {
    /**
     * 执行结果 1成功 0失败 -1设备不在线
     */
    private Integer status;

    private String commNo;

    private String port;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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
}
