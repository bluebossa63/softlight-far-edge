package ch.niceneasy.pi.softlight;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = { "ch.niceneasy.pi.softlight" })
@EnableConfigurationProperties 
public class SoftlightApplicationConfiguration {

}
