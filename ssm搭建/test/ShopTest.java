/**
 * @author DonY15
 * @description
 * @create 2018\6\19 0019
 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.*;

/**
 * @author DonY15
 * @description
 * @create 2018\6\13 0013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-dao.xml","classpath:applicationContext-service.xml","classpath:applicationContext-tx.xml"})
public class ShopTest {
    @Test
    public void test01(){
        System.out.println("搭建成功");
    }
}