package com.asdbll.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class KafkaProducer {

    public static final String topic = "twitter-stream-topic";
    public static final String term = "#TuesdayThoughts";
    public static void run() throws TwitterException, InterruptedException {

        Properties properties = new Properties();
        properties.put("metadata.broker.list", "localhost:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("client.id", "camus");
        ProducerConfig producerConfig = new ProducerConfig(properties);
        kafka.javaapi.producer.Producer<String, String> producer = new kafka.javaapi.producer.Producer<String, String>(
                producerConfig);

        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        endpoint.trackTerms(Lists.newArrayList(term));

        String consumerKey = TwitterConstant.CONSUMER_KEY_KEY;
        String consumerSecret = TwitterConstant.CONSUMER_SECRET_KEY;
        String accessToken = TwitterConstant.ACCESS_TOKEN_KEY;
        String accessTokenSecret = TwitterConstant.ACCESS_TOKEN_SECRET_KEY;

        Authentication auth = new OAuth1(consumerKey, consumerSecret, accessToken,
                accessTokenSecret);

        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();

        client.connect();

        for (int msgRead = 0; msgRead < 1000; msgRead++) {
            String rawJSON = queue.take();
            Status status = TwitterObjectFactory.createStatus(rawJSON);
            KeyedMessage<String, String> message = null;
            try {
                message = new KeyedMessage<String, String>(topic, status.getText());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Stream Ended.");
            }
            producer.send(message);
        }
        producer.close();
        client.stop();
    }

    public static void main(String[] args) {
        try {
            KafkaProducer.run();
        } catch (TwitterException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
