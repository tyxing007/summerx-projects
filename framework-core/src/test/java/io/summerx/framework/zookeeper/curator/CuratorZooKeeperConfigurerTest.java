package io.summerx.framework.zookeeper.curator;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xiayg on 6/30/2016.
 */
public class CuratorZooKeeperConfigurerTest extends ZooKeeperTest {

    @Override
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void runTest() throws Exception {

        setup();

        CuratorZooKeeperConfigurer configurer = new CuratorZooKeeperConfigurer();
        configurer.setClient(client);
        configurer.setZkpaths(Arrays.asList("/conf/comm", "/conf/app1"));
        configurer.setLocation(new ClassPathResource("jdbc.properties"));

        configurer.afterPropertiesSet();

        Properties props = configurer.mergeProperties();

        Assert.assertEquals("url@zk:/conf/app1", props.get("jdbc.url"));
        Assert.assertEquals("username@zk:/conf/comm", props.get("jdbc.username"));
        Assert.assertEquals("password@jdbc.properties", props.get("jdbc.password"));
    }

    @Test
    public void runSpringTest() throws Exception {

        setup();

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/spring-context.xml");

        ZKValue zkValue = applicationContext.getBean("zkValue", ZKValue.class);

        Assert.assertEquals("url@zk:/conf/app1", zkValue.getUrl());
        Assert.assertEquals("username@zk:/conf/comm", zkValue.getUsername());
        Assert.assertEquals("password@jdbc.properties", zkValue.getPassword());
    }
}
