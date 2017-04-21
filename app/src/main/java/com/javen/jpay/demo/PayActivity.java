package com.javen.jpay.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mayihuijia.com.R;

public class PayActivity extends Activity implements OnClickListener {
	Button closeButton;
	Button comfirmButton;
	TextView content_get_time;
	TextView content_get_pay_money;
	RelativeLayout wxLayout;
	RelativeLayout zfbLayout;
	RadioButton wxradBtn;
	RadioButton zfbradBtn;
	public static final String PAYPARAMS = "PAYPARAMS";
	private int payType;
	private String waresName;
	private int price;
	private String notifyUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pay);
		WindowManager.LayoutParams params = getWindow().getAttributes();// 得到属性
		params.gravity = Gravity.CENTER;// 显示在中间

		DisplayMetrics metrics = getResources().getDisplayMetrics();

		int dispayWidth = metrics.widthPixels;
		int dispayHeight = metrics.heightPixels;

		params.width = (int) (dispayWidth * 0.8);
		params.height = (int) (dispayHeight * 0.8);
		getWindow().setAttributes(params);

		initView();
		hideRadioButton();
		zfbradBtn.setChecked(true);

		Bundle extra = getIntent().getBundleExtra(PAYPARAMS);
		if (extra != null) {
			payType = extra.getInt("payType", 1);
			waresName = extra.getString("waresName", "充值");
			price = extra.getInt("price", 89);
			notifyUrl = extra.getString("notifyUrl", "");
			content_get_time.setText(waresName);
			content_get_pay_money.setText(price+"");
		}
	}

	private void initView() {
		closeButton = (Button) findViewById(R.id.close_btn);
		comfirmButton = (Button) findViewById(R.id.comfirm_btn);
		content_get_time = (TextView) findViewById(R.id.content_get_time);
		content_get_pay_money = (TextView) findViewById(R.id.content_get_pay_money);
		wxLayout = (RelativeLayout) findViewById(R.id.sel_wx_layout);
		zfbLayout = (RelativeLayout) findViewById(R.id.sel_zfb_layout);

		wxradBtn = (RadioButton) findViewById(R.id.wx_radBtn);
		zfbradBtn = (RadioButton) findViewById(R.id.zfb_radBtn);

		closeButton.setOnClickListener(this);
		comfirmButton.setOnClickListener(this);

		wxLayout.setOnClickListener(this);
		zfbLayout.setOnClickListener(this);

		wxradBtn.setOnClickListener(this);
		zfbradBtn.setOnClickListener(this);
	}

	public static void start(Context context,int payType, String waresName, int price,String notifyUrl) {
		Intent intent = new Intent(context, PayActivity.class);

		Bundle bundle = new Bundle();
		bundle.putInt("payType", payType);
		bundle.putString("waresName", waresName);
		bundle.putInt("price", price);
		bundle.putString("notifyUrl", notifyUrl);

		intent.putExtra(PAYPARAMS, bundle);
		context.startActivity(intent);
	}

	private void hideRadioButton() {
		wxradBtn.setChecked(false);
		zfbradBtn.setChecked(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.close_btn:
			finish();
			break;
		case R.id.sel_wx_layout:
		case R.id.wx_radBtn:
			hideRadioButton();
			wxradBtn.setChecked(true);
			break;
		case R.id.sel_zfb_layout:
		case R.id.zfb_radBtn:
			hideRadioButton();
			zfbradBtn.setChecked(true);
			break;

		case R.id.comfirm_btn:
			int choice = getPayMode();
			System.out.println(choice+" payType="+payType);
			if (choice == 403) {
				
			}else {
				
			}
			comfirmButton.setText("确认支付(支付中...)");
			break;

		default:
			break;
		}
	}

	private int getPayMode() {
		if (wxradBtn.isChecked()) {
			return 403;
		}
		if (zfbradBtn.isChecked()) {
			return 401;
		}
		return 403;
	}
}
