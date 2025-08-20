package com.fix_engine.fix_engine.oms;

import org.springframework.stereotype.Component;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.AvgPx;
import quickfix.field.CumQty;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.LastPx;
import quickfix.field.LastShares;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

@Component
public class OmsApplication extends MessageCracker implements Application {

    @Override public void onCreate(SessionID sessionId) {}
    @Override public void onLogon(SessionID sessionId) {
    	System.out.println(">> OMS logged on: " + sessionId);
    }
    @Override public void onLogout(SessionID sessionId) {}
    @Override public void toAdmin(Message message, SessionID sessionId) {}
    @Override public void toApp(Message message, SessionID sessionId) throws DoNotSend {}
    @Override public void fromAdmin(Message message, SessionID sessionId) {}
    
    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        crack(message, sessionId);
    }

    @Handler
    public void onMessage(NewOrderSingle order, SessionID sessionId) throws FieldNotFound, SessionNotFound {

    	System.out.println("OMS received order: " + order);
        ExecutionReport execReport = new ExecutionReport(new OrderID("123"), new ExecID("456"), new ExecType(ExecType.FILL), new OrdStatus(OrdStatus.FILLED), order.getSide(), new LeavesQty(0), new CumQty(0), new AvgPx(0));
        Session.sendToTarget(execReport, sessionId);
    }
}
