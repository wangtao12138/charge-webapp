package cn.com.cdboost.charge.webapi.dto.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


/**
 * @author wt
 * @desc
 * @create in  2018/8/16
 **/
@Getter
@Setter
public class WithdrawCashListParam extends QueryListParamDate{
    private Integer userId;
    @NotBlank(message = "customerGuid 不能为空")
    private String customerGuid;
    @NotBlank(message = "withdrawMethod 不能为空")
    private String withdrawMethod;
    @NotBlank(message = "status 不能为空")
    private String status;

}
