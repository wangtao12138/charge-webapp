package cn.com.cdboost.charge.webapi.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 充电ic卡添加
 */
@Setter
@Getter
@ApiModel(value = "ChargerICCardAddParam",description = "添加ic卡")
public class ChargerICCardAddParam {

    @NotBlank(message = "cardId不能为空")
    @Valid
    @ApiModelProperty(value = "ic卡号")
    private String cardId;

    @NotNull(message = "initAmount不能为null")
    @ApiModelProperty(value = "初始金额")
    private BigDecimal initAmount;

    @ApiModelProperty(value = "姓名")
    private String customerName;

    @NotBlank(message = "customerContact不能为空")
    @ApiModelProperty(value = "联系电话")
    private String customerContact;

    @NotBlank(message = "projectGuid不能为空")
    @ApiModelProperty(value = "站点标识")
    private String projectGuid;

    @ApiModelProperty(value = "备注")
    private String remark;

    private String cardGuid;
}
