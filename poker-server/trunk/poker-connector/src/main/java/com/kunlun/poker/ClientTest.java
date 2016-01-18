package com.kunlun.poker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.msgpack.MessagePack;

import com.kunlun.poker.front.FrontCodecFactory;
import com.kunlun.poker.message.IntermediateMessage;

public class ClientTest {

	public static void main(String[] args) throws InterruptedException {
		NioSocketConnector connector = new NioSocketConnector();
		// Configure the service.
		connector.setConnectTimeoutMillis(10000);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new FrontCodecFactory()));
		connector.setHandler(new IoHandlerAdapter() {
			public void sessionOpened(IoSession session) throws Exception {
				System.out.println("session 打开");

				IntermediateMessage im = new IntermediateMessage();
				im.setServerId(1);

				try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(bytesOut)) {
					out.writeShort(201);
					
//					Map<String, Integer> map = new HashMap<>();
//					map.put("age", 1);
//					map.put("sex", 2);
//					
					out.write(new MessagePack().write(2));
//
					im.setBody(bytesOut.toByteArray());
					session.write(im);
				}
			}

			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				super.messageReceived(session, message);
				IntermediateMessage im = (IntermediateMessage) message;

				try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(im
						.getBody());
						DataInputStream in = new DataInputStream(bytesIn)) {

					int opcode = in.readUnsignedShort();
					@SuppressWarnings("rawtypes")
					Map map = (Map)new MessagePack().read(in);

					System.out.println("收到消息: opcode=" + opcode + "; msg="
							+ map + "; serverId=" + im.getServerId());
				}
			}

			public void sessionClosed(IoSession session) throws Exception {
				System.out.println("session 关闭");
			}
		});
		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(
						12345));
				future.awaitUninterruptibly();
				break;
			} catch (RuntimeIoException e) {
				System.err.println("Failed to connect.");
				e.printStackTrace();
				Thread.sleep(5000);
			}
		}
	}
}
