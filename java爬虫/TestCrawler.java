package com.crawler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author DonY15
 * @description Java基础爬虫(豆瓣部分示例)
 * ======================[工具类]===============================
 *
 * HttpClientUtil
 *
 * ======================[jar包]===============================
 *
 * <!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
 *         <dependency>
 *             <groupId>org.apache.httpcomponents</groupId>
 *             <artifactId>httpclient</artifactId>
 *             <version>4.5.6</version>
 *         </dependency>
 *
 *         <!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
 *         <dependency>
 *             <groupId>org.jsoup</groupId>
 *             <artifactId>jsoup</artifactId>
 *             <version>1.11.3</version>
 *         </dependency>
 *
 * =====================================================
 * @create 2018\7\25 0025
 */
public class TestCrawler {

    public static void main(String[] args) throws IOException {
        String url = "https://movie.douban.com/subject/26611804/?tag=%E7%83%AD%E9%97%A8&from=gaia_video";
        //通过httpClient获取网页响应,将返回的响应解析为纯文本
        String result = HttpClientUtil.doGet(url);
        //System.out.println(result);
        //Jsoup解析纯文本网页返回Document对象进行操作即可(jQuery选择器查询)
        Document doc = Jsoup.parse(result);

        //根据id查找
        Elements content = doc.select("#content h1");
        System.out.println(content.text());
        /**
         * =====================================================
         *
         * 豆瓣影评主图部分 mainpic
         *
         * =====================================================
         */


        Elements mainpicA = doc.select("#mainpic a");

        String moremainpic = mainpicA.attr("href");
        String moremainpictitle = mainpicA.attr("title");
        System.out.println(moremainpic);
        System.out.println(moremainpictitle);

        Elements mainpicI = doc.select("#mainpic img");
        String mainpicSrc = mainpicI.attr("src");
        System.out.println(mainpicSrc);

        /**
         * =====================================================
         *
         * 豆瓣影评概要部分 info
         *
         * =====================================================
         */

        /**
         * 导演
         */
        Elements directorA = doc.select("#info .pl").eq(0);
        System.out.println(directorA.text());

        Elements directorB = doc.select("#info a").eq(0);
        System.out.println(directorB.attr("href"));
        System.out.println(directorB.text());

        /**
         * 编剧
         */
        Elements writersA = doc.select("#info .pl").eq(1);
        System.out.println(writersA.text());

        Elements writersB = doc.select("#info a").eq(1);
        System.out.println(writersB.attr("href"));
        System.out.println(writersB.text());

        /**
         * 主演
         */
        Elements actorA = doc.select("#info .pl").eq(2);
        System.out.println(actorA.text());

        Elements actorB = doc.select("#info > .actor > .attrs a");
        for (int i = 0; i < actorB.size(); i++) {
            System.out.println(actorB.get(i).attr("href"));
            System.out.println(actorB.get(i).text());
        }

        /**
         * =====================================================
         *
         * 豆瓣影评简介部分 related-info
         *
         * =====================================================
         */

        Elements introductionA = doc.select(".related-info h2");
        System.out.println(introductionA.text());
        Elements introductionB = doc.select("#link-report span").eq(0);
        System.out.println(introductionB.html());

    }


}
