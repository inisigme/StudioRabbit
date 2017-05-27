package RabbitMQ.producers;

/**
 * Created by Inisigme on 26-May-17.
 */
import RabbitMQ.Config;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.Console;

public class BTS {

    private static final String EXCHANGE_NAME = "Gathering";

    public static final byte[] buf = new byte[200];

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        int l = 0;
        while(true) {
            channel.basicPublish(EXCHANGE_NAME, "BTS", null, buf);
            System.out.println(l);
            l+=buf.length;
            Thread.sleep(2*60*1000 / Config.dif);
        }
    }

}