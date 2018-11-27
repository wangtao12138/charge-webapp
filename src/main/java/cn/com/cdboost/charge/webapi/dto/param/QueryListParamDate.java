package cn.com.cdboost.charge.webapi.dto.param;

import cn.com.cdboost.charge.base.vo.PageQueryParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author wt
 * @desc 分页查询参数实体类
 * @create 2017-10-31 09:30
 **/
@Getter
@Setter
public abstract class QueryListParamDate extends PageQueryParam {

    /**
     * 起始时间
     */
    @NotBlank(message = "startDate不能为null")
    private String startDate;

    /**
     *结束时间
     */
    @NotBlank(message = "endDate不能为null")
    private String endDate;


}
