package cn.com.cdboost.charge.webapi.dto.param;


import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class ChargeHeartStepParam {

    @NotBlank(message = "commNo不能为空")
    @Valid
    private String commNo;

    @Range(min = 1, max = 600, message = "heartStep范围在0-600之间")
    private Integer heartStep;

    public String getCommNo() {
        return commNo;
    }

    public void setCommNo(String commNo) {
        this.commNo = commNo;
    }

    public Integer getHeartStep() {
        return heartStep;
    }

    public void setHeartStep(Integer heartStep) {
        this.heartStep = heartStep;
    }
}
