package cn.com.cdboost.charge.webapi.base;

import cn.com.cdboost.charge.base.vo.ApiResult;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.webapi.vo.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * API result测试
 */
public class ApiResultTest {
    @Test
    public void apiResultTest() {
        ApiResult<User> result = new ApiResult<>();
        User user = new User();
        user.setName("fanlvyu");
        user.setAge(28);
        result.setData(user);

        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void apiResultTest2() {
        ApiResult<PageData<User>> result = new ApiResult<>();
        PageData<User> pageData = new PageData<>();
        pageData.setTotal(100L);
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setName("fanlvyu");
        user.setAge(28);
        list.add(user);

        User user2 = new User();
        user2.setName("fanlvyu");
        user2.setAge(28);
        list.add(user2);
        pageData.setList(list);

        result.setData(pageData);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void apiResultTest3() {
        ApiResult<PageData<User>> result = new ApiResult<>();
        PageData<User> pageData = new PageData<>();
        pageData.setTotal(0L);
        pageData.setList(null);

        result.setData(pageData);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void apiResultTest4() {
        ApiResult<User> result = new ApiResult<>();
        result.setData(null);
        System.out.println(JSON.toJSONString(result));
    }
}
