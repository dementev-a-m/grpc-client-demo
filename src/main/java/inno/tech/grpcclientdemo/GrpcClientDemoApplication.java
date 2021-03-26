package inno.tech.grpcclientdemo;

import inno.tech.grpcserverdemo.Client;
import inno.tech.grpcserverdemo.ClientServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class GrpcClientDemoApplication implements CommandLineRunner {

    private final ClientServiceGrpc.ClientServiceBlockingStub clientServiceBlockingStub;

    public static void main(String[] args) {
        SpringApplication.run(GrpcClientDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Client.ClientRequest clientRequest = Client.ClientRequest.newBuilder().setClientId(123456789L).build();
        Client.ClientResponse client = clientServiceBlockingStub.getClient(clientRequest);
        log.info(client.getClientId()+"\n" + client.getFirstName()+"\n"+ client.getLastName());
    }
}
