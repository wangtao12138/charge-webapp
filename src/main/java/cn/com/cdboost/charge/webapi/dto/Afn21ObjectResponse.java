package cn.com.cdboost.charge.webapi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wt
 * @desc
 * @create in  2018/11/24
 **/
@Getter
@Setter
public class Afn21ObjectResponse extends AfnBaseResponse{

    private String appEUI;
    private String moteEUI;
    private String port;
    private String sessionId;
    private String openId;
    private String key ;
    private String overCurrent;
    private String overVoltage;
    private String underVoltage;
    private String fullCurrent;
    private String fullTime;
}
