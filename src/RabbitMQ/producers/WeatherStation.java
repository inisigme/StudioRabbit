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

    private static final String EXCHANGE_NAME = "WeatherStation";
    public static final byte[] buf = new byte[20];
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        while(true) {
            for(int j = 11; j>0; --j) {
                long start_time = System.currentTimeMillis();
                System.out.println(start_time);
                buf[0] = 2;
                byte[] bytes = ByteBuffer.allocate(8).putLong(start_time).array();
                System.arraycopy(bytes, 0, buf, 1, 7);

                channel.basicPublish(EXCHANGE_NAME, "", null, buf);
            }
            Thread.sleep(5*60*1000 / Config.dif);
        }

    }

}