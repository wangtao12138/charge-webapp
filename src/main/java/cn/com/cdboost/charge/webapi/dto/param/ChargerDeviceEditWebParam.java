package cn.com.cdboost.charge.webapi.dto.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


/**
 * @author wt
 * @desc
 * @create in  2018/11/5
 **/
@Getter
@Setter
public class ChargerDeviceEditWebParam extends  ChargerDeviceAddWebParam {
    @NotBlank(message = "chargingPlieGuid不能为空")
    private String chargingPlieGuid;

}
