package com.javen.jpay.entity;

public class Order { 
	private String appId;
	private String appKey;
	private String body;
	private String payType;//支付方式
	private String deviceInfo;//设备号
	private int totalFee;//单位分
	private String tradeNo;//商户订单号
	private String attach;//附加参数
	private String notifyUrl;//支付通知地址
	private String returnUrl;//支付页面回调地址
	
	public Order() {
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotiftyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	@Override
	public String toString() {
		return "Order [appId=" + appId + ", appKey=" + appKey + ", body="
				+ body + ", payType=" + payType + ", deviceInfo=" + deviceInfo
				+ ", totalFee=" + totalFee + ", tradeNo=" + tradeNo
				+ ", attach=" + attach + ", notifyUrl=" + notifyUrl
				+ ", returnUrl=" + returnUrl + "]";
	}
	
	
} 