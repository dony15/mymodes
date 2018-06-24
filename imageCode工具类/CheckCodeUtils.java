package com.dony15.shop.utils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author DonY15
 * @description 验证码工具类
 *
 * @method public void  static imagecheckcode(HttpServletRequest req, HttpServletResponse resp)
 * @返回值 (src路径字符串getWriter() 响应到前端)
 *
 * @method public void  static getCodeWord(HttpServletRequest req, HttpServletResponse resp)
 * @返回值1  word (长度为4的验证码字符串添加到Session域对象中)
 * @返回值2  (长度为4的验证码字符串getWriter()响应到前端)
 * @create 2017\11\19 0019
 */
public class CheckCodeUtils {
    /**
     *  1.保存所有的成语
     */
    private static List<String> words=new ArrayList<String>();


    /**
     * 2.图片验证码程序
     */
    public static void imagecheckcode(HttpServletRequest req,
                               HttpServletResponse resp) throws IOException {

        /**
         * 3 初始化成语资源放入List集合中保存
         */
        initWords(req);

        /**
         *  4.设置http响应报文的mime类型-->响应图片时,写image/jpeg
         *    不能写之前的UTF-8,否则会被当成文本解析
         */
        resp.setContentType("image/jpeg");

        /**
         * 5.随机对象,返回的index是任意一个成语
         */
        Random random = new Random();
        int index = random.nextInt(words.size());
        String word = words.get(index);
        req.getSession().setAttribute("word",word);
        /**
         * 6.定义验证码的尺寸
         */
        int width=120;
        int height=30;

        /**
         * 核心
         * 7.用java生成一个图片
         *    参数:
         *        1)宽度
         *        2)高度
         *        3)颜色类型,在BufferedImage中有
         *   此时bufferedImage就是一个图片对像,它有绘制对象
         */
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        /**
         * 8.获取图片的绘制对象 Graphics
         */
        Graphics graphics = bufferedImage.getGraphics();

        /**
         * ------------------------------------------------------------------------------------------------------
         *
         *      根据需求调色即可
         * 9. 验证码分为三部分(颜色不可以相近,设置随机颜色)
         *      1)底色    200-249
         *      2)验证码   20-100
         *      3)噪点    160-200
         *
         * ------------------------------------------------------------------------------------------------------
         */
        //1)绘制底色(一般比较浅,fillRect进行填充,drawRect是描边绘制)
        Color dise = getRandomColor(200, 249);
        graphics.setColor(dise);
        graphics.fillRect(0,0,width,height);
        //设置白色 **
        graphics.setColor(Color.WHITE);
        //描边绘制
        graphics.drawRect(0,0,width,height);
        //2)绘制验证码
        Graphics2D graphics2D=(Graphics2D) graphics;
        //设置即将绘制出来的文字的样式
        graphics2D.setFont(new Font("宋体",Font.BOLD,18));
        int x=10;
        for (int i = 0; i < word.length(); i++) {
            //设置颜色比较深
            graphics2D.setColor(getRandomColor(20,100));
            //设置一个旋转角度
            int angle=random.nextInt(60)-30;
            //转化成弧度
            double theta = Math.PI / 180 * angle;
            //获得某个单个字
            char c=word.charAt(i);
            //设定字的 角度,x轴,y轴,
            graphics2D.rotate(theta,x,20);
            //字体右移,画上去
            graphics2D.drawString(c+"",x,20);
            //画完再转回来
            graphics2D.rotate(-theta,x,20);
            x+=30;
        }
        //3)噪点
        graphics.setColor(getRandomColor(160,200));
        //绘制线条(两个点确定一条线)
        int x1;
        int x2;
        int y1;
        int y2;
        /**
         * -------------------------------------------------------------------------------------------
         * 根据需求调整数量和线条长度
         *
         * i<30 30条噪点线
         * 每条线长度不会超过12*12=sqrt(288)
         * -------------------------------------------------------------------------------------------
         */
        for (int i = 0; i < 30; i++) {


            //每一条线都需要有限制(第一个点在全图上随机)
            x1= random.nextInt(width);
            y1= random.nextInt(height);

            //每条线长度不会超过12*12=sqrt(288)
            x2=random.nextInt(12)+x1;
            y2=random.nextInt(12)+y1;
            graphics.drawLine(x1,y1,x2,y2);
        }
        //释放资源
        graphics.dispose();

        /**
         * ------------------------------------------------------------------------------------------------------
         *  10.输出到网页信息 ajax接收
         *  参数
         *      1)图片
         *      2)图片类型
         *      3)响应输出流
         * ------------------------------------------------------------------------------------------------------
         */
        ImageIO.write(bufferedImage,"jpg",resp.getOutputStream());
    }

    /**
     * 将图片验证程序的读取工作提取出来
     * @param req
     */
    private static void initWords(HttpServletRequest req){

        if(words.size()==0){

            /**
             * ----------------------------------------------------------------------------------------------------
             *
             * 3-1.导入成语txt文件并放在WEB-INF中,获取路径
             * 根据需求更改路径------/WEB-INF/new_words.txt----------
             *
             * ----------------------------------------------------------------------------------------------------
             */
            String path=req.getSession().getServletContext().getRealPath("/WEB-INF/new_words.txt");

            //3-2.读取资源文件
            BufferedReader reader=null;
            try {
                //注意指定编码格式
                reader=new BufferedReader(new InputStreamReader(new FileInputStream(path),"utf-8"));
                //3-3.用String一行一行读取
                String line;
                for (;(line=reader.readLine())!=null;){
                    words.add(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 设置验证码随机颜色工具,传入起点和终点即可
     */
    private static Color getRandomColor(int fc,int ec){
        Random random = new Random();
        //每个颜色区间都不同
        int r = fc + random.nextInt(ec - fc);
        int g = fc + random.nextInt(ec - fc);
        int b = fc + random.nextInt(ec - fc);
        //返回设定的颜色
        return new Color(r,g,b);
    }

    public static void getCodeWord(HttpServletRequest req,HttpServletResponse resp){
        //返回session
        String word = (String) req.getSession().getAttribute("word");
        try {
            System.out.println(word);
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write(word);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
