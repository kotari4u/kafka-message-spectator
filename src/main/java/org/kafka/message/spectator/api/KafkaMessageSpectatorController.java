package org.kafka.message.spectator.api;

import org.kafka.message.spectator.consumer.KafkaMessageSpectator;
import org.kafka.message.spectator.consumer.KafkaSpectatorFactory;
import org.kafka.message.spectator.domain.ConsumerInfo;
import org.kafka.message.spectator.domain.SpectatorDateInput;
import org.kafka.message.spectator.domain.SpectatorInput;
import org.kafka.message.spectator.domain.SpectatorOffsetInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
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
	 * ref: http://localhost:8080/spectate/message_count/my-topic
	 * @param topic
	 * @return
	 */
	@GetMapping("/message_count/{topic}")
	public long messageCount(@PathVariable(value = "topic") String topic){
		try {
			LOGGER.info("message_count {}", topic);
			SpectatorInput spectatorInput = new SpectatorInput();
			spectatorInput.setTopic(topic);
			
			ConsumerInfo consumerInfo = this.kafkaSpectatorFactory.createConsumer();
			// Subscribe to the topic.
			
			return this.kafkaMessageCount.spectate(consumerInfo, spectatorInput);
		} catch (Exception anyException){
			LOGGER.error("Error while calculating message count", anyException);
			return -1;
		}
	}
	
	/**
	 * Ex: http://localhost:8080/spectate/poll-messages-by-offset/my-topic-p5/1/2
	 * @param topic
	 * @param startPosition
	 * @param pollTimeInSeconds
	 * @return
	 */
	@GetMapping(value = "/poll-messages-by-offset/{topic}/{startPosition}/{pollTimeInSeconds}",
					produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> pollMessagesByOffset(@PathVariable(value = "topic") String topic,
											@PathVariable(value = "startPosition") int startPosition,
											@PathVariable(value = "pollTimeInSeconds", required = false) int pollTimeInSeconds){
		try {
			
			SpectatorOffsetInput spectatorInput = new SpectatorOffsetInput();
			spectatorInput.setTopic(topic);
			spectatorInput.setStartPosition(startPosition);
			if(pollTimeInSeconds > 0){
				spectatorInput.setPollTime(pollTimeInSeconds);
			}
			LOGGER.info("Fetching messages for {}", spectatorInput);
			
			ConsumerInfo consumerInfo = this.kafkaSpectatorFactory.createConsumer();
			
			return this.kafkaMessagePollByOffset.spectate(consumerInfo, spectatorInput);
		} catch (Exception anyException){
			LOGGER.error("Error while calculating messages", anyException);
			return new HashMap<>();
		}
	}
	
	/**
	 * Ex: http://localhost:8080/spectate/poll-messages-by-date/my-topic-p5/2023-09-03T00:15:30/
	 *
	 * @param topic
	 * @param dateTime
	 * @param pollTimeInSeconds
	 * @return
	 */
	@GetMapping(value = "/poll-messages-by-date/{topic}/{dateTime}/{pollTimeInSeconds}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> pollMessagesByDate(@PathVariable(value = "topic") String topic,
											@PathVariable(value = "dateTime") String dateTime,
											@PathVariable(value = "pollTimeInSeconds", required = false) int pollTimeInSeconds){
		try {
			
			SpectatorDateInput spectatorInput = new SpectatorDateInput();
			spectatorInput.setTopic(topic);
			spectatorInput.setDateTime(LocalDateTime.parse(dateTime));
			if(pollTimeInSeconds > 0){
				spectatorInput.setPollTime(pollTimeInSeconds);
			}
			LOGGER.info("Fetching messages for {}", spectatorInput);
			
			ConsumerInfo consumerInfo = this.kafkaSpectatorFactory.createConsumer();
			
			return this.kafkaMessagePollByDate.spectate(consumerInfo, spectatorInput);
		} catch (Exception anyException){
			LOGGER.error("Error while calculating messages", anyException);
			return new HashMap<>();
		}
	}
	
}
