package com.hg.jeedev.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hg.jeedev.common.rpc.ServiceProxyFactory;
import com.hg.jeedev.modules.zhdd.jcbk.service.Person;
import com.hg.jeedev.modules.zhdd.jcbk.service.PersonService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ClientTest {
	
	@Autowired
	ServiceProxyFactory serviceProxyFactory;
	
	@Test
	public void clientTest() {
		try {
			PersonService service = serviceProxyFactory.getProxy(PersonService.class,"/server");
			
			System.out.println(service.getInfo());
			
			Person person = new Person();
			person.setAge(23);
			person.setName("Qjm");
			person.setSex("男");
			System.out.println(service.printInfo(person));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
