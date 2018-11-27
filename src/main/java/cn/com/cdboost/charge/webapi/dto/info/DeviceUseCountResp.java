package cn.com.cdboost.charge.webapi.dto.info;



import cn.com.cdboost.charge.statistic.vo.dto.DeviceCountStatic;
import cn.com.cdboost.charge.statistic.vo.dto.DeviceUseCountListDto;

import java.util.List;

/**
 * 电量电费统计---设备使用统计
 */
public class DeviceUseCountResp {
    private List<DeviceUseCountListDto> list;
    private DeviceCountStatic statistics;

    public List<DeviceUseCountListDto> getList() {
        return list;
    }

    public void setList(List<DeviceUseCountListDto> list) {
        this.list = list;
    }

    public DeviceCountStatic getStatistics() {
        return statistics;
    }

    public void setStatistics(DeviceCountStatic statistics) {
        this.statistics = statistics;
    }
}
