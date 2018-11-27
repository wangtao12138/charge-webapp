package cn.com.cdboost.charge.webapi.dto.param;

import cn.com.cdboost.charge.base.vo.PageQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 充电设备列表查询
 */
@Setter
@Getter
@ApiModel(value = "ChargerDeviceQueryParam", description = "充电设备列表查询")
public class ChargerDeviceQueryWebParam extends PageQueryParam implements Serializable {
    @ApiModelProperty(value = "设备编号")
    private String deviceNo;
    //设备程序版本
    @ApiModelProperty(value = "版本号")
    private Integer ver;
    @ApiModelProperty(value = "查询结束时间")
    private String endDate;
    @ApiModelProperty(value = "查询开始时间")
    private String startDate;
    @ApiModelProperty(value = "充电桩唯一编号")
    private String chargingPlieGuid;
    @ApiModelProperty(value = "充电记录唯一编号")
    private String chargingGuid;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "项目唯一编号")
    private String projectGuid;
    /**
     * 运行状态
     */
    @ApiModelProperty(value = "运行状态")
    private String runState;
    /**
     * 0 -离线 1-在线
     */
    @ApiModelProperty(value = "在线状态 0-离线 1-在线")
    private Integer online;
    /**
     * 计费方式
     */
    @ApiModelProperty(value = "计费方式")
    private String payCategory;
    @ApiModelProperty(value = "客户唯一编号")
    private String customerGuid;
    @ApiModelProperty(value = "端口号")
    private String port;
    @ApiModelProperty(value = "通信编号")
    private String commNo;
    @ApiModelProperty(value = "节点编号")
    private String nodeId;
    @ApiModelProperty(value = "节点类型 1-组织 2-项目")
    private Integer nodeType;
    @ApiModelProperty(value = "组织编号")
    private List<Long> orgNoList;
    @ApiModelProperty(value = "分页总数")
    private Long total;
}
