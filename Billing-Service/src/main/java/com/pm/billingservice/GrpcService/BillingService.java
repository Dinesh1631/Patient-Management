package com.pm.billingservice.GrpcService;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@GrpcService
public class BillingService extends BillingServiceGrpc.BillingServiceImplBase {
private final static Logger log = Logger.getLogger(BillingService.class.getName());
    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        log.info("Received Billing request {} "+ request.toString());
         //BusinessLogic
        String accountID = UUID.randomUUID().toString();
        BillingResponse responce = BillingResponse.newBuilder()
                .setAccountId(accountID)
                .setStatus("Active")
                .build();
        responseObserver.onNext(responce);
        responseObserver.onCompleted();
    }
}
