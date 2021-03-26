package inno.tech.grpcclientdemo.config;

import inno.tech.grpcserverdemo.ClientServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
    @Bean(destroyMethod = "shutdown")
    public ManagedChannel managedChannel(){
        return ManagedChannelBuilder.forAddress("localhost",8080).usePlaintext().build();
    }
    @Bean
    public ClientServiceGrpc.ClientServiceBlockingStub clientServiceBlockingStub(ManagedChannel managedChannel){
       return ClientServiceGrpc.newBlockingStub(managedChannel);
    }
}
