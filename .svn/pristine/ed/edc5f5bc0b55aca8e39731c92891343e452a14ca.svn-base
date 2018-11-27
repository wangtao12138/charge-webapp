package cn.com.cdboost.charge.webapi.service;

import cn.com.cdboost.charge.customer.vo.param.ChargerDeviceQueryVo;
import cn.com.cdboost.charge.merchant.vo.info.ChargerSchemeQueryVo;
import cn.com.cdboost.charge.statistic.vo.dto.ProjectUseCountDto;
import cn.com.cdboost.charge.webapi.dto.info.DeviceUseCountResp;
import cn.com.cdboost.charge.webapi.dto.info.PowerAndFeeCountResp;
import cn.com.cdboost.charge.webapi.dto.param.ElectricCountQueryParam; /**
 * 电量电费相关接口
 */
public interface ElectricFeeWrapper {
    /**
     * 电量电费柱状图
     * @param param
     * @return
     */
    PowerAndFeeCountResp queryPowerAndFeeCount(ElectricCountQueryParam param);

    /**
     * 统计站点使用情况
     * @param param
     * @return
     */
    ProjectUseCountDto projectUseCountList(ChargerSchemeQueryVo param);

    /**
     * 统计设备使用情况
     * @param queryVo
     * @return
     */
    DeviceUseCountResp deviceUseCountList(ChargerDeviceQueryVo queryVo);
}
