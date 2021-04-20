package log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import talk.main.WebsocketChatServer;

import java.io.File;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

/**
 * @Program: netty_talk
 * @Description
 * @Author XieFeng
 * @Create 2020-12-18-16-47
 **/


public class LogTest {
    static final Log log=LogFactory.getLog(LogTest.class);

    public static void main(String[] args) {
        try{
            int i=1/0;
        }catch (Exception e)
        {
            log.fatal(e.getMessage(),e);
        }
    }
}
