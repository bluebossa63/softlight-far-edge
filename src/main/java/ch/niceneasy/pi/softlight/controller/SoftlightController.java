package ch.niceneasy.pi.softlight.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.niceneasy.pi.softlight.json.Values;

@Controller
public class SoftlightController {

	Logger logger = LoggerFactory.getLogger(SoftlightController.class);

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${file.upload.dir:/var/uploads}")
	private String uploadDirectory;

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
			// http_requests_total{method="post",code="200"} 1027 1395066363000
			stringBuilder.append("softlight{color=\"red\"} ").append(values.getRed()).append(" ")
					.append(values.getTimestamp()).append("\n");
			stringBuilder.append("softlight{color=\"green\"} ").append(values.getGreen()).append(" ")
					.append(values.getTimestamp()).append("\n");
			stringBuilder.append("softlight{color=\"blue\"} ").append(values.getBlue()).append(" ")
					.append(values.getTimestamp()).append("\n");
		}
		return ResponseEntity.ok(stringBuilder.toString());

	}

	@RequestMapping(value = "/updater/{package}", produces = { "text/plain" }, method = RequestMethod.GET)
	ResponseEntity<InputStreamResource> updater(@RequestHeader(name = "If-Modified-Since", required = false) String ifModifiedSince,
			@PathVariable("package") String _package) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
				.withZone(ZoneId.of("GMT"));

		Path file = FileSystems.getDefault().getPath(uploadDirectory, _package);
		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
		LocalDateTime lastModifiedTime = LocalDateTime
				.ofInstant(Instant.ofEpochMilli(attr.lastModifiedTime().toMillis()), df.getZone()).withNano(0);

		try {
			LocalDateTime ifModifiedSinceLdt = ifModifiedSince != null ? LocalDateTime.parse(ifModifiedSince, df)
					: null;

			if (ifModifiedSince == null || lastModifiedTime.isAfter(ifModifiedSinceLdt)) {
				FileInputStream update = new FileInputStream(file.toFile());
				InputStreamResource inputStreamResource = new InputStreamResource(update);
				return ResponseEntity.ok().header("Modified-Last", lastModifiedTime.format(df))
						.body(inputStreamResource);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).header("Modified-Last", lastModifiedTime.format(df))
				.body(null);
	}

	@RequestMapping(value = "/uploadFile", produces = {
			"text/plain" }, method = RequestMethod.POST)
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/updater/").path(fileName)
				.toUriString();
		return ResponseEntity.status(HttpStatus.ACCEPTED).header("Location", fileDownloadUri).body(fileDownloadUri);
	}

}
