package com.pointsevenfive.esb.run;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.pointsevenfive.esb.config.Environment;
import com.pointsevenfive.esb.util.MessageHelper;

import javax.jms.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Client {

    private Connection connection;
    private Session session;
    private JmsConnectionFactory factory;
    private boolean isTransacted = false;
    private int timeoutSeconds = 10;


    public Client(String env) throws JMSException, IOException {
        Environment environment = new Environment(env);
        this.factory = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER).createConnectionFactory();
        this.factory.setStringProperty(WMQConstants.WMQ_HOST_NAME, environment.getHost());
        this.factory.setIntProperty(WMQConstants.WMQ_PORT, environment.getPort());
        this.factory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, environment.getQueueManager());
        this.factory.setStringProperty(WMQConstants.WMQ_CHANNEL, environment.getChannel());
        this.factory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
    }

    public Message sendAndReceive(String requestQueue, String replyQueue, String payload) {
        Message receivedMessage = null;
        try {
            initiateSessionConnection();
            Destination replyTo = session.createQueue(replyQueue);
            MessageConsumer consumer = session.createConsumer(replyTo);
            connection.start();
            session.createProducer(session.createQueue(requestQueue))
                    .send(generateTextMessage(session, payload, replyTo));
            receivedMessage = consumer.receive(timeoutSeconds);
        } catch (JMSException | NoSuchAlgorithmException e) {
            System.err.println(String.format("Runtime connection error: %s", e.getMessage()));
        } finally {
            killSessionConnection();
        }
        return receivedMessage;
    }

    public Message listenAndRecieve(String queue, String correlationId) throws JMSException {
        Message receivedMessage = null;
        try {
            initiateSessionConnection();
            Destination listeningTo = session.createQueue(queue);
            MessageConsumer consumer = session.createConsumer(listeningTo, String.format("JMSCorrelationID = '%s'", correlationId));
            connection.start();
            receivedMessage = consumer.receive(timeoutSeconds);
        } catch (JMSException e) {
            System.err.println(String.format("Runtime connection error: %s", e.getMessage()));
        } finally {
            killSessionConnection();
        }
        return receivedMessage;
    }

    private void initiateSessionConnection() throws JMSException {
        connection = factory.createConnection();
        session = connection.createSession(isTransacted, Session.AUTO_ACKNOWLEDGE);
    }

    private void killSessionConnection() {
        try { session.close(); }  catch (JMSException e) { /* ignored */ }
        try { connection.close(); } catch (JMSException e) { /* ignored */ }
    }

    private TextMessage generateTextMessage(Session session, String payload, Destination destination) throws JMSException, NoSuchAlgorithmException {
        TextMessage txtMessage = session.createTextMessage();
        txtMessage.setText(payload);
        txtMessage.setJMSReplyTo(destination);
        txtMessage.setJMSCorrelationIDAsBytes(MessageHelper.genUUID().getBytes());
        return txtMessage;
    }

    public boolean isTransacted() {
        return isTransacted;
    }

    public void setTransacted(boolean transacted) {
        isTransacted = transacted;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
