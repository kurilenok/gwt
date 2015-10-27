package org.numisoft.gwt.gwtproject.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	public List<Customer> greetServer(String input)
			throws IllegalArgumentException {

		List<Customer> customers = new ArrayList();

		try {
			String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres";
			Connection connection = DriverManager.getConnection(url);

			String select = "select * from customers";
			PreparedStatement statement = connection.prepareStatement(select);

			ResultSet result = statement.executeQuery();

			while (result.next()) {

				Customer customer = new Customer(
						result.getString("first_name"),
						result.getString("last_name"));
				customers.add(customer);

			}

			statement.close();
			connection.close();

		} catch (SQLException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Customer tom = new Customer();
		// tom.setFirstName("tom");
		// tom.setLastName("brady");
		// customers.add(tom);
		//
		// Customer john = new Customer();
		// john.setFirstName("john");
		// john.setLastName("doe");
		// customers.add(john);
		//
		// Customer vasja = new Customer();
		// vasja.setFirstName("vasja");
		// vasja.setLastName("pupkin");
		// customers.add(vasja);
		//
		// Customer eduard = new Customer();
		// eduard.setFirstName("eduard");
		// eduard.setLastName("leikind");
		// customers.add(eduard);

		return customers;
	}

}
