package cn.com.cdboost.charge.webapi.dto.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录成功，返回登录用户相关信息
 */
@Setter
@Getter
@ApiModel(value = "LoginSuccessInfoVo", description = "返回登录用户相关信息")
public class LoginSuccessInfoVo {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String userMobile;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱")
    private String userMail;

    /**
     * 用户所属角色id
     */
    @ApiModelProperty(value = "用户所属角色id")
    private Integer roleId;

}
