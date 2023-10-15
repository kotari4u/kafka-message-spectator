package org.kafka.message.spectator.api;

import org.kafka.message.spectator.config.KafkaMessageSpectateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * This is to provide configuration to UI
 * So that users can choose the host and consumer group
 *
 * @author Hemambara vamsi, kotari
 */
@RestController
@RequestMapping("/spectate")
public class KafkaMessageSpectatorConfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageSpectatorConfigController.class);
	
	@Autowired
	@Qualifier("config")
	private KafkaMessageSpectateConfig config;
	
	/**
	 * This is to retrieve kafka configuration with hosts, topics, consumer groups.
	 *
	 * ref: <a href="http://localhost:8080/spectate/consumer/config">Sample link</a>
	 *
	 * @return List<KafkaMessageSpectateConfig.ConsumerConfig>
	 *     Ex: {"host":"localhost:9092","consumerGroups":["MySampleConsumer1","MySampleConsumer2"]}
	 */
	@GetMapping(value = "/consumer/config",
				produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<KafkaMessageSpectateConfig.ConsumerConfig> consumerConfig(){
		try {
			return this.config.getConsumers();
		} catch (Exception anyException){
			LOGGER.error("Error while calculating message count", anyException);
			return new ArrayList<>();
		}
	}
}
