import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.msgpack.MessagePack;

public class Message {
	public static final MessagePack messagePack = new MessagePack();
	private int serverId;
	private int opcode;
	private Object body;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public byte[] toByteArray() {
		byte[] bytes = null;
		try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bytesOut)) {
			out.write(1);
			out.writeShort(opcode);
			messagePack.write(out, body);
			bytes = bytesOut.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bytes != null) {
			try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(bytesOut)) {
				out.writeShort(bytes.length);
				out.write(bytes);
				bytes = bytesOut.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bytes;
	}
	
	public void fromByteArray(byte[] bytes)
	{
		
	}
}
