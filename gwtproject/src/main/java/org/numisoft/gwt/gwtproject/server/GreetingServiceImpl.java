package org.numisoft.gwt.gwtproject.server;

import org.numisoft.gwt.gwtproject.client.Customer;
import org.numisoft.gwt.gwtproject.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	@Override
	public Customer greetServer(String input) throws IllegalArgumentException {

		Customer tom = new Customer();
		tom.setFirstName("tom");
		tom.setLastName("brady");

		return tom;
	}

}
