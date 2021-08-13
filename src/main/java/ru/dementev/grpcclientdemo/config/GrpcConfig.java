package ru.dementev.grpcclientdemo.config;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dementev.grpcserverdemo.ClientServiceGrpc;

@Configuration
public class GrpcConfig {
    @Bean(destroyMethod = "shutdown")
    public ManagedChannel managedChannel() {
        return NettyChannelBuilder.forAddress("localhost", 8091).usePlaintext().build();
    }

    @Bean
    public ClientServiceGrpc.ClientServiceBlockingStub clientServiceBlockingStub(ManagedChannel managedChannel) {
        return ClientServiceGrpc.newBlockingStub(managedChannel);
    }

    @Bean
    public ClientServiceGrpc.ClientServiceStub clientServiceStub(ManagedChannel managedChannel) {
        return ClientServiceGrpc.newStub(managedChannel);

    }

}
