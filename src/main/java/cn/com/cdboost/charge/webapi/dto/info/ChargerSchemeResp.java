package cn.com.cdboost.charge.webapi.dto.info;

import cn.com.cdboost.charge.webapi.dto.param.ChargingSchemeDto;

import javax.validation.Valid;
import java.util.List;

/**
 * 方案返回
 */
public class ChargerSchemeResp {
    private String projectGuid;
    private Integer minPower;
    private Integer maxPower;
    //充电方案
    @Valid
    private List<ChargingSchemeDto> schemeList;

    public String getProjectGuid() {
        return projectGuid;
    }

    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }

    public Integer getMinPower() {
        return minPower;
    }

    public void setMinPower(Integer minPower) {
        this.minPower = minPower;
    }

    public Integer getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Integer maxPower) {
        this.maxPower = maxPower;
    }

    public List<ChargingSchemeDto> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(List<ChargingSchemeDto> schemeList) {
        this.schemeList = schemeList;
    }
}
