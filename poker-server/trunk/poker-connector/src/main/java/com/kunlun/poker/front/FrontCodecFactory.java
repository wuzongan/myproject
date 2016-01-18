package com.kunlun.poker.front;

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
public class FrontCodecFactory implements ProtocolCodecFactory {
	private FrontCodecEncoder encoder = new FrontCodecEncoder();
	private FrontCodecDecoder decoder = new FrontCodecDecoder();

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
                return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
                return decoder;
	}

	class FrontCodecDecoder extends CumulativeProtocolDecoder {

		@Override
		protected boolean doDecode(IoSession session, IoBuffer in,
				ProtocolDecoderOutput out) throws Exception {
			if (in.prefixedDataAvailable(2)) {
				int length = in.getUnsignedShort();
				int serverId = in.get();
				byte[] data = new byte[length - 1];
				in.get(data);

				IntermediateMessage message = new IntermediateMessage();
				message.setServerId(serverId);
				message.setBody(data);
				
				Integer sessionId = (Integer)session.getAttribute("sessionId");
				if(sessionId != null)
				{
					message.setSessionIds(new int[]{sessionId});
				}
				out.write(message);

				return true;
			}

			return false;
		}
	}

	class FrontCodecEncoder extends ProtocolEncoderAdapter {

		public void encode(IoSession session, Object message,
				ProtocolEncoderOutput out) throws Exception {
			IntermediateMessage tMessage = (IntermediateMessage) message;

			byte[] body = tMessage.getBody();
			int length = body.length + 1;
			IoBuffer buffer = IoBuffer.allocate(length + 2);
			buffer.putUnsignedShort(length);
			buffer.put((byte) tMessage.getServerId());
			buffer.put(body);
			buffer.flip();

			out.write(buffer);
		}
	}
}
