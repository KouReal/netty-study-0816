package test2;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nettyserver.NettyServer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test2 {
	@Autowired
	NettyServer nettyServer;
}
