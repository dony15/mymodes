package com.qfedu.weixinpay.utils;

/**
 * Created by jackiechan on 2018/2/2/����10:33
 */
public class PayConfigUtil {

	//微信平台的app_id
	public static String APP_ID = "wx632c8f211f8122c6";
	//收款账号id
	public static String MCH_ID = "1497984412";
	//调用接口进行数据加密的钥匙
	public static String API_KEY = "sbNCm1JnevqI36LrEaxFwcaT0hkGxFnC";
	//支付的时候用的接口
	public static String UFDOOER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//查询支付状态的时候用的接口
	public static String QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	//配置这个参数，让微信支付服务器访问的通知支付结果的url（点对点）
	public static String NOTIFY_URL = "http://127.0.0.1:8080/weixinpay/result";
	//支付创建者的ip
	public static String CREATE_IP = "114.242.26.51";

	
}
