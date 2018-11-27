package cn.com.cdboost.charge.webapi.controller;


import cn.com.cdboost.charge.base.aop.SystemControllerLog;
import cn.com.cdboost.charge.base.constant.GlobalConstant;
import cn.com.cdboost.charge.base.constant.RedisKeyConstant;
import cn.com.cdboost.charge.base.constant.UserInfoKey;
import cn.com.cdboost.charge.base.exception.ParamCheckException;
import cn.com.cdboost.charge.base.util.DESUtil;
import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.user.dto.info.UserListInfo;
import cn.com.cdboost.charge.user.dto.info.UserLoginInfo;
import cn.com.cdboost.charge.user.dto.param.UserAddParam;
import cn.com.cdboost.charge.user.dto.param.UserEditParam;
import cn.com.cdboost.charge.user.dto.param.UserQueryParam;
import cn.com.cdboost.charge.user.dubbo.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * @author linyang
 * @date 2017年5月15日
 * @Description:用户操作Controller层
 */
@RestController
@Api(value ="人员管理",tags = "人员管理相关接口")
@RequestMapping("/api/webapi/v1/user")
@Slf4j
public class UserController {

	@Reference(version = "1.0")
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 用户新增
	 * @param session
	 * @param
	 * @return
	 */
	@SystemControllerLog(description = "用户新增")
	@PostMapping(value = "/add")
	@ApiOperation(value="用户新增", notes="用户新增接口")
	public ApiResult add(HttpSession session, HttpServletRequest request, @Valid @RequestBody UserAddParam user) throws ParamCheckException {
		ApiResult result = new ApiResult();
		String md5Token = (String)request.getAttribute("md5Token");
		Map entries = redisTemplate.opsForHash().entries(md5Token);
		String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
		Integer currentUserId = Integer.valueOf(userId);

		user.setCreateUserId(currentUserId);
		if(user.getDataOrgList()==null||user.getDataOrgList().size()<1|| null==user.getDataOrgList().get(0)){
			result.error("管辖区域不能为空");
			return result;
		}
		userService.add(user);

		// 操作日志
		/*UserLog log = new UserLog();
		log.setUserId(Long.valueOf(currentUserId));
		log.setOptType(Action.ADD.getActionId());
		log.setOptObject("用户新增");
		log.setObjectKey("");
		log.setObjectKeyValue("");
		String content = "添加人员["+param.getUserName()+"]" ;
		log.setOptContent(content);
		log.setOptParam(JSON.toJSONString(param));
		userLogService.insertSelective(log);*/

		result.setMessage("添加成功");
		return result;
	}

	/**
	 * 用户修改
	 * @param session
	 * @param
	 * @return
	 */
	@SystemControllerLog(description = "用户修改")
	@PutMapping(value = "/edit")
	@ApiOperation(value="用户修改", notes="用户修改接口")
	public ApiResult edit(HttpServletRequest request,HttpSession session, @Valid @RequestBody UserEditParam user) throws ParamCheckException {
		ApiResult result = new ApiResult();
		String md5Token = (String)request.getAttribute("md5Token");
		Map entries = redisTemplate.opsForHash().entries(md5Token);
		String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;


		if(user.getDataOrgList()==null||user.getDataOrgList().size()<1|| null==user.getDataOrgList().get(0)){
			result.error("管辖区域不能为空");
			return result;
		}
		userService.edit(user);

		// 操作日志
	/*	UserLog log = new UserLog();
		log.setUserId(Long.valueOf(currentUser.getId()));
		log.setOptType(Action.MODIFY.getActionId());
		log.setOptObject("用户修改");
		log.setObjectKey("userId");
		log.setObjectKeyValue(String.valueOf( param.getUserId()));
		String content = "修改人员["+param.getUserName()+"]信息";
		log.setOptContent(content);
		log.setOptParam(JSON.toJSONString(param));
		userLogService.insertSelective(log);
		result.setMessage("修改成功");*/
		return result;
	}

	/**
	 * 批量删除用户
	 * @param session
	 * @param ids
	 * @return
	 */
	@SystemControllerLog(description = "批量删除用户")
	@DeleteMapping(value = "/delete")
	@ApiOperation(value="批量删除用户", notes="批量删除用户接口")
	public ApiResult delete(HttpServletRequest request,HttpSession session, @RequestBody Integer[] ids) throws ParamCheckException {
		ApiResult result = new ApiResult();
		String md5Token = (String)request.getAttribute("md5Token");
		Map entries = redisTemplate.opsForHash().entries(md5Token);
		String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;


		Set<Integer> idSet = Sets.newHashSet(ids);
		userService.batchDelete(Integer.valueOf(userId),new ArrayList<>(idSet));

		// 操作日志
		/*userLogService.create(currentUser.getId(), Action.DELETE.getActionId(),"用户删除操作","ids", "","删除人员"+list.toString(), JSON.toJSONString(ids));*/
		result.setMessage("删除成功");
		return result;
	}


	/**
	 * 用户列表查询
	 * @param session
	 * @param queryParam
	 * @return
	 */
	@SystemControllerLog(description = "用户列表查询")
	@GetMapping(value = "/queryList")
	@ApiOperation(value="用户列表查询", notes="用户列表查询接口")
	public ApiResult queryList(HttpSession session, UserQueryParam queryParam) {
		ApiResult result = new ApiResult<>();
		PageData<UserListInfo> userListInfoPageData = userService.queryList(queryParam);
		result.setData(userListInfoPageData);

		return result;
	}

	/**
	 * 登录用户，设置自己的新密码
	 * @param session
	 * @param newPassword
	 * @return
	 */
	@SystemControllerLog(description = "登录用户，设置自己的新密码")
	@PutMapping(value="updatePassword")
	@ApiOperation(value="登录用户，设置自己的新密码", notes="登录用户，设置自己的新密码接口")
	public ApiResult updatePassword(HttpServletRequest request,HttpSession session, @RequestParam String oldPassword, @RequestParam String newPassword) throws ParamCheckException {
		ApiResult result = new ApiResult("修改成功");
		if (StringUtils.isEmpty(oldPassword)) {
			result.error("参数oldPassword不能是空字符串");
			return result;
		}

		if (StringUtils.isEmpty(newPassword)) {
			result.error("参数newPassword不能是空字符串");
			return result;
		}

		String md5Token = (String)request.getAttribute("md5Token");
		Map entries = redisTemplate.opsForHash().entries(md5Token);
		String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;


		userService.modifyPassword(Integer.valueOf(userId), oldPassword,newPassword);


		// 操作日志
		/*userLogService.create(currentUser.getId(), Action.MODIFY.getActionId(),"用户修改密码","list", "","用户["+currentUser.getUserName()+"]修改密码","用户修改密码 userID:" + currentUser.getId());*/
		return result;
	}

	// 修改密码时，检验密码是否正确
	@SystemControllerLog(description = "修改密码时，检验密码是否正确")
	@GetMapping(value="/validate")
	@ApiOperation(value="修改密码时，检验密码是否正确", notes="修改密码时，检验密码是否正确")
	public ApiResult validate(HttpServletRequest request,HttpSession session, @RequestParam String password) {
		ApiResult result = new ApiResult();
		if (StringUtils.isEmpty(password)) {
			result.error("参数password不能为空");
			return result;
		}

		String md5Token = (String)request.getAttribute("md5Token");
		Map entries = redisTemplate.opsForHash().entries(md5Token);
		String userId = String.valueOf(entries.get(UserInfoKey.USER_GUID)) ;
		UserLoginInfo currentUser = (UserLoginInfo)redisTemplate.opsForValue().get(RedisKeyConstant.USER_LOGIN + userId);
		// 解密
		String pwd = null;
		try {
			pwd = DESUtil.decrypt(password.getBytes(), GlobalConstant.SECRET_KEY.getBytes());
		} catch (Exception e) {
			log.error("密码解密异常",e);
		}


		return result;
	}
	/*@SystemControllerLog(description = "统计查询模块, 日志查询")
	@GetMapping("/queryCustomerLog")
	@ApiOperation(value="统计查询模块, 日志查询", notes="统计查询模块, 日志查询")
	public String queryCustomerLog(
			  CustomerLogParam customerLogParam
	) {
		PageResult< List<UserLog>> result=new PageResult<>();
		PageInfo<UserLog> userLogs = userLogService.queryUserLog(customerLogParam);
		result.setData(userLogs.getList());
		result.setTotal(userLogs.getTotal());
		return  JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:ss");
	}*/
}
