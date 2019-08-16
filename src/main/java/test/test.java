package test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import codec.LenPreMsgEncoder;
import nettyclient.client;
import nettyserver.NettyServer;
import protocol.Header;
import protocol.LenPreMsg;
import protocol.RegDiscover;
import protocol.RegService;
import protocol.RpcRequest;
import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
	
	@Autowired
	client cli;
	
	@Test
	public void test1(){
		RegDiscover regDiscover = new RegDiscover("httpsdsfsafsaerver", "addsdfsafsr");
		RpcRequest rpcRequest = new RpcRequest("734209479jdfjsfdjsfip794079014",
				"adfjashfpaoh73497498d80fh32", 
				"asjdfaiohpgh", 
				"djas;fja938u2893", 
				"0f8y0409y3", 
				"7398ujfhhhe9hf93h2f9h");
		RegService regService = new RegService("customerfdsafsadf", "xxxdsfadsfasdf");
		List<LenPreMsg> msgs = asList(new LenPreMsg(Header.reg_discover, 1, regDiscover),
				new LenPreMsg(Header.rpc_request, 1, rpcRequest),
				new LenPreMsg(Header.reg_addservice, 1, regService)); 
//		List<Integer> protocols = asList(Header.reg_discover,Header.rpc_request,Header.reg_addservice);
		for(int i=0;i<4;++i){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(int i=0;i<10;++i){
						try {
							cli.sendtoserver(msgs.get(i%3));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}).start();
		}
		
		
		try {
			Thread.sleep(180000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
