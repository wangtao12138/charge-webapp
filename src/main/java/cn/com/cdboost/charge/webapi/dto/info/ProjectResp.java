package cn.com.cdboost.charge.webapi.dto.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "ProjectResp", description = "站点对象")
public class ProjectResp implements Serializable {

    private static final long serialVersionUID = -2072998417418756541L;
    @ApiModelProperty(value = "站点标识")
    private String projectGuid;

    @ApiModelProperty(value = "商户标识")
    private String merchantGuid;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    @ApiModelProperty(value = "物业标识")
    private String propertyGuid;

    @ApiModelProperty(value = "所属组织")
    private String orgName;

    @ApiModelProperty(value = "站点名称")
    private String projectName;

    @ApiModelProperty(value = "站点位置")
    private String projectAddr;

    @ApiModelProperty(value = "小区名称")
    private String communityName;

    @ApiModelProperty(value = "物业公司名称")
    private String companyName;

    @ApiModelProperty(value = "物业公司名称")
    private String createTime;

    @ApiModelProperty(value = "总表表号")
    private String meterNo;
    /*@ApiModelProperty(value = "经度坐标")
    private BigDecimal lng;

    @ApiModelProperty(value = "纬度坐标")
    private BigDecimal lat;

    @ApiModelProperty(value = "1 GPS 2百度坐标")
    private Integer locationType;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;*/
}
