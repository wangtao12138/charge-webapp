package cn.com.cdboost.charge.webapi.constants;

public enum Action {

	ADD("添加", 1), MODIFY("修改", 2), DELETE("删除", 3), READ("浏览", 4),
	CALL("实时召测", 5), DOWNLOAD("下载", 6), SEND_SMS("发送短信", 7), DOWN_CUSTOMER_RECORD("下发档案", 8),
	UPLOADFILE("上传文件", 9), FEE_PAY("充值缴费", 10), REMOVE_PAY("远程充值", 11), LOCAL_PAY("本地充值", 12),
	ON_OFF("通断", 13), OPEN_ICCARD("开户", 14), CHANGE_ICCARD("补卡", 15), JZQ_REBOOT("集中器重启", 16),
	JZQ_INIT("集中器初始化", 17), REPLENISH("掌机补抄", 18), CHANGE_METER("换表操作", 19),FRONT_LOG("前端操作日志", 20),
	LOGING("登陆",21),LOGINGOUT("登出",22);

	private Action(String desc , int actionId ){
        this.desc = desc ;
        this.actionId = actionId ;
    }
	
    private String desc ;
    
    private int actionId ;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
}
