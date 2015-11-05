package org.numisoft.gwt.gwtproject.client;

import java.util.List;

import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.CustomerRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	/**
	 * This method tries to connect to DB with port number, user name and
	 * password provided by user
	 */
	boolean connectToDB(String port, String user, String pass) throws IllegalArgumentException;

	/**
	 * This method checks if DB tables exist. If DB tables are not available -
	 * DB tables are created and initialized.
	 * */
	void checkTables() throws IllegalArgumentException;

	/***/
	List<Customer> searchCustomers(CustomerRequest request) throws IllegalArgumentException;

	/***/
	String modifyCustomer(Customer customer) throws IllegalArgumentException;

	/***/
	void deleteCustomer(Customer customer) throws IllegalArgumentException;

}
