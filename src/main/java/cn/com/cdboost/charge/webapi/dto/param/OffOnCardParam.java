package cn.com.cdboost.charge.webapi.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 停用启用充电IC卡
 */
@ApiModel(value = "OffOnCardParam",description = "停用或启用ic卡参数对象")
public class OffOnCardParam {

    @ApiModelProperty(value = "ic卡卡号集合")
    private List<String> cardIds;

    @ApiModelProperty(value = "停用或启用标识（0-停用；1-启用）")
    private Integer onOrOff;

    public List<String> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<String> cardIds) {
        this.cardIds = cardIds;
    }

    public Integer getOnOrOff() {
        return onOrOff;
    }

    public void setOnOrOff(Integer onOrOff) {
        this.onOrOff = onOrOff;
    }

}
