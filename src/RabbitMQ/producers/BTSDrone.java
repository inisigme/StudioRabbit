package RabbitMQ.producers;

/**
 * Created by Inisigme on 26-May-17.
 */
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.nio.ByteBuffer;

public class BTSDrone {

    public static final byte[] buf = new byte[200];
    private static final String EXCHANGE_NAME = "BTSDrone";

    public static void main(String[] argv) throws Exception {

//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUsername("test");
//        factory.setPassword("test");
//        //factory.setVirtualHost();
//        factory.setHost("192.168.1.100");
//        //factory.setPort(portNumber);
//        Connection conn = factory.newConnection();
//        Channel channel = conn.createChannel();

        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUri("amqp://xxfikmfg:JFchWCnY03w_XRNGK-2cnna21Ag-NIq_@golden-kangaroo.rmq.cloudamqp.com/xxfikmfg");
        factory.setUri("amqp://mvgskkgb:2hOkp0TFI8lYnw7bhARKpIMt1KV5GqEL@lark.rmq.cloudamqp.com/mvgskkgb");
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        // ConnectionFactory factory = new ConnectionFactory();
        // factory.setHost("localhost");
        // Connection connection = factory.newConnection();
        // Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        while(true) {
            long start_time = System.currentTimeMillis() ;
            System.out.println(start_time);
            buf[0]=5;
            byte [] bytes = ByteBuffer.allocate(8).putLong(start_time).array();
            System.arraycopy(bytes, 0, buf, 1, 7);

            channel.basicPublish(EXCHANGE_NAME, "", null, buf);

            Thread.sleep(60*1000*2);
        }
    }

}