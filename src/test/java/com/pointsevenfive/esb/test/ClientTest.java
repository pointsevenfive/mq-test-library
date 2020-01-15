package com.pointsevenfive.esb.test;

import com.pointsevenfive.esb.run.Client;
import com.pointsevenfive.esb.util.ResourceHelper;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ClientTest {

    private String requestQueue = "DEMO.IN";
    private String replyQueue = "DEMO.OUT";
    private String testMessage = "";

    // Requires QM to be available
    @Test
    public void runSendAndReceiveTest() throws JMSException {
        testMessage = ResourceHelper.getResource("msgs/demo.xml");
        Client client = new Client("test");
        Message message = client.sendAndReceive(requestQueue, replyQueue, testMessage);
        assertThat(message.getBody(TextMessage.class), is(testMessage));
    }
}
