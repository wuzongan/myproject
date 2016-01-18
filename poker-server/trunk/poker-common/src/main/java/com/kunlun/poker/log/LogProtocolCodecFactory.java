package com.kunlun.poker.log;

import java.io.IOException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogProtocolCodecFactory implements ProtocolCodecFactory {
    
    private static final Logger logger = LoggerFactory
            .getLogger(LogProtocolCodecFactory.class);
    private final MessagePack messagePack = new MessagePack();
    private LogEncoder encoder;
    private LogDecoder decoder;
    
    public LogProtocolCodecFactory() {
        this.encoder = new LogEncoder();
        this.decoder = new LogDecoder();
    }
    
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return this.encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return this.decoder;
    }

    class LogEncoder extends ProtocolEncoderAdapter{
        @Override
        public void encode(IoSession session, Object message,
                ProtocolEncoderOutput out)  {
            if(message == null){
                return;
            }
            if(message instanceof LogMessage){
                LogMessage logMessage = (LogMessage) message;
                Object value = logMessage.getValue();
                if(value==null){
                    return;
                }
                byte[] bytes;
                try {
                    bytes = messagePack.write(value);
                    if(bytes == null || bytes.length == 0){
                        return;
                    }
                    int messageLength = bytes.length;
                    logger.debug("value====================="+value+",bytes.len================="+messageLength);
                    IoBuffer buffer = IoBuffer.allocate(messageLength+4);
                    buffer.setAutoExpand(true);
                    buffer.putInt(messageLength);
                    buffer.put(bytes);
                    buffer.flip();
                    buffer.free();
                    out.write(buffer);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
                
             }
        }
}    
    class LogDecoder extends CumulativeProtocolDecoder{
        @Override
        protected boolean doDecode(IoSession session, IoBuffer in,
                ProtocolDecoderOutput out) throws Exception {
            if(!in.prefixedDataAvailable(4)){
                return false;
            }
            int len = in.getInt();
            byte[] buffer = new byte[len];
            in.get(buffer);
            Object value =  messagePack.read(buffer, Templates.tMap(Templates.TString, Templates.TString));
            LogMessage msg = new LogMessage();
            msg.setValue(value);
            out.write(msg);
            return true;
        }
    }

}
