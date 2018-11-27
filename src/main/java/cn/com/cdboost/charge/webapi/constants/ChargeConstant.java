package cn.com.cdboost.charge.webapi.constants;

/**
 * 充电相关常量
 */
public class ChargeConstant {

    /**
     * 下发状态
     */
    public enum SendFlag {
        SEND(1,"已下发"),NOT_SEND(0,"未下发");
        /**
         * 运行状态
         */
        private Integer status;

        /**
         * 描述
         */
        private String desc;

        SendFlag(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 设备在线状态
     */
    public enum DeviceOnlineStatus {
        OFFLINE(0,"离线"),ONLINE(1,"在线");
        /**
         * 运行状态
         */
        private Integer status;

        /**
         * 描述
         */
        private String desc;

        DeviceOnlineStatus(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 是否删除
     */
    public enum IsDel {
        NOTDEL(0,"正常"),DEL(1,"删除");
        /**
         * 是否删除
         */
        private Integer status;

        /**
         * 描述
         */
        private String desc;

        IsDel(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 充电桩设备运行状态
     */
    public enum DeviceRunState {
        IDEL(0,"空闲"),
        CHARGING(1,"充电 "),
        UN_USE(2,"停用"),
        FAULT(-1,"故障");

        /**
         * 运行状态
         */
        private Integer state;

        /**
         * 描述
         */
        private String desc;

        DeviceRunState(Integer state, String desc) {
            this.state = state;
            this.desc = desc;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        /**
         * 根据状态查询对应描述信息
         * @param state
         * @return
         */
        public static final String getDescByState(Integer state) {
            for (DeviceRunState deviceRunState : DeviceRunState.values()) {
                if (deviceRunState.getState().equals(state)) {
                    return deviceRunState.getDesc();
                }
            }
            return "";
        }
    }

    /**
     * 充电状态枚举
     */
    public enum ChargeState {
        CHARGING(0,"正在充电"),
        COMPLETED(1,"充电完成");

        /**
         * 充电状态
         */
        private Integer state;

        /**
         * 描述
         */
        private String desc;

        ChargeState(Integer state, String desc) {
            this.state = state;
            this.desc = desc;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 方案充值类别
     */
    public enum SchemePayCategory {
          TEMPORARY_RECHARGE(1,"临时充值"),
        MONTH_RECHARGE(2,"包月充值"),
        RECHARGE_FULL(3,"一次充满"),
        BALANCE_RECHARGE(4,"余额活动充值"),
        IC_RECHARGE(5,"IC卡充值");

        /**
         * 充值类别
         */
        private Integer type;

        /**
         * 描述
         */
        private String desc;

        SchemePayCategory(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static final String getDescByType(Integer type) {
            for (SchemePayCategory category : SchemePayCategory.values()) {
                if (category.getType().equals(type)) {
                    return category.getDesc();
                }
            }
            return "";
        }
    }

    /**
     * 方案充值类别
     */
    public enum SchemeIsEnable {
        ABLE(1,"启用"),
        UNABLE(0,"禁用");

        /**
         * 充值类别
         */
        private Integer type;

        /**
         * 描述
         */
        private String desc;

        SchemeIsEnable(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static final String getDescByType(Integer type) {
            for (SchemePayCategory category : SchemePayCategory.values()) {
                if (category.getType().equals(type)) {
                    return category.getDesc();
                }
            }
            return "";
        }
    }

    /**
     * 车辆类别
     */
    public enum CarType {
        CAR(2,"电动车"),
        ELECTRO_MOBILE(1,"电瓶车");

        /**
         * 充值类别
         */
        private Integer type;

        /**
         * 描述
         */
        private String desc;

        CarType(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static final String getDescByType(Integer type) {
            for (SchemePayCategory category : SchemePayCategory.values()) {
                if (category.getType().equals(type)) {
                    return category.getDesc();
                }
            }
            return "";
        }
    }

    /**
     * 通断电类型
     */
    public enum OnOffType {
        ON("on",1,"通电操作"),OFF("off",0,"断电操作");

        /**
         * 前置机返回类型
         */
        private String type;

        /**
         * websocket返回给前端的值
         */
        private Integer value;

        /**
         * 描述
         */
        private String desc;

        OnOffType(String type, Integer value, String desc) {
            this.type = type;
            this.value = value;
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 通断电操作状态
     */
    public enum OnOffOperateStatus {
        ON_SUCCESS(1,1,"通电成功"),
        ON_FAIL(1,0,"通电失败"),
        OFF_SUCCESS(0,1,"断电成功"),
        OFF_FAIL(0,0,"断电失败");

        /**
         * 1通电，0断电
         */
        private Integer type;

        /**
         * 通断电中间件返回state
         */
        private Integer status;

        /**
         * 描述
         */
        private String desc;

        OnOffOperateStatus(Integer type, Integer status, String desc) {
            this.type = type;
            this.status = status;
            this.desc = desc;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }
}
