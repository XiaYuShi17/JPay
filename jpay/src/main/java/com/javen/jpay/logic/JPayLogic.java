package com.javen.jpay.logic;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.javen.jpay.Constants;
import com.javen.jpay.JPay;
import com.javen.jpay.entity.Order;
import com.javen.jpay.utils.HttpKit;
import com.javen.jpay.utils.MD5;

import java.util.HashMap;
import java.util.Map;


public class JPayLogic {
	private static  JPayLogic mJPayLogic = new JPayLogic();
	private JPayLogic() {
	} 
 
	public static JPayLogic getIntance(){
		return mJPayLogic;
	}
	
	public static String getUnifiedOrder(Order order, final JPay.JPayListener listener){
		try {
			String appId = order.getAppId();
			String appKey = order.getAppKey();
			String body = order.getBody();
			String payType = order.getPayType();
			String deviceInfo = order.getDeviceInfo();
			String totalFee = String.valueOf(order.getTotalFee());
			String tradeNo = order.getTradeNo();
			String attach = order.getAttach();
			String notifyUrl = order.getNotifyUrl();
			String returnUrl = order.getReturnUrl();
			
			
			Map<String, String> queryParas = new HashMap<String, String>();
			queryParas.put("appId", appId);
			
			queryParas.put("payType", payType);
			queryParas.put("tradeNo", tradeNo);
			queryParas.put("deviceInfo",deviceInfo);
			queryParas.put("totalFee", totalFee);
			if (!TextUtils.isEmpty(body)) {
				queryParas.put("body", body);
			}
			if (!TextUtils.isEmpty(notifyUrl)) {
				queryParas.put("notifyUrl", notifyUrl);
			}
			if (!TextUtils.isEmpty(returnUrl)) {
				queryParas.put("returnUrl", returnUrl);
			}
			if (!TextUtils.isEmpty(attach)) {
				queryParas.put("attach", attach);
			}
			StringBuffer sbf = new StringBuffer();
			sbf.append(appId).append(payType).append(appKey).append(deviceInfo)
			.append(totalFee).append(tradeNo);
			String sign = MD5.MD5sign(sbf.toString());
			queryParas.put("sign", sign);
			return HttpKit.post(Constants.PAY_URL, queryParas);
		} catch (final Exception e) {
			Log.e("JPayLogic", e.getMessage());
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					listener.onPayError(JPay.PAY_OTHER_ERROR, "异常"+e.getMessage());
				}
			});
		}
		return null;
	}
} 