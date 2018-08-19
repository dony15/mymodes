package com.dony15.utils;

/**
 * @author DonY15
 * @description Freemarker的工具类,模板路径设置固定
 * @create 2018\8\19 0019
 */

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Locale;
import java.util.Map;

public class FreemarkerUtil {

    /**
     *通过指定的名字获取相应的模板
     * @param request 固定
     * @param fileName 模板名,不含路径
     * @return
     */
    public Template getTemplate(HttpServletRequest request, String fileName) {
        try {
            Configuration cfg = new Configuration();
            cfg.setOutputEncoding("UTF-8");
            cfg.setDefaultEncoding("UTF-8");// 编码设置1
            cfg.setEncoding(Locale.CHINA, "UTF-8");

            //设定读取ftl模板文件的目录
            cfg.setClassForTemplateLoading(this.getClass(), "/templates/freemarkers_template_ftl");     //读取src目录下
//            cfg.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/templates/freemarkers_template_ftl"); //读取webroot目录下
            //在模板文件目录中找到名称为name的文件,并加载为模板
            Template template = cfg.getTemplate(fileName);
            template.setEncoding("UTF-8");// 编码设置2
            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过指定的文件目录和文件名生成相应的文件
     * @param template Utils中获得的模板对象
     * @param fileDir 通过模板生成Html的路径
     * @param fileName html文件名
     * @param root 模板参数,map的方式传入
     * @return
     */
    public  Boolean printToFile(Template template,String fileDir,String fileName,Map<String,Object> root) throws Exception {
        boolean done = false;
        Writer writer = null;
        try {
            //判断多级目录是否存在，不存在则一级级创建
            String[] paths = fileDir.split("\\\\");//注意：此处“\\”是错误的，必须要“\\\\”才能分割字符串
            String dir = paths[0];
            for (int i = 1; i < paths.length; i++) {
                dir = dir + File.separator + paths[i];
                File file=new File(dir.toString());
                if (!file.exists()) {
                    file.mkdir();
                }
            }
            //创建输出流
            File file = new File(fileDir +File.separator+ fileName);

            //设置生成的文件编码为UTF-8
            //服务器不支持UTF-8格式HTML时候使用ANSI格式HTML文件，即系统默认编码
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));  // 编码设置3
            //writer = new FileWriter(fileDir +File.separator+ fileName);
            //输出模板和数据模型都对应的文件
            template.process(root, writer);
            done = true;
        }  finally {
            if(writer!=null){
                writer.close();
            }
        }
        return done;
    }

    /**
     * 通过传入的请求和新闻信息重新生成html文件
     * @param news  //新闻实体类
     * @param request   //请求
     * @param user  //用户
     * @return
     */
//    public  boolean genenateHtml(News news,HttpServletRequest request,User user) {
//        String fileName = news.getFileName();
//        Map<String, Object> root = new HashMap<String, Object>();
//        root.put("id", news.getId());
//        root.put("title", news.getTitle());
//        root.put("create_date", news.getCreateDate());
//        root.put("creator", news.getCreator());
//        root.put("content", ClobUtil.ClobToString(news.getContent()));
//        root.put("fileName", news.getUploadFile()==null?"":news.getUploadFile());
//        FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
//        Template template = null;
//        template = freemarkerUtil.getTemplate(request, "news.ftl");//新闻发布系统的freemarker模板
//        String htmlDir = news.getDir();
//        boolean done = printToFile(template, htmlDir, fileName, root);
//        return done;
//    }
}