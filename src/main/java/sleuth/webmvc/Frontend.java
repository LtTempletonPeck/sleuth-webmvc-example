package sleuth.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@EnableAutoConfiguration
@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {

  @Autowired WebClient webClient;

  String backendBaseUrl = System.getProperty("spring.example.backendBaseUrl", "http://localhost:9000");

  @RequestMapping("/") public Mono<String> callBackend() {
    return webClient.get().uri("/api").retrieve().bodyToMono(String.class);
  }

  @Bean WebClient webClient() {
    return WebClient.create(backendBaseUrl);
  }

  public static void main(String[] args) {
    SpringApplication.run(Frontend.class,
        "--spring.application.name=frontend",
        "--server.port=8081"
    );
  }
}
