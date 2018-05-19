<h1>Twitter Sentiment Analysis Using Kafka</h1>

[Logo1]: Logo1.png "Image Logo1.png" 
[Logo2]: Logo2.png "Image Logo2.png"
[Result]: Result.png "Image Result.png"

![Twitter Logo][Logo1] ![Kafka Logo][Logo2]
<h2>Requirements</h2>

You should have installed [Apache KafKa](https://kafka.apache.org/), [Apache Maven](https://maven.apache.org/), [Apache ZooKeeper](https://zookeeper.apache.org/) and at least JDK 1.8 on your system. Also, you'll need to have API Credentials from Twitter. Visit [Twitter Application Management](https://apps.twitter.com/) and please fill in the Twitter Credentials into the file below.


`src/main/java/com/asdbll/twitter/TwitterConstant.java`



<h2>Build</h2>

To build the application please run:

`mvn clean install`

<h2>How to Run Application</h2>

Start Zookeeper server by moving into the bin folder of Zookeeper installed directory by using:

`zkServer.sh start`

Start Kafka server by moving into the bin folder of Kafka installed directory by using:

`./kafka-server-start.sh ../config/server.properties`

Run the application with:

`mvn exec:java@kafka-producer`

and,

`mvn exec:java@kafka-consumer`

<h2>Result</h2>

The result of the sentiment analysis of tweets is as shown in the below and the results are updated in real-time.

![Result Image][Result]


