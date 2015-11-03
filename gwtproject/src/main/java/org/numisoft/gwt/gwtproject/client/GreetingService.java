package org.numisoft.gwt.gwtproject.client;

import java.util.List;

import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.CustomerRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	List<Customer> searchCustomers(CustomerRequest request) throws IllegalArgumentException;

	String modifyCustomer(Customer customer) throws IllegalArgumentException;

	void deleteCustomer(Customer customer) throws IllegalArgumentException;

	void checkTables(String tableName) throws IllegalArgumentException;
}
