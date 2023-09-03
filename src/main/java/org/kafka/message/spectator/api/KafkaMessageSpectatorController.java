package org.kafka.message.spectator.api;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.kafka.message.spectator.consumer.KafkaMessageSpectator;
import org.kafka.message.spectator.consumer.KafkaSpectatorFactory;
import org.kafka.message.spectator.domain.SpectatorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@Qualifier("kafkaMessageSpectator")
	private KafkaMessageSpectator<String, String> kafkaMessageSpectator;
	
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
			
			KafkaConsumer<String, String> consumer = this.kafkaSpectatorFactory.createConsumer();
			// Subscribe to the topic.
			
			return this.kafkaMessageSpectator.messageCount(consumer, spectatorInput);
		} catch (Exception anyException){
			LOGGER.error("Error while calculating message count", anyException);
			return -1;
		}
	}
	
	@GetMapping(value = "/poll-messages/{topic}/{startPosition}/{endPosition}/{pollTimeInSeconds}",
					produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> pollMessages(@PathVariable(value = "topic") String topic,
											@PathVariable(value = "startPosition") int startPosition,
											@PathVariable(value = "endPosition") int endPosition,
											@PathVariable(value = "pollTimeInSeconds", required = false) int pollTimeInSeconds){
		try {
			
			SpectatorInput spectatorInput = new SpectatorInput();
			spectatorInput.setTopic(topic);
			spectatorInput.setStartPosition(startPosition);
			spectatorInput.setEndPosition(endPosition);
			if(pollTimeInSeconds > 0){
				spectatorInput.setPollTime(pollTimeInSeconds);
			}
			LOGGER.info("Fetching messages for {}", spectatorInput);
			
			KafkaConsumer<String, String> consumer = this.kafkaSpectatorFactory.createConsumer();
			// Subscribe to the topic.
			
			return this.kafkaMessageSpectator.pollMessages(consumer, spectatorInput);
		} catch (Exception anyException){
			LOGGER.error("Error while calculating messages", anyException);
			return new HashMap<>();
		}
	}
	
}
