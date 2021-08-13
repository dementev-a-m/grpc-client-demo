package ru.dementev.grpcclientdemo;


import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.dementev.grpcserverdemo.ClientOuterClass;
import ru.dementev.grpcserverdemo.ClientServiceGrpc;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class GrpcClientDemoApplication implements CommandLineRunner {

    private final ClientServiceGrpc.ClientServiceBlockingStub clientServiceBlockingStub;
    private final ClientServiceGrpc.ClientServiceStub clientServiceStub;

    public static void main(String[] args) {
        SpringApplication.run(GrpcClientDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var count = 0;
        List<Long> ids = new ArrayList<>();
        while (count++ < 100) {
            try {
                Empty empty = clientServiceBlockingStub.save(ClientOuterClass.Client.newBuilder()
                        .setPhoneNumber("+791636422" + count)
                        .setClientId(count)
                        .setFirstName("Test")
                        .setLastName("LastName")
                        .build());
                ClientOuterClass.Client client = clientServiceBlockingStub.get(ClientOuterClass.ClientId.newBuilder().setClientId(count).build());
                ids.add(client.getClientId());
//
                log.info(client.getFirstName() + " " + client.getLastName() + " " + client.getPhoneNumber() + " " + client.getClientId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        while (true) {
            observer(ids);
            Thread.sleep(5000);
        }

    }

    public void observer(List<Long> ids) {
        StreamObserver<ClientOuterClass.Client> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ClientOuterClass.Client client) {
                log.info("Пришел ответ через обсервер "+ client.getFirstName() + " " + client.getLastName() + " " + client.getPhoneNumber() + " " + client.getClientId());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Ошибка обработки"+ t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                log.info("done");
            }
        };
        StreamObserver<ClientOuterClass.ClientId> requestObserver = clientServiceStub.observe(responseObserver);
        try {
            ids.forEach(id ->
                requestObserver.onNext(ClientOuterClass.ClientId.newBuilder().setClientId(id).build()));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            requestObserver.onError(e);
        }
        requestObserver.onCompleted();


    }

}
