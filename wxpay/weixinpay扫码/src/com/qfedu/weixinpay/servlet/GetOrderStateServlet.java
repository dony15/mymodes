package com.qfedu.weixinpay.servlet;

import com.qfedu.weixinpay.utils.PayCommonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/getorderstate")
public class GetOrderStateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderid=req.getParameter("orderid");
        System.out.println(orderid);
        try {
            //请求微信服务器，获取orderid对应的订单的支付信息。
            String str=PayCommonUtil.weixin_query(orderid);
            resp.setContentType("text/html;charset=utf-8");
           //String str= (String) req.getSession().getAttribute("state");
            System.out.println(str);
            resp.getWriter().print(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
