package RabbitMQ.producers;

/**
 * Created by Inisigme on 26-May-17.
 */
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.Console;

public class BTSDrone {

    private static final String EXCHANGE_NAME = "Gathering";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Console console = System.console();
        channel.basicPublish(EXCHANGE_NAME, "BTSDrone", null, new String("tresc").getBytes("UTF-8"));



    }

}