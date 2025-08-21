package com.fix.engine.oms;

import com.fix.engine.model.Order;
import com.fix.engine.service.OrderStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;

import java.util.Date;

@Component
public class OmsApplication extends MessageCracker implements Application {

    @Autowired private OrderStore store;

    @Override
    public void onLogon(SessionID sessionId) { System.out.println("OMS logged on: " + sessionId); }
    @Override public void onLogout(SessionID sessionId) {}
    @Override public void toAdmin(Message message, SessionID sessionId) {}
    @Override public void fromAdmin(Message message, SessionID sessionId) {}
    @Override public void toApp(Message message, SessionID sessionId) {}
    
    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        if (message instanceof quickfix.fix44.NewOrderSingle) {
            quickfix.fix44.NewOrderSingle orderMsg = (quickfix.fix44.NewOrderSingle) message;

            String symbol = orderMsg.getSymbol().getValue();
            int qty = (int) orderMsg.getOrderQty().getValue();
            double price = orderMsg.getPrice().getValue();
            String side = orderMsg.getSide().getValue() == Side.BUY ? "BUY" : "SELL";

            Order order = new Order();
            order.setSymbol(symbol); order.setQuantity(qty); order.setPrice(price);
            order.setSide(side); order.setType(orderMsg.getOrdType().getValue() == OrdType.LIMIT ? "LIMIT" : "MARKET");
            order.setStatus("NEW");
            store.addOrder(order);

            // Send execution report back
            quickfix.fix44.ExecutionReport exec = new quickfix.fix44.ExecutionReport(
                new OrderID(String.valueOf(order.getId())),
                new ExecID("1"),
                new ExecType(ExecType.NEW),
                new OrdStatus(OrdStatus.NEW),
                new Side(orderMsg.getSide().getValue()),
                new LeavesQty(qty),
                new CumQty(0), null
            );
            exec.set(new Symbol(symbol));
            exec.set(new LastPx(0));
            try {
				Session.sendToTarget(exec, sessionId);
			} catch (SessionNotFound e) {
				e.printStackTrace();
			}
            System.out.println("OMS received order: " + order.getId());
        }
    }
	@Override
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}
}