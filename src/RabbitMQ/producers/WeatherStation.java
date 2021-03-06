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
import java.nio.ByteBuffer;

public class WeatherStation {

    private static final String EXCHANGE_NAME = "Gathering";
    public static final byte[] buf = new byte[10*11];
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        int l = 0;
        while(true) {
            long start_time = System.currentTimeMillis() ;
            System.out.println(start_time);
            buf[0]=2;
            byte [] bytes = ByteBuffer.allocate(8).putLong(start_time).array();
            for(int i = 0; i < 8; i++)
                buf[i+1] = bytes[i];

            channel.basicPublish(EXCHANGE_NAME, "WeatherStation", null, buf);
            System.out.println(l);
            l+=buf.length;
            Thread.sleep(10*60*1000 / Config.dif);
        }

    }

}