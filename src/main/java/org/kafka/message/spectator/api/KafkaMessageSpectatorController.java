package org.kafka.message.spectator.api;

import org.kafka.message.spectator.consumer.KafkaMessageSpectator;
import org.kafka.message.spectator.consumer.KafkaSpectatorFactory;
import org.kafka.message.spectator.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Hemambara vamsi, kotari
 */
@RestController
@RequestMapping("/spectate")
public class KafkaMessageSpectatorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageSpectatorController.class);
	
	@Autowired
	@Qualifier("kafkaMessageCount")
	private KafkaMessageSpectator<String, String, Long> kafkaMessageCount;
	
	@Autowired
	@Qualifier("kafkaMessagePollByOffset")
	private KafkaMessageSpectator<String, String, Map<String, String>> kafkaMessagePollByOffset;
	
	@Autowired
	@Qualifier("kafkaMessagePollByDate")
	private KafkaMessageSpectator<String, String, Map<String, String>> kafkaMessagePollByDate;
	
	@Autowired
	@Qualifier("kafkaSpectatorFactory")
	private KafkaSpectatorFactory kafkaSpectatorFactory;
	
	/**
	 * ref: <a href="http://localhost:8080/spectate/message_count/localhost:9092/MySampleConsumer/my-topic">Sample link</a>
	 *
	 * @param topic - Topic to retrieve messages from
	 * @return - long - number of messages in a topic
	 */
	@GetMapping("/message_count/{host}/{consumerGroup}/{topic}")
	@ResponseBody
	public long messageCount(
			@PathVariable(value = "host") String host,
			@PathVariable(value = "consumerGroup") String consumerGroup,
			@PathVariable(value = "topic") String topic){
		try {
			LOGGER.info("message_count {}", topic);
			SpectatorInput spectatorInput = new SpectatorInput();
			spectatorInput.setHost(host);
			spectatorInput.setConsumerGroup(consumerGroup);
			spectatorInput.setTopic(topic);
			
			ConsumerInfo consumerInfo = this.kafkaSpectatorFactory.createConsumer(spectatorInput);
			// Subscribe to the topic.
			
			return this.kafkaMessageCount.spectate(consumerInfo, spectatorInput);
		} catch (Exception anyException){
			LOGGER.error("Error while calculating message count", anyException);
			return -1;
		}
	}
	
	/**
	 * ref: <a href="http://localhost:8080/spectate/poll-messages-by-offset/localhost:9092/MySampleConsumer/my-topic/1/2">Sample link</a>
	 *
	 * @param host - Kafka host url
	 * @param consumerGroup - consumer group to retrieve messages from Kafka
	 * @param topic - Topic to retrieve messages from
	 * @param startPosition - Position to start with. This position # is offset read from the last
	 * @param pollTimeInSeconds - Max poll to fetch messages. Consumer will pull # of messages with in the time limit
	 *                          If we are trying to retrieve more messages / there are any network delays
	 *                          Give more time to poll
	 * @return - Messages with key value pair
	 */
	@GetMapping(value = "/poll-messages-by-offset/{host}/{consumerGroup}/{topic}/{startPosition}/{pollTimeInSeconds}",
					produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SpectatorOutput<String, String>> pollMessagesByOffset(
			@PathVariable(value = "host") String host,
			@PathVariable(value = "consumerGroup") String consumerGroup,
			@PathVariable(value = "topic") String topic,
			@PathVariable(value = "startPosition") int startPosition,
			@PathVariable(value = "pollTimeInSeconds", required = false) int pollTimeInSeconds){
		try {
			
			SpectatorOffsetInput spectatorInput = new SpectatorOffsetInput();
			spectatorInput.setHost(host);
			spectatorInput.setConsumerGroup(consumerGroup);
			spectatorInput.setTopic(topic);
			spectatorInput.setStartPosition(startPosition);
			if(pollTimeInSeconds > 0){
				spectatorInput.setPollTime(pollTimeInSeconds);
			}
			LOGGER.info("Fetching messages for {}", spectatorInput);
			
			ConsumerInfo consumerInfo = this.kafkaSpectatorFactory.createConsumer(spectatorInput);
			
			Map<String, String> messages = this.kafkaMessagePollByOffset.spectate(consumerInfo, spectatorInput);
			return new SpectatorDataOutput<>(messages).getMessages();
		} catch (Exception anyException){
			LOGGER.error("Error while calculating messages", anyException);
			return new SpectatorDataOutput<String, String>().getMessages();
		}
	}
	
	/**
	 * ref: <a href="http://localhost:8080/spectate/poll-messages-by-date/localhost:9092/MySampleConsumer/my-topic-p5/2023-09-03T00:15:30/">Sample link</a>
	 *
	 * @param topic - Topic to retrieve messages from
	 * @param dateTime - Date time to start with
	 * @param pollTimeInSeconds - Max poll to fetch messages. Consumer will pull # of messages with in the time limit
	 * 	 *                          If we are trying to retrieve more messages / there are any network delays
	 * 	 *                          Give more time to poll
	 * @return - Messages in a key value format
	 */
	@GetMapping(value = "/poll-messages-by-date/{host}/{consumerGroup}/{topic}/{dateTime}/{pollTimeInSeconds}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SpectatorOutput<String, String>> pollMessagesByDate(
			@PathVariable(value = "host") String host,
			@PathVariable(value = "consumerGroup") String consumerGroup,
			@PathVariable(value = "topic") String topic,
			@PathVariable(value = "dateTime") String dateTime,
			@PathVariable(value = "pollTimeInSeconds", required = false) int pollTimeInSeconds){
		try {
			
			SpectatorDateInput spectatorInput = new SpectatorDateInput();
			spectatorInput.setHost(host);
			spectatorInput.setConsumerGroup(consumerGroup);
			spectatorInput.setTopic(topic);
			spectatorInput.setDateTime(LocalDateTime.parse(dateTime));
			if(pollTimeInSeconds > 0){
				spectatorInput.setPollTime(pollTimeInSeconds);
			}
			LOGGER.info("Fetching messages for {}", spectatorInput);
			
			ConsumerInfo consumerInfo = this.kafkaSpectatorFactory.createConsumer(spectatorInput);
			
			Map<String, String> messages = this.kafkaMessagePollByDate.spectate(consumerInfo, spectatorInput);
			return new SpectatorDataOutput<>(messages).getMessages();
		} catch (Exception anyException){
			LOGGER.error("Error while calculating messages", anyException);
			return new SpectatorDataOutput<String, String>().getMessages();
		}
	}
	
}
