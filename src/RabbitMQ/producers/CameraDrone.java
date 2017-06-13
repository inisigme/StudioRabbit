package RabbitMQ.producers;

/**
 * Created by Inisigme on 26-May-17.
 */
import RabbitMQ.Config;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.nio.ByteBuffer;

import java.io.Console;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraDrone {

    private static final String EXCHANGE_NAME = "Gathering";
    public static final byte[] buf = new byte[1024*1024];

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        while(true) {
            int counter = 60 * 20 / Config.dif;
            while (counter > 0) {
                long start_time = System.currentTimeMillis();
                System.out.println(start_time);
                buf[0] = 1;
                byte[] bytes = ByteBuffer.allocate(8).putLong(start_time).array();
                System.arraycopy(bytes, 0, buf, 1, 7);

                channel.basicPublish(EXCHANGE_NAME, "CameraDrone", null, buf);
                counter--;
                Thread.sleep(1000 / Config.dif);
            }
            Thread.sleep(1000*60*10);
        }

    }

}