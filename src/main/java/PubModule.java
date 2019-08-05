import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class PubModule {
    public static void main(String[] args)
            throws IOException, TimeoutException {
        System.out.println("Exhange module");

        String ExchangeName = "rmq.direct";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("rabbitmq");
        factory.setPassword("1234");
        factory.setHost("192.168.0.104"); //cn4
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclarePassive(ExchangeName);



        /**** basic Property Builder *****/
        AMQP.BasicProperties.Builder builder
                = new AMQP.BasicProperties.Builder();
        Map<String,Object> headerMap = new HashMap<String,   Object>();
        headerMap.put("pubIP", InetAddress.getLocalHost().getHostAddress());
        headerMap.put("pubHostname",InetAddress.getLocalHost().getHostName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss", Locale.UK);
        Date date =new Date();
        dateFormat.format(date);

        builder.timestamp(date).appId("**Foo * Bar * AppIdField**").headers(headerMap);
        AMQP.BasicProperties theProps = builder.build();

        String routingKey = "dcpu";
        String msg = "*****TEST MESSAGE*****Routing key - dcpu";

        channel.basicPublish(ExchangeName, routingKey,
                theProps,msg.getBytes("UTF-8"));
        System.out.println(" [x] Sent ' " + msg +" '");




        channel.close();
        connection.close();
    }

}