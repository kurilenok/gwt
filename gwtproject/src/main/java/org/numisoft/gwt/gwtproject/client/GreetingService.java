package org.numisoft.gwt.gwtproject.client;

import java.util.List;

import org.numisoft.gwt.gwtproject.shared.Customer;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	/*
	 * @typeArgs <java.util.List<org.numisoft.gwt.gwtproject.shared.Customer>>
	 */

	List<Customer> greetServer(String name) throws IllegalArgumentException;
}
