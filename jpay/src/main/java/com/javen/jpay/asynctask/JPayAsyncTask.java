package com.javen.jpay.asynctask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.javen.jpay.JPay;
import com.javen.jpay.entity.Order;
import com.javen.jpay.logic.JPayLogic;

@SuppressLint("NewApi") 
public class JPayAsyncTask extends AsyncTask<Object, Integer, Object>{
	private Activity mContent;
	private JPay.JPayListener mListener;
	private Order mOrder;
	
	public JPayAsyncTask(Activity content,JPay.JPayListener listener) {
		mContent = content;
		mListener = listener;
	}

	@Override
	protected Object doInBackground(Object... params) {
		mOrder = (Order)params[0];
		return JPayLogic.getUnifiedOrder(mOrder,mListener);
	}

	@Override
	protected void onPostExecute(Object result) {
		try {
			if (result!=null) {
				String data= (String)result;
				Log.d("JPay", "payType>"+mOrder.getPayType()+" data>"+data);
				if (mOrder.getPayType().equals("wxpay_android")) {
					JPay.getIntance(mContent).toWxPay(data, mListener);
				}else if (mOrder.getPayType().equals("alipay_android")) {
					JPay.getIntance(mContent).toAliPay(data, mListener);
				}else {
					mListener.onPayError(JPay.PAY_OTHER_ERROR, "支付方式填写错误");
				}
			}
		} catch (Exception e) {
			Log.e("JPay", e.getMessage());
		}
		
		super.onPostExecute(result);
	}

	

}
