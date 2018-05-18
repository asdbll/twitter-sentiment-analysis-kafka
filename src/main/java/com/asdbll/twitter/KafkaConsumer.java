package com.asdbll.twitter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import edu.stanford.nlp.util.CoreMap;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import static com.asdbll.twitter.KafkaProducer.topic;

public class KafkaConsumer {

    public static int positiveCounter ;
    public static int negativeCounter ;
    public static int neutralCounter ;
    public static int counter ;

    DrawGraph drawGraph = new DrawGraph();

    private ConsumerConnector consumerConnector = null;
    PercentageCalculator calculator = new PercentageCalculator();
    SentimentAnalyzer analyzer = new SentimentAnalyzer();

    public void initialize() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "testgroup");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "300");
        props.put("auto.commit.interval.ms", "100");
        props.put("auto.offset.reset", "smallest");

        ConsumerConfig conConfig = new ConsumerConfig(props);
        consumerConnector = Consumer.createJavaConsumerConnector(conConfig);
    }

    public void consume() throws Exception {

        //Key = topic name, Value = No. of threads for topic
        Map<String, Integer> topicCount = new HashMap<String, Integer>();
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumerConnector.createMessageStreams(topicCount);

        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);

        StringBuilder string = new StringBuilder();


        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();

            while (it.hasNext()) {

                String str = new String(it.next().message());

                List<CoreMap> annotations = analyzer.getAnnotations(str);

                int sentimentType = analyzer.getSentimentClass(annotations);
                counter++;

                switch (sentimentType) {
                    case 0:
                        negativeCounter++;
                        break;
                    case 1:
                        negativeCounter++;
                        break;
                    case 2:
                        neutralCounter++;
                        break;
                    case 3:
                        positiveCounter++;
                        break;
                    case 4:
                        positiveCounter++;
                        break;
                }

                drawGraph.Dataset(positiveCounter,negativeCounter,neutralCounter);
                drawGraph.refreshChart();

                string.append('\r');
                string.append("Number of Tweets: ");
                string.append(counter);
                string.append(" Positive: ");
                string.append(String.format("%.2f",calculator.calcPositive(counter,positiveCounter)));
                string.append("%");
                string.append(" (" + positiveCounter + ") ");
                string.append(" Neutral: ");
                string.append(String.format("%.2f",calculator.calcNeutral(counter,neutralCounter)));
                string.append("%");
                string.append(" (" + neutralCounter + ") ");
                string.append(" Negative: ");
                string.append(String.format("%.2f",calculator.calcNegative(counter,negativeCounter)));
                string.append("%");
                string.append(" (" + negativeCounter + ") ");

                System.out.print(string);

            }
        }
        consumerConnector.shutdown();
    }

    public static void main(String[] args) throws Exception {

        KafkaConsumer kafkaConsumer = new KafkaConsumer();

        kafkaConsumer.initialize();
        kafkaConsumer.consume();
    }
}


