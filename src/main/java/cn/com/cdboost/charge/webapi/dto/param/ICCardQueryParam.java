package cn.com.cdboost.charge.webapi.dto.param;

import cn.com.cdboost.charge.base.vo.PageQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 充电桩IC卡查询vo
 */
@Setter
@Getter
@ApiModel(value = "ICCardQueryParam",description = "ic卡相关查询对象")
public class ICCardQueryParam extends PageQueryParam {
    @ApiModelProperty(value = "卡号" )
    private String cardId;

    @ApiModelProperty(value = "ic卡状态 0初始、1 -启用、2-停用")
    private Integer cardState;

    private String startDate;

    private String endDate;

    private String projectGuid;

    private String deviceNo;

    private String commNo;

    private Integer sendFlag;

    private String nodeId;

    private Integer nodeType;

    private List<Long> orgNoList;
}
