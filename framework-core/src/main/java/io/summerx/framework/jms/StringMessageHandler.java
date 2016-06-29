package io.summerx.framework.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringMessageHandler implements MessageHandler<String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handleMessage(String messageText) {
        if (logger.isDebugEnabled()) {
            logger.debug("===============================================================");
            logger.debug(messageText);
            logger.debug("===============================================================");
        }
    }
}
