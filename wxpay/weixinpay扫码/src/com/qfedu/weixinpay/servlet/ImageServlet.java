package com.qfedu.weixinpay.servlet;

import com.qfedu.weixinpay.utils.ZxingUtil;

import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

    //HttpServlet的子类的doXXX方法一定不能调用父类的同名方法，不然就会报错400或者405
    //这是模板方法设计模式。
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //url代表了手机微信访问的地址
        String url=req.getParameter("url");//二维码的文本信息
        BufferedImage image=ZxingUtil.createImage(url,300,300);
        if(image!=null){
            ImageIO.write(image,"JPEG",resp.getOutputStream());
        }
    }
}
