package ch.niceneasy.pi.softlight.controller;

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

	@RequestMapping(value = "/api/softlight", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Values> postValues(@RequestBody Values values) {
		try {
			logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(values));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(values);
	}

}
