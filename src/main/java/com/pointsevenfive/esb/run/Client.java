package com.pointsevenfive.esb.run;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.pointsevenfive.esb.config.Environment;

import javax.jms.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Client {

    private Environment environment;
    private Connection connection;
    private JmsConnectionFactory factory;
    private static boolean IS_TRANSACTED = false;
    private static int TIMEOUT_SECONDS = 10;


    public Client(String targetEnv) throws JMSException {
        this.environment = new Environment(targetEnv);
        this.factory = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER).createConnectionFactory();
        this.factory.setStringProperty(WMQConstants.WMQ_HOST_NAME, environment.getHost());
        this.factory.setIntProperty(WMQConstants.WMQ_PORT, environment.getPort());
        this.factory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, environment.getQueueManager());
        this.factory.setStringProperty(WMQConstants.WMQ_CHANNEL, environment.getChannel());
        this.factory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);

    }

    public Message sendAndReceive(String requestQueue, String replyQueue, String payload) throws JMSException {
        connection = factory.createConnection();
        Session session = connection.createSession(IS_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
        Destination replyDestination = session.createQueue(replyQueue);
        MessageConsumer consumer = session.createConsumer(replyDestination);
        connection.start();
        session.createProducer(session.createQueue(requestQueue))
                .send(generateTextMessage(session, payload, replyDestination));
        return consumer.receive(TIMEOUT_SECONDS);
    }

    private TextMessage generateTextMessage(Session session, String payload, Destination destination) throws JMSException {
        TextMessage txtMessage = session.createTextMessage();
        txtMessage.setText(payload);
        txtMessage.setJMSReplyTo(destination);
        txtMessage.setJMSCorrelationIDAsBytes(String.format("AMQ MSG %d", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).getBytes());
        return txtMessage;
    }

}
