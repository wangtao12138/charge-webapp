package cn.com.cdboost.charge.webapi.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Afn22Response {
    /**
     *通信编号
     */
    private String commNo;
    /**
     *端口号
     */
    private String port;
    /**
     *过流阈值（单位A）范围：2-5
     */
    private Float overCurrent;
    /**
     *过压阈值（单位V）范围：250-280
     */
    private Float overVoltage;
    /**
     *欠压阈值（单位V）范围：120-220
     */
    private Float underVoltage;
    /**
     *涓流阈值（单位A）范围：0.1-0.5
     */
    private Float underCurrent;
    /**
     * 判断进去涓流状态时延 单位：秒
     */
    private Integer underCurrentDelay;
    /**
     * 执行结果 1成功 0失败 -1设备不在线
     */
    private Integer status;


}