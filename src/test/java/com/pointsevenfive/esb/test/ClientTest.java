package com.pointsevenfive.esb.test;

import com.pointsevenfive.esb.run.Client;
import com.pointsevenfive.esb.util.ResourceHelper;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ClientTest {

    private String requestQueue = "DEMO.IN";
    private String replyQueue = "DEMO.OUT";
    private String testMessage = ResourceHelper.getResource("msgs/demo.xml");

    // Dependency on Queue Manager
    @Test
    public void runSendAndReceiveTest() throws JMSException, IOException {
        Client client = new Client("test");
        Message message = client.sendAndReceive(requestQueue, replyQueue, testMessage);
        assertThat(message.getBody(TextMessage.class), is(testMessage));
    }
}
