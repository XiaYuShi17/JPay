package com.javen.jpay;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.javen.jpay.alipay.Alipay;
import com.javen.jpay.asynctask.JPayAsyncTask;
import com.javen.jpay.entity.Order;
import com.javen.jpay.weixin.WeiXinPay;

import org.json.JSONException;
import org.json.JSONObject;


public class JPay {
	private final static String TAG = JPay.class.getName();

	// 未安装微信或微信版本过低
	public static final int WEIXIN_VERSION_LOW = 0x001;
	// 支付参数异常
	public static final int PAY_PARAMETERS_ERROE = 0x002;
	// 支付失败
	public static final int PAY_ERROR = 0x003;
	// 支付网络连接出错
	public static final int PAY_NETWORK_ERROR = 0x004;
	// 支付结果解析错误
	public static final int RESULT_ERROR = 0x005;
	// 正在处理中
	public static final int PAY_DEALING = 0x006;
	// 其它支付错误
	public static final int PAY_OTHER_ERROR = 0x007;

	private static JPay mJPay;
	private Activity mContext;

	private JPay(Activity context) {
		mContext = context;
	}

	public static JPay getIntance(Activity context) {
		if (mJPay == null) {
			synchronized (JPay.class) {
				if (mJPay == null) {
					mJPay = new JPay(context);
				}
			}
		}
		return mJPay;
	}

	public interface JPayListener {
		// 支付成功
		void onPaySuccess();

		// 支付失败
		void onPayError(int error_code, String message);

		// 支付取消
		void onPayCancel();
	}

	public enum PayMode {
		WXPAY, ALIPAY,
	}

	public void toPay(PayMode payMode, String payParameters,
			JPayListener listener) {
		if (payMode.name().equalsIgnoreCase(PayMode.WXPAY.name())) {
			toWxPay(payParameters, listener);
		} else if (payMode.name().equalsIgnoreCase(PayMode.ALIPAY.name())) {
			toAliPay(payParameters, listener);
		}
	}

	/**
	 * 微信支付-通过订单信息异步获取预付订单并唤起支付
	 * @param order
	 * @param listener
	 */
	public void toWxPay(Order order, JPayListener listener) {
		isListener(listener);
		if (order == null) {
			listener.onPayError(PAY_PARAMETERS_ERROE, "订单信息不能为空");
			return;
		}

		if (TextUtils.isEmpty(order.getAppId())
				|| TextUtils.isEmpty(order.getAppKey())
				|| TextUtils.isEmpty(order.getDeviceInfo())
				|| TextUtils.isEmpty(order.getTradeNo())
				|| TextUtils.isEmpty(order.getPayType())
				|| TextUtils.isEmpty(order.getBody())
				|| order.getTotalFee() <= 0) {
			listener.onPayError(PAY_PARAMETERS_ERROE, "请检查参数");
			return;
		}

		new JPayAsyncTask(mContext, listener).execute(order);
	}

	/**
	 * 微信支付-通过预付订单唤起支付
	 * @param payParameters
	 * @param listener
	 */
	public void toWxPay(String payParameters, JPayListener listener) {
		try {
			isListener(listener);
			if (TextUtils.isEmpty(payParameters)) {
				listener.onPayError(PAY_OTHER_ERROR, "获取预付订单异常");
				return;
			}

			JSONObject payObject = null;
			JSONObject dataObject = null;

			payObject = new JSONObject(payParameters);
			int code = payObject.getInt("code");
			String message = payObject.getString("message");
			if (code != 0) {
				if (listener != null) {
					listener.onPayError(code, message);
				}
				return;
			}
			
			dataObject = payObject.getJSONObject("data");

			if (TextUtils.isEmpty(dataObject.optString("appId"))
					|| TextUtils.isEmpty(dataObject.optString("partnerId"))
					|| TextUtils.isEmpty(dataObject.optString("prepayId"))
					|| TextUtils.isEmpty(dataObject.optString("nonceStr"))
					|| TextUtils.isEmpty(dataObject.optString("timeStamp"))
					|| TextUtils.isEmpty(dataObject.optString("sign"))) {
				listener.onPayError(PAY_PARAMETERS_ERROE, "参数异常");
				return;
			}
			
			WeiXinPay.getInstance(mContext).startWXPay(
					dataObject.optString("appId"),
					dataObject.optString("partnerId"),
					dataObject.optString("prepayId"),
					dataObject.optString("nonceStr"),
					dataObject.optString("timeStamp"),
					dataObject.optString("sign"), listener);

		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			listener.onPayError(PAY_OTHER_ERROR, "异常"+e.getMessage());
		}
	}
	/**
	 * 微信支付
	 * @param appId
	 * @param partnerId
	 * @param prepayId
	 * @param nonceStr
	 * @param timeStamp
	 * @param sign
	 * @param listener
	 */
	public void toWxPay(String appId, String partnerId, String prepayId,
			String nonceStr, String timeStamp, String sign,
			JPayListener listener) {
		isListener(listener);
		if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(partnerId)
				|| TextUtils.isEmpty(prepayId) || TextUtils.isEmpty(nonceStr)
				|| TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(sign)) {
			listener.onPayError(PAY_PARAMETERS_ERROE, "参数异常");
			return;
		}
		WeiXinPay.getInstance(mContext).startWXPay(appId, partnerId, prepayId,
				nonceStr, timeStamp, sign, listener);
	}

	/**
	 * 支付宝支付-通过订单信息异步获取预付订单并唤起支付
	 * @param order
	 * @param listener
	 */
	public void toAliPay(Order order, JPayListener listener) {
		isListener(listener);

		if (order == null) {
			listener.onPayError(PAY_PARAMETERS_ERROE, "订单信息不能为空");
			return;
		}

		if (TextUtils.isEmpty(order.getAppId())
				|| TextUtils.isEmpty(order.getAppKey())
				|| TextUtils.isEmpty(order.getDeviceInfo())
				|| TextUtils.isEmpty(order.getTradeNo())
				|| TextUtils.isEmpty(order.getPayType())
				|| TextUtils.isEmpty(order.getBody())
				|| order.getTotalFee() <= 0) {
			listener.onPayError(PAY_PARAMETERS_ERROE, "请检查参数");
			return;
		}

		new JPayAsyncTask(mContext, listener).execute(order);
	}

	/**
	 * 支付宝支付-通过预付订单唤起支付
	 * @param payParameters
	 * @param listener
	 */
	public void toAliPay(String payParameters, JPayListener listener) {
		try {
			isListener(listener);
			if (TextUtils.isEmpty(payParameters)) {
				listener.onPayError(PAY_OTHER_ERROR, "获取预付订单异常");
				return;
			}
			
			System.out.println("payParameters>"+payParameters);
			
			JSONObject payObject = new JSONObject(payParameters);
			int code = payObject.getInt("code");
			String message = payObject.getString("message");
			if (code != 0) {
				listener.onPayError(code, message);
				return;
			}
			String data = payObject.getJSONObject("data").getString("payInfo");
			Alipay.getInstance(mContext).startAliPay(data, listener);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			listener.onPayError(PAY_OTHER_ERROR, "异常"+e.getMessage());
		}
	}

	private void isListener(JPayListener listener) {
		if (listener == null) {
			Toast.makeText(mContext, "listener is null", Toast.LENGTH_LONG).show();
			return;
		}
	}

}
