package com.fix_engine.fix_engine.client;

import org.springframework.stereotype.Component;

import quickfix.Application;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.ClOrdID;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;

@Component
public class FixClient implements Application {

    private SessionID sessionID;

    @Override
    public void onLogon(SessionID sessionId) { 
    	this.sessionID = sessionId; 
    	System.out.println(">>>> FIX LOGON received. SessionID = " + sessionId);}
    @Override public void onLogout(SessionID sessionId) {}
    @Override public void toAdmin(Message message, SessionID sessionId) {}
    @Override public void toApp(Message message, SessionID sessionId) {}
    @Override public void fromAdmin(Message message, SessionID sessionId) {}
    
    @Override
    public void fromApp(Message message, SessionID sessionId) {
        System.out.println("Client received: " + message);
    }

    public void sendOrder() throws SessionNotFound {
        NewOrderSingle order = new NewOrderSingle(
                new ClOrdID("1"), new Side(Side.BUY),
                new TransactTime(), new OrdType(OrdType.LIMIT)
        );
        order.set(new Symbol("AAPL"));
        order.set(new OrderQty(100));
        order.set(new Price(150.0));
        System.out.println(">>>> sessionID = " + sessionID);
        Session.sendToTarget(order, sessionID);
        System.out.println("Client sent order.");
    }
	@Override
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}
}
