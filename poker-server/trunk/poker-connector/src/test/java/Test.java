import java.io.IOException;

public class Test {
	public static void main(String[] args) throws IOException{
//		for(int i = 0; i < 1000; i ++)
//		{
//			IoConnector connector = new NioSocketConnector();
//			DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
//			filterChain.addLast("codec",
//					new ProtocolCodecFilter(new ProtocolCodecFactory() {
//						private ProtocolEncoder
//						
//						@Override
//						public ProtocolEncoder getEncoder(IoSession session) throws Exception {
//							// TODO Auto-generated method stub
//							return null;
//						}
//						
//						@Override
//						public ProtocolDecoder getDecoder(IoSession session) throws Exception {
//							// TODO Auto-generated method stub
//							return null;
//						}
//					});
//
//			LoggingFilter lf = new LoggingFilter();
//			lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
//			lf.setMessageSentLogLevel(LogLevel.DEBUG);
//			filterChain.addLast("logger", lf);
//
//			final int itg = i;
//			connector.setHandler(new IoHandlerAdapter(){
//				@Override
//				public void sessionCreated(IoSession session) throws Exception {
//					Message message = new Message();
//					message.setServerId(102);
//					message.setOpcode(415);
//					message.setBody(itg);
//					session.write(message.toByteArray());
//				}
//			});
//		}
	}
}
