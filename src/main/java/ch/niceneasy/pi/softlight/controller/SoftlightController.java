package ch.niceneasy.pi.softlight.controller;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.niceneasy.pi.softlight.json.Values;

@Controller
public class SoftlightController {

	Logger logger = LoggerFactory.getLogger(SoftlightController.class);

	@Autowired
	ObjectMapper objectMapper;

	LinkedBlockingQueue<Values> queue = new LinkedBlockingQueue<Values>();

	@RequestMapping(value = "/api/softlight", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Values> postValues(@RequestBody Values values) {
		queue.add(values);
		try {
			logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(values));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(values);
	}

	@RequestMapping(value = "/metrics", produces = { "text/plain" }, method = RequestMethod.GET)
	ResponseEntity<String> metrics() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("# HELP http_request_duration_seconds A histogram of the request duration.\n");
		stringBuilder.append("# TYPE http_request_duration_seconds histogram\n");
		while (!queue.isEmpty()) {
			Values values = queue.poll();
			//http_requests_total{method="post",code="200"} 1027 1395066363000
			stringBuilder.append("softlight{color=\"red\"} ").append(values.getRed()).append(" ").append(values.getTimestamp()).append("\n");
			stringBuilder.append("softlight{color=\"green\"} ").append(values.getGreen()).append(" ").append(values.getTimestamp()).append("\n");
			stringBuilder.append("softlight{color=\"blue\"} ").append(values.getBlue()).append(" ").append(values.getTimestamp()).append("\n");
		}
		return ResponseEntity.ok(stringBuilder.toString());

	}

}
