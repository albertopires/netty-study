package io.github.albertopires.study;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;

		try {
			while (in.isReadable()) {
				System.out.print((char) in.readByte());
				System.out.flush();
			}
		} finally {
			// ((ByteBuf) msg).release();
			System.err.println("Releasing msg");
			Date d = new Date();
			String str = d.toString();
			ByteBuf response = ctx.alloc().buffer(str.length());
			response.writeBytes(str.getBytes());
			ctx.writeAndFlush(response);
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
	
}
