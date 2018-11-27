package cn.com.cdboost.charge.webapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class RoleRight implements Serializable {
    private static final long serialVersionUID = -1789434617322862556L;
    /**
     * 主键
     */

    private Integer id;

    /**
     * 角色ID
     */

    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 动作ID（0-所有）
     */
    private Long actionId;


}