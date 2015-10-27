package org.numisoft.gwt.gwtproject.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.language.Metaphone;
import org.numisoft.gwt.gwtproject.client.GreetingService;
import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.CustomerRequest;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	@Override
	public List<Customer> greetServer(CustomerRequest request) throws IllegalArgumentException {

		List<Customer> customers = new ArrayList<Customer>();
		Metaphone metaphone = new Metaphone();

		try {
			String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres";
			Connection connection = DriverManager.getConnection(url);

			StringBuilder select = new StringBuilder();
			select.append("SELECT * FROM customers WHERE first_name_metaphone LIKE '%");
			select.append(metaphone.encode(request.getFirstName()));
			select.append("%' AND last_name_metaphone LIKE '%");
			select.append(metaphone.encode(request.getLastName()));
			select.append("%' ORDER BY modified_when DESC;");

			PreparedStatement statement = connection.prepareStatement(select.toString());

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Customer customer = new Customer();
				customer.setTitle(result.getString("title"));
				customer.setFirstName(result.getString("first_name"));
				customer.setLastName(result.getString("last_name"));
				customer.setCustomerType(result.getString("customer_type"));
				customer.setModifiedWhen(result.getString("modified_when").substring(0, 19));
				customers.add(customer);
			}

			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;
	}

}
