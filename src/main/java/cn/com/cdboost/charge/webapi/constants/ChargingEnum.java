package cn.com.cdboost.charge.webapi.constants;

public enum ChargingEnum {
    FREE_STATE(0,"空闲"),
    ON_STATE(1,"充电"),
    OFF_STATE(2,"停用"),
    ERROR_STATE(-1,"故障"),
    MONTH_PAYCATEGORY(2,"包月充值"),
    TEMP_PAYCATEGORY(1,"购买次数");

    /**
     * 枚举常量值
     */
    private Integer key;

    /**
     * 描述信息
     */
    private String message;

    ChargingEnum(Integer key, String message) {
        this.key = key;
        this.message = message;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
