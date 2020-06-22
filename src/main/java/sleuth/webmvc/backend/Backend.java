package sleuth.webmvc.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableAutoConfiguration
@EnableBinding(Sink.class)
public class Backend {

  @StreamListener(Sink.INPUT)
  public void onMessage(String message) {
    System.err.println("hello " + message);
  }

  public static void main(String[] args) {
    SpringApplication.run(Backend.class,
        "--spring.application.name=backend",
        "--server.port=9000",
        "--spring.cloud.stream.default.content-type=application/json;charset-UTF-8",
        "--spring.cloud.stream.default.group=backend",
        "--spring.cloud.stream.default.consumer.partitioned=true",
        "--spring.cloud.stream.default.consumer.headerMode=headers",
        "--spring.cloud.stream.bindings.input.destination=hello"
    );
  }

}
