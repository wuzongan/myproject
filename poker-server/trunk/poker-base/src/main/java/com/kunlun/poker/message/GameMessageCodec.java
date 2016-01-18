package com.kunlun.poker.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;
import org.msgpack.type.IntegerValue;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;

import com.googlecode.canoe.core.exception.ServerException;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.support.AbstractMessageCodec;
import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.kunlun.poker.server.GameSession;

public class GameMessageCodec extends AbstractMessageCodec {
	private SessionRoleManager sessionRoleManager;
	private MessagePack messagePack = new MessagePack();

	public void setSessionRoleManager(SessionRoleManager sessionRoleManager) {
		this.sessionRoleManager = sessionRoleManager;
	}

	@Override
	protected Request doDecodeRequest(Object message) {
		IntermediateMessage im = (IntermediateMessage) message;
		try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(
				im.getBody());
				DataInputStream in = new DataInputStream(bytesIn)) {
			Request request = new Request();
			int opcode = in.readUnsignedShort();
			request.setOpcode(opcode);
			int available = in.available();
			if (available != 0) {
				Object data = getPrimitive(messagePack.read(in));

				request.setData(data);
			}
			return request;
		} catch (IOException e) {
			throw new ServerException(e);
		}
	}

	private static Object getPrimitive(Value value) {
		if (value.isBooleanValue())
			return value.asBooleanValue().getBoolean();
		if (value.isFloatValue())
			return value.asFloatValue().getFloat();
		if (value.isIntegerValue()) {
			IntegerValue intValue = value.asIntegerValue();
			try {
				return intValue.getInt();
			} catch (MessageTypeException e) {
				return intValue.getLong();
			}
		}
		if (value.isNilValue())
			return null;
		if (value.isMapValue())
			return getMap(value);
		if (value.isArrayValue())
			return getArray(value);

		return value.asRawValue().getString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map getMap(Value value) {
		MapValue mapValue = value.asMapValue();
		Map map = new HashMap();

		Value[] keyValues = mapValue.getKeyValueArray();

		for (int i = 0, length = keyValues.length / 2; i < length; i++) {
			map.put(getPrimitive(keyValues[i * 2]),
					getPrimitive(keyValues[i * 2 + 1]));
		}

		return map;
	}

	private static Object[] getArray(Value value) {
		Object[] values = value.asArrayValue().toArray();
		Object[] array = new Object[values.length];

		for (int i = 0, length = values.length; i < length; i++) {
			if (values[i] instanceof Value) {
				array[i] = getPrimitive((Value) values[i]);
			} else {
				array[i] = values[i];
			}
		}

		return array;
	}

	@Override
	protected Object doEncodeResponse(Response response) throws ServerException {
		try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bytesOut)) {
			out.writeShort(response.getOpcode());
			messagePack.write(out, response.getData());

			IntermediateMessage message = new IntermediateMessage();

			int[] sessionIds = null;
			ResponseScope scope = response.getScope();
			if (scope == ResponseScope.SELF) {
				sessionIds = new int[] { (int) ((GameSession) response
						.getSession()).getId() };
			} else if (scope == ResponseScope.SPECIFIED) {
				Collection<? extends Role> recievers = response.getRecievers();
				int numberOfSessions = recievers.size();

				sessionIds = new int[numberOfSessions];

				int i = 0;
				for (Role reciever : recievers) {
					GameSession session = ((GameSession) sessionRoleManager
							.getSession(reciever));
					if (session != null)
						sessionIds[i] = (int) session.getId();
					i++;
				}
			}

			message.setSessionIds(sessionIds);
			message.setBody(bytesOut.toByteArray());
			return message;
		} catch (IOException e) {
			throw new ServerException(e);
		}
	}

}
