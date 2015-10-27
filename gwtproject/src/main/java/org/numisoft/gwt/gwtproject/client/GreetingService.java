package org.numisoft.gwt.gwtproject.client;

import java.util.List;

import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.CustomerRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	/*
	 * @typeArgs <java.util.List<org.numisoft.gwt.gwtproject.shared.Customer>>
	 * 
	 * @typeArgs <org.numisoft.gwt.gwtproject.shared.CustomerRequest>
	 */

	List<Customer> greetServer(CustomerRequest request)
			throws IllegalArgumentException;
}
