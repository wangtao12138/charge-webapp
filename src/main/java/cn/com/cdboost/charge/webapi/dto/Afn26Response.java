package cn.com.cdboost.charge.webapi.dto;

public class Afn26Response {

    /**
     * 执行结果 1成功 0失败 -1设备不在线
     */
    private Integer status;

    /**
     * 档案是否存在 true存在
     */
    private boolean exist = false;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
