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
import java.util.Date;
import java.io.Console;

public class BTS {

    private static final String EXCHANGE_NAME = "BTS";

    public static final byte[] buf = new byte[200];

    public static void main(String[] argv) throws Exception {
        //Remote time

        //String TIME_SERVER = "time-a.nist.gov";
        String IpAddress = "127.0.0.1";
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(IpAddress);

//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUsername("test1");
//        factory.setPassword("test1");
//        //factory.setVirtualHost();
//        factory.setHost("25.67.28.99");
//        //factory.setPort(portNumber);
//        Connection conn = factory.newConnection();
//        Channel channel = conn.createChannel();

         ConnectionFactory factory = new ConnectionFactory();
         factory.setHost("localhost");
         Connection connection = factory.newConnection();
         Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        while(true) {
            for (int j = 500; j > 0; --j){
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                long start_time = timeInfo.getReturnTime();
                //long start_time = System.currentTimeMillis();
                System.out.println(start_time);
                buf[0] = 4;
                byte[] bytes = ByteBuffer.allocate(8).putLong(start_time).array();
                System.arraycopy(bytes, 0, buf, 1, 7);

                channel.basicPublish(EXCHANGE_NAME, "", null, buf);
             }
            Thread.sleep(60*1000 / Config.dif);
        }
    }

}