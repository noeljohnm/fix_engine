package com.fix.engine.client;

import org.springframework.stereotype.Component;

import com.fix.engine.model.Order;

import quickfix.Application;
import quickfix.Message;
import quickfix.MessageCracker;
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
public class FixClient extends MessageCracker implements Application {

    private SessionID sessionID;

    @Override
    public void onLogon(SessionID sessionId) { this.sessionID = sessionId; }

    @Override public void onLogout(SessionID sessionId) {}
    @Override public void toAdmin(Message message, SessionID sessionId) {}
    @Override public void fromAdmin(Message message, SessionID sessionId) {}
    @Override public void toApp(Message message, SessionID sessionId) {}
    @Override
    public void fromApp(Message message, SessionID sessionId) { System.out.println("Received: " + message); }

    public void sendOrder(Order order) throws SessionNotFound {
        if (sessionID == null) { System.out.println("Session not ready"); return; }

        NewOrderSingle newOrder = new NewOrderSingle(
            new ClOrdID(String.valueOf(order.getId())),
            new Side(order.getSide().equalsIgnoreCase("BUY") ? Side.BUY : Side.SELL),
            new TransactTime(),
            new OrdType(order.getType().equalsIgnoreCase("LIMIT") ? OrdType.LIMIT : OrdType.MARKET)
        );
        newOrder.set(new Symbol(order.getSymbol()));
        newOrder.set(new OrderQty(order.getQuantity()));
        newOrder.set(new Price(order.getPrice()));

        Session.sendToTarget(newOrder, sessionID);
        System.out.println("Sent order: " + order.getId());
    }

	@Override
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}
}