package cn.com.cdboost.charge.webapi.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ApiModel(value = "ProjectParam", description = "站点对象")
public class ProjectParam implements Serializable {

    private static final long serialVersionUID = -5220725627778631762L;
    @ApiModelProperty(value = "站点标识")
    private String projectGuid;

    @ApiModelProperty(value = "商户标识")
    private String merchantGuid;

    @ApiModelProperty(value = "物业标识")
    private String propertyGuid;

    @ApiModelProperty(value = "组织标识")
    @NotBlank(message = "orgNo不能为空")
    private String orgNo;

    @ApiModelProperty(value = "运营模式 1-代理商自运营")
    private Integer operateMode;

    @ApiModelProperty(value = "站点名称")
    @NotBlank(message = "projectName不能为空")
    private String projectName;

    @ApiModelProperty(value = "站点位置")
    @NotBlank(message = "projectAddr不能为空")
    private String projectAddr;

    @ApiModelProperty(value = "小区名称")
    private String communityName;

    @ApiModelProperty(value = "物业公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系人员")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "提成金额")
    private BigDecimal upPrice;

    @ApiModelProperty(value = "基础电价")
    private BigDecimal basePrice;

    @ApiModelProperty(value = "充电桩执行的电价")
    private BigDecimal price;

    @ApiModelProperty(value = "经度坐标")
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
    private String district;

    @ApiModelProperty(value = "站点备注信息")
    private String remark;

    @ApiModelProperty(value = "数据有效 1-有效  0-删除")
    private Integer isDel;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "方案类型：0-方案1；1-方案2")
    private Integer schemeType;

    private BigDecimal profitRatio;

    private String meterNo;
}
