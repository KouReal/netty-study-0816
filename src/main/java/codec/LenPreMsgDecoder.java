package codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocol.LenPreMsg;
import protocol.ProtocolMap;

public class LenPreMsgDecoder extends ByteToMessageDecoder{
	private static Logger logger = LoggerFactory.getLogger(LenPreMsgDecoder.class);
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		logger.info("decode: readinx:{},readable:{},writeidx:{}",in.readerIndex(),in.readableBytes(),in.writerIndex());
		if(in.readableBytes()<LenPreMsg.BASE_PRE_LEN){
			logger.info("readable too little");
			return ;
		}
		int beginindex;
		int protocol_id;
		while(true){
			beginindex = in.readerIndex();
			in.markReaderIndex();
			logger.info("before readInt:,readerindex:{},readable:{}",in.readerIndex(),in.readableBytes());
			protocol_id = in.readInt();
			logger.info("protocolid:{}",protocol_id);
			if(ProtocolMap.checkprotocol(protocol_id)){
				break;
			}
			in.resetReaderIndex();
			in.readByte();
			if(in.readableBytes()<LenPreMsg.BASE_PRE_LEN){
				return;
			}
		}
		
		int len = in.readInt();
		logger.info("read len:{}",len);
		
		if(len==0){
			//heartbeat
			out.add(new LenPreMsg(protocol_id, 0, null));
			return ;
		}
		logger.info("after read length,readablebytes:{}",in.readableBytes());
		if(in.readableBytes()<len){
			in.readerIndex(beginindex);
			return ;
		}
		byte[] data = new byte[len];
		in.readBytes(data);
		Class<?> protocol_cls = ProtocolMap.getclass(protocol_id);
		logger.info("start serialize,cls:{}",protocol_cls);
		Object obj = (Object) SerializeUtil.deserializeWithProtostuff(data, protocol_cls);
		logger.info("decoder get obj:{}",obj);
		out.add(new LenPreMsg(protocol_id, len, obj));
		
		
	}
	

}
