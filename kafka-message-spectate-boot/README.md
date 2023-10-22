# kafka-message-spectator
Spectate kafka messages by dynamically connecting to Kafka topic using consumer groups

## Summary
Kafka message spectator boot is a spring boot component that connects to Kafka based on the kafka 
configuration defined in application.yml files. It polls messages for viewing purposes. It can support SSL functionality. It requires developer to configure SSL params
<br/>
It does not provide any admin related functionalities like creating topics, start/stop/restart clusters etc
<br/>
We can configure more than N Kafka hosts and consumer groups & topics for each Kafka host
Ensure kafka consumers groups have access to respective topics to publish a message

Tha parent git repo has UI functionality as well to call the API exposed in this boot application. In order to use UI, go through <a href="https://github.com/kotari4u/kafka-message-spectator/blob/phase-1/kafka-message-spectate-ui/README.md"> Kafka Message Spectator UI </a>

## Features
    -   Poll Messages by Offset
        
    -   Poll messages by Date

<B><u>Offset message poll</u></B> - This feature is used to poll the messages by message count. Offset is nothing a but a number how far you want to go back and fetch. For example if there are 10000 messages, if user input offset = 2000, then it will fetch all the messages from = 8000 to 9000

<B><u>Message Poll by Date</u></B> - Fetch messages from given time and date.

<br/>
In both the scenarios max poll time can be passed in request to poll 1000 messages in specified time. Make sure you provide decent poll time to avoid blocking other users if there are issues fetching messages


<u>Note</u>: By default it can search only 1000 messages. Thats why in the example it only fetches from 8000 - 9000. Changing the max poll message count requires a consumer group to be destroyed and recreated
This feature is not available yet.<br/> If you like to change the message count, feel free to change in application.dynamically
But once the application comes up, it cannot change it dynamically. It requires a config change and a restart

Exposed rest end points to hit from localhost:

Pull configuration : http://localhost:8080/spectate/consumer/config <br/>
Pull messages by offset: http://localhost:8080/spectate/poll-messages-by-offset/localhost:9092/MySampleConsumer/my-topic/1/2
Pull messages by date: http://localhost:8080/spectate/poll-messages-by-date/localhost:9092/MySampleConsumer2/my-topic-p5/2023-10-22T08:30:00/10

## Synchronous retrieval per consumer group
Inorder to pull the messages from a topic using a consumer group, we need to move the offset accordingly. 
<br/>
Moving offset and message poll operations have to be done atomically. If in between if another request try to use the same consumer group
then it could move the offset and corrupts the first request. In order to achieve this, polling per consumer group is a synchronous operartion
In order to support more users, create more consumer groups


## References
There are few other open source Kafka message viewers available along admin functionalities
- <a href="https://github.com/obsidiandynamics/kafdrop">KafDrop</a>. Note: Few of the references here are pulled from KafDrop
