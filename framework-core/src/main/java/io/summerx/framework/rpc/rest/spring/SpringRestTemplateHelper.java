package io.summerx.framework.rpc.rest.spring;

import com.alibaba.dubbo.common.json.JSONObject;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xiayg on 7/14/2016.
 */
public class SpringRestTemplateHelper {

    private RestTemplate restTemplate;

    public void post(String url) {
        restTemplate.setInterceptors(null);

        JSONObject o;

    }
}
