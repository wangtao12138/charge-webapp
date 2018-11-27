package cn.com.cdboost.charge.webapi.dto.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * ic卡下发列表
 */
@Setter
@Getter
@ApiModel(value = "CardListResp",description = "ic卡下发列表")
public class CardListResp {
    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "设备通信编号")
    private String commNo;

    @ApiModelProperty(value = "ic卡编号")
    private String cardId;

    @ApiModelProperty(value = "IC卡状态 -1移除  0-欠费  1-正常")
    private Integer state;

    @ApiModelProperty
    private Integer pointCode;

    @ApiModelProperty(value = "ic卡下发状态 0-未下发 1-已下发")
    private Integer sendFlag;

    @ApiModelProperty(value = "下发时间")
    private String updateTime;

    @ApiModelProperty(value = "内部guid")
    private String cardGuid;
}
