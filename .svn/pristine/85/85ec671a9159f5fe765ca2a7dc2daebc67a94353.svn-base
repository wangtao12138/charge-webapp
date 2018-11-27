package cn.com.cdboost.charge.webapi.controller;


import cn.com.cdboost.charge.base.aop.SystemControllerLog;
import cn.com.cdboost.charge.base.constant.UserInfoKey;
import cn.com.cdboost.charge.base.exception.ParamCheckException;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.user.dto.info.RoleInfo;
import cn.com.cdboost.charge.user.dto.info.RoleRightInfo;
import cn.com.cdboost.charge.user.dto.param.MenuActionParam;
import cn.com.cdboost.charge.user.dto.param.RoleAddParam;
import cn.com.cdboost.charge.user.dto.param.RoleEditParam;
import cn.com.cdboost.charge.user.dubbo.RoleService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
* @Description:角色操作Controller层
*@author linyang
*@date 2017年5月15日
*/
@RestController
@Api(value ="角色相关",tags = "角色相关接口")
@RequestMapping("/api/webapi/v1/role")
public class RoleController {

	@Reference(version = "1.0")
	private RoleService roleService;
	@Autowired
	private RedisTemplate redisTemplate;

	// 角色添加
	@SystemControllerLog(description = "角色添加")
	@PostMapping(value="/save")
	@ApiOperation(notes = "角色添加",value = "角色添加")
	public ApiResult save(HttpServletRequest request, HttpSession session, @Valid @RequestBody RoleAddParam param){
		ApiResult result = new ApiResult();
		/*LoginUser currentUser = (LoginUser)session.getAttribute(GlobalConstant.CURRENT_LOGIN_USER);
		userLogService.create(currentUser.getId(), Action.ADD.getActionId(),"角色控制","roleName",param.getRoleName(),"添加角色:["+param.getRoleName()+"]", JSON.toJSONString(param));*/
		String md5Token = (String)request.getAttribute("md5Token");
		Map entries = redisTemplate.opsForHash().entries(md5Token);
		String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
		RoleInfo role = new RoleInfo();
		role.setCreateUserId(Integer.valueOf(userId));
		role.setDescription(param.getDescription());
		role.setRoleName(param.getRoleName());

		List<MenuActionParam> menuActionList = param.getMenuActionList();
		if (CollectionUtils.isEmpty(menuActionList)) {
			result.error("该角色对应的菜单动作列表数据为空");
			return result;
		}
		List<RoleRightInfo> roleRightList = this.getRoleRightList(menuActionList);

		roleService.addRole(role,roleRightList);

		result.setMessage("新增成功");
		return result;
	}
	private List<RoleRightInfo> getRoleRightList(List<MenuActionParam> menuActionList) {
		List<RoleRightInfo> roleRights = Lists.newArrayList();
		for (MenuActionParam actionParam : menuActionList) {
			RoleRightInfo roleRight = new RoleRightInfo();
			roleRight.setMenuId(actionParam.getMenuId());
			roleRight.setActionId(actionParam.getActionId());
			roleRights.add(roleRight);
		}
		return roleRights;
	}

	// 角色删除
	@SystemControllerLog(description = "角色删除")
	@DeleteMapping(value="/delete/{roleId}")
	@ApiOperation(notes = "角色删除",value = "角色删除")
	public ApiResult delete(HttpSession session, @PathVariable Integer roleId) throws ParamCheckException {
		ApiResult result = new ApiResult();
		/*LoginUser currentUser = (LoginUser)session.getAttribute(GlobalConstant.CURRENT_LOGIN_USER);*/
		RoleRightInfo roleRightInfo = roleService.selectByRoleId(roleId);
		roleService.delete(roleId);
		result.setMessage("删除成功");
		/*userLogService.create(currentUser.getId(), Action.DELETE.getActionId(),"角色控制","roleId", String.valueOf(roleId),"删除角色:["+role.getRoleName()+"]" , String.valueOf(roleId));*/
		return result;
	}

	// 角色修改
	@SystemControllerLog(description = "角色修改")
	@PutMapping("/update")
	@ApiOperation(notes = "角色删除",value = "角色删除")
	public String update(HttpServletRequest request, HttpSession session, @Valid @RequestBody RoleEditParam param){
		ApiResult result = new ApiResult();
		String md5Token = (String)request.getAttribute("md5Token");
		Map entries = redisTemplate.opsForHash().entries(md5Token);
		String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
		/*userLogService.create(currentUser.getId(), Action.MODIFY.getActionId(),"角色控制","roleId", String.valueOf(param.getRoleId()),"修改角色["+param.getRoleName()+"]信息" , JSON.toJSONString(param));*/

		RoleInfo role = new RoleInfo();
		role.setCreateUserId(Integer.valueOf(userId));
		role.setDescription(param.getDescription());
		role.setRoleName(param.getRoleName());
		role.setId(param.getRoleId());
		List<RoleRightInfo> roleRightList = this.getRoleRightList(param.getMenuActionList());

		roleService.update(role,roleRightList);

		result.setMessage("修改成功");
		return JSON.toJSONString(result);
	}

	/**
	 * 查询所有角色
	 * @return
	 */
	@SystemControllerLog(description = "查询所有角色")
	@GetMapping("/queryAll")
	@ApiOperation(notes = "查询所有角色",value = "查询所有角色")
	public ApiResult queryAll(HttpServletRequest request,HttpSession session) {
        ApiResult<List<RoleInfo>> result = new ApiResult<>();
        String md5Token = (String)request.getAttribute("md5Token");
        Map entries = redisTemplate.opsForHash().entries(md5Token);
        String isSystem = String.valueOf(entries.get(UserInfoKey.IS_SYSTEM)) ;
		String roleId = String.valueOf(entries.get(UserInfoKey.USER_ROLEID)) ;
		List<RoleInfo> roles = roleService.listRole(Long.valueOf(roleId), Integer.valueOf(isSystem));
		result.setData(roles);

		return result;
	}

	/**
	 * 根据角色id，查询对应的菜单动作权限列表
	 * @param roleId
	 * @return*/

	@SystemControllerLog(description = "根据角色id，查询对应的菜单动作权限列表")
	@GetMapping("/queryRolePermissions")
	@ApiOperation(notes = "根据角色id，查询对应的菜单动作权限列表",value = "根据角色id，查询对应的菜单动作权限列表")
	public ApiResult queryRolePermissions(@RequestParam Integer roleId) {
		ApiResult result = new ApiResult<>();
		List<RoleRightInfo> rolePermissions = roleService.queryRolePermissionsByRoleId(roleId);
		result.setData(rolePermissions);

		return result;
	}

	
}
