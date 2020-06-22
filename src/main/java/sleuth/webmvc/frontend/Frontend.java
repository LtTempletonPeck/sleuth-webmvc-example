package sleuth.webmvc.frontend;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@EnableBinding(Source.class)
@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {

  @Autowired OutputGateway outputGateway;

  @Autowired KafkaTemplate<String, String> kafkaTemplate;

  @RequestMapping("/")
  public String callBackend() {
    String messageId = UUID.randomUUID().toString();
    String message = "sent " + messageId;
    outputGateway.output(message, messageId.getBytes(StandardCharsets.UTF_8));
    return message;
  }

  @MessagingGateway
  interface OutputGateway {

    @Gateway(requestChannel = Source.OUTPUT)
    public void output(String message, @Header(KafkaHeaders.MESSAGE_KEY) byte[] key);

  }

  public static void main(String[] args) {
    SpringApplication.run(Frontend.class,
        "--spring.application.name=frontend",
        "--server.port=8081",
        "--spring.cloud.stream.default.content-type=application/json;charset-UTF-8",
        "--spring.cloud.stream.default.group=frontend",
        "--spring.cloud.stream.default.producer.headerMode=headers",
        "--spring.cloud.stream.bindings.output.destination=hello",
        "--spring.cloud.stream.bindings.output.producer.partitionKeyExpression=payload"
    );
  }

}
