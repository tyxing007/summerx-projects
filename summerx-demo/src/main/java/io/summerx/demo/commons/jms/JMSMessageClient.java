package io.summerx.demo.commons.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Map;

/**
 * 一个简单的发送JMS消息的工具类
 */
public class JMSMessageClient {

    @Resource(name = "jmsTemplate1")
    JmsTemplate jmsTemplate1;

    @Resource(name = "jmsTemplate1")
    JmsTemplate jmsTemplate2;


    public void send(final JmsTemplate jmsTemplate, final String destinationName, final Object object) {
        send(jmsTemplate, destinationName, object, null);
    }

    public void send(final JmsTemplate jmsTemplate, final String destinationName, final Object object, final Map<String, Object> props) {
        jmsTemplate.send(destinationName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = jmsTemplate.getMessageConverter().toMessage(object, session);
                if (props != null) {
                    for (String name : props.keySet()) {
                        message.setObjectProperty(name, props.get(name));
                    }
                }
                return message;
            }
        });
    }

    public static class JMSMessageHelper {
        @Resource(name = "jmsTemplate1")
        public JmsTemplate jmsTemplate1;

        @Resource(name = "jmsTemplate2")
        public JmsTemplate jmsTemplate2;


    }

    public static void main(String[] args) {
        JMSMessageClient client = null;

    }
}
