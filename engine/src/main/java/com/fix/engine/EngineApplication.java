package com.fix.engine;

import com.fix.engine.client.FixClient;
import com.fix.engine.oms.OmsApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import quickfix.*;

@SpringBootApplication
public class EngineApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(EngineApplication.class, args);

        OmsApplication omsApp = context.getBean(OmsApplication.class);
        FixClient client = context.getBean(FixClient.class);

        // OMS Acceptor
        SessionSettings omsSettings = new SessionSettings("src/main/resources/oms.cfg");
        MessageStoreFactory storeFactory = new FileStoreFactory(omsSettings);
        LogFactory logFactory = new FileLogFactory(omsSettings);
        MessageFactory messageFactory = new quickfix.fix44.MessageFactory();
        SocketAcceptor acceptor = new SocketAcceptor(omsApp, storeFactory, omsSettings, logFactory, messageFactory);
        acceptor.start();

        // Client Initiator
        SessionSettings clientSettings = new SessionSettings("src/main/resources/client.cfg");
        SocketInitiator initiator = new SocketInitiator(client, storeFactory, clientSettings, logFactory, messageFactory);
        initiator.start();
    }
}