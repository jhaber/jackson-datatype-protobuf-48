package net.evenh.protobuf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import net.evenh.domain.HelloWorld.HelloRequest;
import net.evenh.domain.HelloWorld.HelloResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple application to reproduce https://github.com/HubSpot/jackson-datatype-protobuf/issues/48.
 */
@SpringBootApplication
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  /**
   * Workaround proposed at https://github.com/HubSpot/jackson-datatype-protobuf/issues/48#issuecomment-414233456
   */
  class WorkaroundStrategy extends PropertyNamingStrategyBase {
    @Override
    public String translate(String input) {
      return input;
    }
  }

  @Configuration
  class JacksonConfig {
    @Value("${workaround:false}")
    private boolean enableWorkaround;

    @Bean
    Jackson2ObjectMapperBuilderCustomizer configureProtobufSupport() {
      return builder -> {
        builder.featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        builder.modulesToInstall(new ProtobufModule());

        if (enableWorkaround) {
          builder.propertyNamingStrategy(new WorkaroundStrategy());
        }
      };
    }
  }

  @RestController
  class HelloController {
    @PostMapping(value = "/hello")
    public HelloResponse helloWorld(@RequestBody HelloRequest req) {
      final String msg = "Hello " + req.getFirstName() + " " + req.getLastName();

      return HelloResponse.newBuilder()
          .setResponseMessage(msg)
          .setSomeRandomNumberVariable(42)
          .build();
    }
  }
}
