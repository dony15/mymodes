package com.dony15.shop.controller;

import com.dony15.shop.pojo.Order;
import com.dony15.shop.pojo.OrderItem;
import com.dony15.shop.service.OrderService;
import com.dony15.shop.utils.PaywxUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author DonY15
 * @description
 * @create 2018\6\22 0022
 */
@Controller
public class payServlet {

    @Autowired
    private OrderService orderService;



    /**
     * 用于获取用户输入的商品名称,然后生成订单号,发送到腾讯服务器,获取短地址,生成二维码,跳转显示页面
     *
     * @param orderId 前台订单号
     * @param req
     * @param resp
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/pay")
    public void goPayPage(@RequestParam("orderId") String orderId, HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        /**
         * 获取body和prices
         */
        Order order = orderService.selectOneToOrderBy(orderId);
        List<OrderItem> items = order.getItems();
        StringBuffer bodysb = new StringBuffer();
        for (int i = 0; i < items.size(); i++) {
            bodysb.append(items.get(i).getName());
        }
        String body = bodysb.toString();
        String prices = order.getPrices().toString();

        PaywxUtils.goPayPage(orderId, body, prices, req, resp);
    }



    /**
     * 生成付款二维码的工具
     *
     * @param req
     * @param resp
     */
    @RequestMapping("/image")
    public void getImage(HttpServletRequest req, HttpServletResponse resp) {
        PaywxUtils.getImage(req, resp);
    }



    /**
     * 请求微信服务器支付状态
     *
     * @param orderid
     * @param req
     * @param resp
     */
    @RequestMapping("/getorderstate")
    public void getOrderstate(@RequestParam("orderid") String orderid, HttpServletRequest req, HttpServletResponse resp) {
        String orderstate = PaywxUtils.getOrderstate(orderid, req, resp);
        if ("支付成功".equals(orderstate)) {
            orderService.updateToOrderstate(orderid);
            return;
        }
    }



    /**
     * 微信服务器返回值,解析微信返回的支付结果(核心逻辑)
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping("/result")
    public void result(HttpServletRequest request, HttpServletResponse response) {

        try {
            PaywxUtils.result(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}