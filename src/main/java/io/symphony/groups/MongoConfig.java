package io.symphony.groups;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import io.symphony.common.conversion.StringToUnitConverter;
import io.symphony.common.conversion.UnitToStringConverter;

@Configuration
public class MongoConfig {

	  @Bean
	  @ConditionalOnMissingBean
	  public MongoCustomConversions mongoCustomConversions() {
	    return new MongoCustomConversions(
	        Arrays.asList(
	            new UnitToStringConverter(),
	            new StringToUnitConverter()
            )
        );
	  }
	
}
