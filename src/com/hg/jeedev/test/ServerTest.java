package com.hg.jeedev.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hg.jeedev.common.rpc.ServiceProxyFactory;
import com.hg.jeedev.modules.zhdd.jcbk.service.Person;
import com.hg.jeedev.modules.zhdd.jcbk.service.PersonService;

/**
 * Servlet implementation class ServerTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@WebServlet("/ServerTest")
public class ServerTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ServiceProxyFactory serviceProxyFactory;
    /**
     * Default constructor. 
     */
    public ServerTest() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
