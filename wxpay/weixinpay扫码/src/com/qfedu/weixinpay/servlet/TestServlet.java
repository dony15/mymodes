package com.qfedu.weixinpay.servlet;

import com.qfedu.weixinpay.utils.PayCommonUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/test")
/**
 * Created by jackiechan on 2018/2/2/上午11:15
 用于获取用户输入的商品名称,然后生成订单号,发送到腾讯服务器,获取短地址,生成二维码,跳转显示页面
 */
public class TestServlet extends HttpServlet {
    Random random = new Random();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String price = "1";//此处默认是1分,次数需要项目开发中实际根据用户购买的商品获取
        String body = req.getParameter("body");//商品描述,获取用户前台输入的想要购买的商品,此处需要参考项目实际开发中获取
        if (req.getMethod().equalsIgnoreCase("get")) {
            body = new String(body.getBytes("ISO8859-1"), "UTF-8");
        }
        String orderId = random.nextInt(100000000) + "";//此处随机生成伪订单,实际开发中请参考项目需求生成
        try {
            String jsonObject = PayCommonUtil.weixin_pay(price, body, orderId);//获取微信返回的二维码对应的短地址
            System.out.println(JSONObject.fromObject(jsonObject).getString("code_url"));
            String url=JSONObject.fromObject(jsonObject).getString("code_url");
            req.getSession().setAttribute("state",JSONObject.fromObject(jsonObject).getString("return_code"));
            //更新数据库，订单表中，这个订单号对应的订单的支付状态。

            //req.getSession().setAttribute("url", url);//将图片放到 session 中
            resp.sendRedirect("/weixinpay/payment.html?order_id="+orderId+"&url="+URLEncoder.encode(url, "utf-8"));//跳转到支付页面,显示二维码
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

}