package cn.com.cdboost.charge.webapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Setter
@Getter
@ApiModel(value = "DeviceParam", description = "充电设备添加")
public class Device implements Serializable {

    /**
     * 商户标识
     */
    @ApiModelProperty(value = "商户标识")
    private String merchantGuid;

    /**
     * 项目标识
     */
    @ApiModelProperty(value = "项目标识")
    private String projectGuid;
    @ApiModelProperty(value = "充电桩唯一标识")
    private String chargingPlieGuid;

    /**
     * 充电桩名称
     */
    @ApiModelProperty(value = "充电桩名称")
    private String deviceName;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    /**
     * 充电桩设备型号
     */
    @ApiModelProperty(value = "充电桩设备型号")
    private String deviceMode;

    /**
     * 通信编号
     */
    @ApiModelProperty(value = "通信编号")
    private String commNo;

    /**
     * 充电桩端口（0-9、a-f 一共16个端口号）
     */
    @ApiModelProperty(value = "充电桩端口（0-9、a-f 一共16个端口号）")
    private String port;

    /**
     * 0 -离线 1-在线
     */
    @ApiModelProperty(value = "0 -离线 1-在线")
    private Integer online;

    /**
     * 通信方式 32-loraWAN
     */
    @ApiModelProperty(value = "通信方式 32-loraWAN ")
    private Integer comMethod;

    /**
     * 充电桩状态 0 空闲 1-充电 2-停用  -1故障
     */
    @ApiModelProperty(value = "充电桩状态 0 空闲 1-充电 2-停用  -1故障 ")
    private Integer runState;

    /**
     * 电流越限阀值
     */
    @ApiModelProperty(value = "电流越限阀值")
    private BigDecimal currentLimit;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String district;

    /**
     * 经度坐标
     */
    @ApiModelProperty(value = "经度坐标")
    private BigDecimal lng;

    /**
     * 纬度坐标
     */
    @ApiModelProperty(value = "纬度坐标")
    private BigDecimal lat;

    /**
     * 1 GPS 2百度坐标
     */
    @ApiModelProperty(value = "1 GPS 2百度坐标")
    private Integer locationType;

    /**
     * 安装地址
     */
    @ApiModelProperty(value = "安装地址")
    private String installAddr;

    /**
     * 安装时间
     */
    @ApiModelProperty(value = "安装时间")
    private Date installDate;

    /**
     * 设备描述
     */
    @ApiModelProperty(value = "设备描述")
    private String remark;

    /**
     * 总表表号
     */
    @ApiModelProperty(value = "总表表号")
    private String meterNo;

    /**
     * 总表地址cno
     */
    @ApiModelProperty(value = "总表地址cno")
    private String meterCno;

    /**
     * 变压器号
     */
    @ApiModelProperty(value = "变压器号")
    private String transformerNo;

    /**
     * 设备状态下发标识 0-未下发 1-已下发
     */
    @ApiModelProperty(value = "设备状态下发标识 0-未下发 1-已下发")
    private Integer sendFlag;

    /**
     * 创建人员ID
     */
    @ApiModelProperty(value = "创建人员ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新人员ID
     */
    @ApiModelProperty(value = "更新人员ID")
    private Long updateUserId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 是否删除 0 正常 1删除
     */
    @ApiModelProperty(value = "是否删除 0 正常 1删除")
    private Integer isDel;

    private Integer result;
}