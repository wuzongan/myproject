package com.kunlun.poker.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.kunlun.poker.message.IntermediateMessage;

/**
 *
 * @author panzd
 */
public class IntermediateCodecFactory implements ProtocolCodecFactory {

	private TransmitEncoder encoder = new TransmitEncoder();
	private TransmitDecoder decoder = new TransmitDecoder();

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	class TransmitDecoder extends CumulativeProtocolDecoder {
		@Override
		protected boolean doDecode(IoSession session, IoBuffer in,
				ProtocolDecoderOutput out) throws Exception {
			if (in.prefixedDataAvailable(2)) {
				int length = in.getUnsignedShort();

				IntermediateMessage message = new IntermediateMessage();

				short numberOfSessions = in.getUnsigned();
				int[] sessionIds = new int[numberOfSessions];
				for (int i = 0; i < numberOfSessions; i++) {
					sessionIds[i] = in.getInt();
				}
				message.setSessionIds(sessionIds);

				short sourceLength = in.getUnsigned();
				byte[] ip = new byte[sourceLength - Short.BYTES];
				in.get(ip);
				int port = in.getUnsignedShort();

				message.setSource(new InetSocketAddress(InetAddress.getByAddress(ip), port));

				byte[] data = new byte[length - sourceLength - numberOfSessions * Integer.BYTES - 2];
				in.get(data);

				message.setBody(data);
				out.write(message);

				return true;
			}

			return false;
		}
	}

	class TransmitEncoder extends ProtocolEncoderAdapter {

		public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        	IntermediateMessage tMessage = (IntermediateMessage)message;

        	int[] sessionIds = tMessage.getSessionIds(); 
        	byte[] body = tMessage.getBody();
        	int bodyLength = body.length;
        	
            InetSocketAddress address = (InetSocketAddress)session.getRemoteAddress();
        	byte[] ip = address.getAddress().getAddress();
        	int addressLength = ip.length + Short.BYTES + 1;
        	
        	int sessionLength = sessionIds.length * Integer.BYTES + 1;
        	
        	int length = sessionLength + addressLength + bodyLength; 
            IoBuffer buffer = IoBuffer.allocate(length + 2);

            buffer.putUnsignedShort(length);
            
            //put sessions
            buffer.putUnsigned(sessionIds.length);
            for(int sessionId : sessionIds)
            {
                buffer.putInt(sessionId);
            }
            
            // put address;
            buffer.putUnsigned(addressLength - 1);
            buffer.put(ip);
            buffer.putUnsignedShort(address.getPort());
            
            // put body;
            buffer.put(body);

            buffer.flip();

            out.write(buffer);
        }
	}
}
