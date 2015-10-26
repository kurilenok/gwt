package org.numisoft.gwt.gwtproject.client;

import org.numisoft.gwt.gwtproject.shared.Customer;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	/*
	 * @typeArgs <org.numisoft.gwt.gwtproject.shared.Customer>
	 */

	Customer[] greetServer(String name) throws IllegalArgumentException;
}
