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

public class Subscriber1 {

    private static final String EXCHANGE_NAME = "Gathering";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "Animals");
        channel.queueBind(queueName, EXCHANGE_NAME, "BTS");
        channel.queueBind(queueName, EXCHANGE_NAME, "BTSDrone");

        System.out.println("Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String fileName = "error";
                switch (body[0]) {
                    case(3):
                        fileName = "Animal.txt";
                        break;

                    case(4):
                        fileName = "BTS.txt";
                        break;

                    case(5):
                        fileName = "BTS.txt";
                        break;
                }

                PrintWriter writer = new PrintWriter(new FileOutputStream(
                        new File(fileName),
                        true /* append = true */));

                System.out.println(body[0] + "    " + Config.getLong(body, 1));
                writer.print(Config.getLong(body, 1));
                writer.print(';');
                writer.print(System.currentTimeMillis());
                writer.println();
                writer.close();


            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}