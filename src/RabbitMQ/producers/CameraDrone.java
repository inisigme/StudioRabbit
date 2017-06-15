package RabbitMQ.producers;

/**
 * Created by Inisigme on 26-May-17.
 */
import RabbitMQ.Config;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import java.io.Console;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraDrone {

    private static final String EXCHANGE_NAME = "CameraDrone";
    public static final byte[] buf = new byte[1024*1024];

    public static void main(String[] argv) throws Exception {

        String TIME_SERVER = "time-a.nist.gov";
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo;

        long timeBefore = System.currentTimeMillis();
        timeInfo = timeClient.getTime(inetAddress);
        long timeAfter = System.currentTimeMillis();
        long timeDiff = (timeBefore + timeAfter - 2*timeInfo.getReturnTime())/2;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://xxfikmfg:JFchWCnY03w_XRNGK-2cnna21Ag-NIq_@golden-kangaroo.rmq.cloudamqp.com/xxfikmfg");
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUsername("test1");
//        factory.setPassword("test1");
//        //factory.setVirtualHost();
//        factory.setHost("25.67.28.99");
//        //factory.setPort(portNumber);
//        Connection conn = factory.newConnection();
//        Channel channel = conn.createChannel();

        // ConnectionFactory factory = new ConnectionFactory();
        // factory.setHost("localhost");
        // Connection connection = factory.newConnection();
        // Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        while(true) {
            int counter = 60 * 20 / Config.dif;
            while (counter > 0) {
                long start_time = System.currentTimeMillis()-timeDiff;
                System.out.println(start_time);
                buf[0] = 1;
                byte[] bytes = ByteBuffer.allocate(8).putLong(start_time).array();
                System.arraycopy(bytes, 0, buf, 1, 7);

                channel.basicPublish(EXCHANGE_NAME, "", null, buf);
                counter--;
                Thread.sleep(1000 / Config.dif);
            }
            Thread.sleep(1000*60*10);
        }

    }

}