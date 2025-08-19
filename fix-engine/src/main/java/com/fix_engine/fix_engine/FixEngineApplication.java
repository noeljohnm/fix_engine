package com.fix_engine.fix_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fix_engine.fix_engine.client.FixClient;
import com.fix_engine.fix_engine.oms.OmsApplication;

import quickfix.Acceptor;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;

@SpringBootApplication
public class FixEngineApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FixEngineApplication.class, args);

        // Start OMS acceptor
        OmsApplication omsApp = new OmsApplication();
        SessionSettings omsSettings = new SessionSettings("src/main/resources/oms.cfg");
        MessageStoreFactory storeFactory = new FileStoreFactory(omsSettings);
        LogFactory logFactory = new FileLogFactory(omsSettings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        Acceptor acceptor = new SocketAcceptor(omsApp, storeFactory, omsSettings, logFactory, messageFactory);
        acceptor.start();

        // Start client initiator
        FixClient client = new FixClient();
        SessionSettings clientSettings = new SessionSettings("src/main/resources/client.cfg");
        MessageStoreFactory clientStore = new FileStoreFactory(clientSettings);
        LogFactory clientLog = new FileLogFactory(clientSettings);
        MessageFactory clientMessageFactory = new DefaultMessageFactory();
        Initiator initiator = new SocketInitiator(client, clientStore, clientSettings, clientLog, clientMessageFactory);
        initiator.start();
    }
}