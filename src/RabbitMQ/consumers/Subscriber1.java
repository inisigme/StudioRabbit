package RabbitMQ.consumers;

/**
 * Created by Inisigme on 26-May-17.
 */
import RabbitMQ.Config;
import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.*;

public class Subscriber1 {

    //private static final String EXCHANGE_NAME = "Gathering";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //Exchanges avaliable :
        //Animals, BTS, BTSDrone, WeatherStation, CameraDrone;

        channel.exchangeDeclare("Animals", BuiltinExchangeType.FANOUT);
        channel.exchangeDeclare("BTS", BuiltinExchangeType.FANOUT);
        channel.exchangeDeclare("BTSDrone", BuiltinExchangeType.FANOUT);

        String queueAnimal1 = channel.queueDeclare().getQueue();
        String queueBTS1 = channel.queueDeclare().getQueue();
        String queueBTSDrone1 = channel.queueDeclare().getQueue();


        channel.queueBind(queueAnimal1, "Animals", "");
        channel.queueBind(queueBTSDrone1, "BTSDrone", "");
        channel.queueBind(queueBTS1, "BTS", "");

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
                long get = System.currentTimeMillis();
                long time = get - ByteBuffer.wrap(body,1,8).getLong();
                writer.print(get+";");
                writer.print(time);
                writer.println(";");
                writer.close();
            }
        };

        channel.basicConsume(queueAnimal1, true, consumer);
        channel.basicConsume(queueBTS1, true, consumer);
        channel.basicConsume(queueBTSDrone1, true, consumer);
    }
}