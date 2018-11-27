package cn.com.cdboost.charge.webapi.dto.param;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ElectricCountQueryParam {
    private String mark;
    private String nodeId;
    /**
     * 1-组织，2-站点
     */
    private Integer nodeType;
    private List<Long> orgNoList;
    private String beginTime;
    private String endTime;
}
