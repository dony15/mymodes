package com.dony15.shop.utils;

import org.jdom.JDOMException;
import pay.wx.utils.PayCommonUtil;
import pay.wx.utils.PayConfigUtil;
import pay.wx.utils.XMLUtil;
import pay.wx.utils.ZxingUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author DonY15
 * @description 微信支付工具类
 * @create 2017\10\23 0023
 */
public class PaywxUtils {
    /**
     * @RequestMapping("/pay") 接口地址(可修改)
     *
     * @param orderId 订单号 如: 15297251026538
     * @param body  商品名集合
     * @param prices 商品总价
     * 获取商品铭记和和商品总价: 如
     *         Order order = orderService.selectOneToOrderBy(orderId);
     *         List<OrderItem> items = order.getItems();
     *         StringBuffer bodysb = new StringBuffer();
     *         for (int i = 0; i < items.size(); i++) {
     *             bodysb.append(items.get(i).getName());
     *         }
     *         String body=bodysb.toString();
     *         String prices = order.getPrices().toString();
     * @param req
     * @param resp
     */
    public static void goPayPage(String orderId,String body, String prices, HttpServletRequest req, HttpServletResponse resp){
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        prices = "1";//此处默认是1分,次数需要项目开发中实际根据用户购买的商品获取
        if (req.getMethod().equalsIgnoreCase("get")) {
            try {
                body = new String(body.getBytes("ISO8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            /**
             * 获取获取微信返回的二维码对应的短地址url,来自weixin_pay生成的字符串
             */
            String url = PayCommonUtil.weixin_pay(prices, body, orderId);

            /**
             * 跳转到支付页面,显示二维码
             */
            resp.sendRedirect("/shop/payment.jsp?order_id="+orderId+"&url="+URLEncoder.encode(url, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * @RequestMapping("/image") 接口地址(不建议修改)
     * 生成二维码图片
     * @param req
     * @param resp
     */
    public static void getImage(HttpServletRequest req, HttpServletResponse resp){
        /**
         *  url 代表了手机微信访问的地址
         *  url 二维码的文本信息
         *  -------------------------------------------------------------
         *  待定url的来源判断是否分离
         */

        String url=req.getParameter("url");
        BufferedImage image=ZxingUtil.createImage(url,300,300);
        if(image!=null){
            try {
                ImageIO.write(image,"JPEG",resp.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     *@RequestMapping("/getorderstate")  接口地址(不建议修改)
     *
     *获取微信服务器中订单的支付状态
     * @param req
     * @param resp
     * @return 字符串 支付成功
     */
    public static String getOrderstate(String orderid ,HttpServletRequest req, HttpServletResponse resp){
        String str="";
        try {
            //请求微信服务器，获取orderid对应的订单的支付信息。
            str=PayCommonUtil.weixin_query(orderid);
            resp.setContentType("text/html;charset=utf-8");
            System.out.println("支付状态为:"+str);
            resp.getWriter().print(str);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @RequestMapping("/result") 接口地址(不建议修改)
     *
     * 微信服务器返回值,解析微信返回的支付结果(核心逻辑)
     * @param request
     * @param response
     * @throws IOException
     */
    public static void result(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String writeContent = "默认支付失败";// 因为没有重定向,所以测试时无法知道支付结果,因此将支付结果写入文件,开发时访问文件查看,实际开发中删除
        String path = request.getSession().getServletContext().getRealPath("file");// 保存结果文件的位置
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = null;// 创建输出流,写入结果用,实际开发中删除由此到上面的内容
        try {
            fileOutputStream = new FileOutputStream(path + "/result.txt", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 读取参数
        InputStream inputStream = null;
        StringBuffer sb = new StringBuffer();
        try {
            inputStream = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        try {
            m = XMLUtil.doXMLParse(sb.toString());
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        // 账号信息
        String key = PayConfigUtil.API_KEY; // key

        System.err.println(packageParams);
        String out_trade_no = (String) packageParams.get("out_trade_no");// 订单号,实际开发中应该在下面的 IF 中,除非需要对每个订单的每次支付结果做记录
        // 判断签名是否正确
        if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功
                ////////// 执行自己的业务逻辑////////////////
                String mch_id = (String) packageParams.get("mch_id");
                String openid = (String) packageParams.get("openid");
                String is_subscribe = (String) packageParams.get("is_subscribe");
                // String out_trade_no = (String)packageParams.get("out_trade_no");

                String total_fee = (String) packageParams.get("total_fee");

                System.err.println("mch_id:" + mch_id);
                System.err.println("openid:" + openid);
                System.err.println("is_subscribe:" + is_subscribe);
                System.err.println("out_trade_no:" + out_trade_no);
                System.err.println("total_fee:" + total_fee);

                ////////// 执行自己的业务逻辑////////////////

                System.err.println("支付成功");
                writeContent = "订单:" + out_trade_no + "支付成功";// 拼接支付结果信息,写入文件,实际开发中删除
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.****
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            } else {
                writeContent = "订单" + out_trade_no + "支付失败,错误信息：" + packageParams.get("err_code");// 拼接支付结果信息,写入文件,实际开发中删除
                System.err.println("订单" + out_trade_no + "支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } else {
            writeContent = "订单" + out_trade_no + "通知签名验证失败,支付失败";// 拼接支付结果信息,写入文件,实际开发中删除
            System.err.println("通知签名验证失败");
        }
        fileOutputStream.write(writeContent.getBytes());// 将支付结果写入文件,实际开发中删除
        fileOutputStream.close();// 将支付结果写入文件,实际开发中删除
    }


}
