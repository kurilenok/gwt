package org.numisoft.gwt.gwtproject.server;

import org.numisoft.gwt.gwtproject.client.GreetingService;
import org.numisoft.gwt.gwtproject.shared.Customer;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	@Override
	public Customer[] greetServer(String input) throws IllegalArgumentException {

		Customer[] customers = new Customer[4];

		Customer tom = new Customer();
		tom.setFirstName("tom");
		tom.setLastName("brady");
		customers[0] = tom;

		Customer john = new Customer();
		john.setFirstName("john");
		john.setLastName("doe");
		customers[1] = john;

		Customer vasja = new Customer();
		vasja.setFirstName("vasja");
		vasja.setLastName("pupkin");
		customers[2] = vasja;

		Customer eduard = new Customer();
		eduard.setFirstName("eduard");
		eduard.setLastName("leikin");
		customers[3] = eduard;

		return customers;
	}

}
