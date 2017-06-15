package RabbitMQ.consumers;

/**
 * Created by Inisigme on 26-May-17.
 */
import RabbitMQ.Config;
import RabbitMQ.producers.CameraDrone;
import com.rabbitmq.client.*;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Subscriber2 {

    //private static final String EXCHANGE_NAME = "";

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
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("CameraDrone", BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare("WeatherStation", BuiltinExchangeType.FANOUT);

        String queueCamera1 = channel.queueDeclare().getQueue();
        String queueWeather1 = channel.queueDeclare().getQueue();

        channel.queueBind(queueCamera1, "CameraDrone", "");
        channel.queueBind(queueWeather1, "WeatherStation", "");

        System.out.println("Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {

                String fileName = envelope.getExchange() + ".txt";
                System.out.println("delivered from "+envelope.getExchange());

                PrintWriter writer = new PrintWriter(new FileOutputStream(
                        new File(fileName),
                        true /* append = true */));

                long get = System.currentTimeMillis() - timeDiff;
                long time = get - ByteBuffer.wrap(body,1,8).getLong();
                writer.print(get+";");
                writer.print(time);
                writer.println(";");
                writer.close();
            }
        };

        channel.basicConsume(queueCamera1, true, consumer);
        channel.basicConsume(queueWeather1, true, consumer);
    }
}