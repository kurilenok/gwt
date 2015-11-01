package org.numisoft.gwt.gwtproject.server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

	String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres";

	/**
	 * This method gets returns list of customers upon request from client side
	 * 
	 * */

	@Override
	public List<Customer> greetServer(CustomerRequest request) throws IllegalArgumentException {

		List<Customer> customers = new ArrayList<Customer>();
		Metaphone metaphone = new Metaphone();

		try {
			Connection connection = DriverManager.getConnection(url);

			StringBuilder select = new StringBuilder();
			select.append("SELECT * FROM customers INNER JOIN customer_types ");
			select.append("ON customers.customer_type_id = customer_types.customer_type_id ");
			select.append("WHERE first_name_metaphone LIKE '%");
			select.append(metaphone.encode(request.getFirstName()));
			select.append("%' AND last_name_metaphone LIKE '%");
			select.append(metaphone.encode(request.getLastName()));
			select.append("%' ORDER BY modified_when DESC");

			if (request.getFirstName().equalsIgnoreCase("")
					&& request.getLastName().equalsIgnoreCase("")) {
				select.append(" LIMIT 10;");
			} else {
				select.append(";");
			}

			PreparedStatement statement = connection.prepareStatement(select.toString());

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				Customer customer = new Customer();
				customer.setCustomerId(result.getInt("customer_id"));
				customer.setTitle(result.getString("title"));
				customer.setFirstName(result.getString("first_name"));
				customer.setLastName(result.getString("last_name"));
				customer.setCustomerType(result.getString("customer_type_caption"));
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

	/**
	 * This method modifies customer data in DB upon request from client side
	 * */

	@Override
	public String modifyCustomer(Customer customer) throws IllegalArgumentException {

		String modifiedWhen = "";
		Metaphone metaphone = new Metaphone();

		try {
			Connection connection = DriverManager.getConnection(url);

			StringBuilder update = new StringBuilder();
			update.append("UPDATE customers SET first_name = '" + customer.getFirstName());
			update.append("', first_name_metaphone = '" + metaphone.encode(customer.getFirstName()));
			update.append("', last_name = '" + customer.getLastName());
			update.append("', last_name_metaphone = '" + metaphone.encode(customer.getLastName()));
			update.append("', title ='" + customer.getTitle());
			update.append("', customer_type_id ='" + customer.getCustomerTypeId());
			update.append("', modified_when = DEFAULT ");
			update.append("WHERE customer_id = " + customer.getCustomerId() + ";");

			PreparedStatement update_statement = connection.prepareStatement(update.toString());
			update_statement.execute();

			/* Get new timestamp for updated customer */
			StringBuilder select = new StringBuilder();
			select.append("SELECT * FROM customers ");
			select.append("WHERE customer_id = " + customer.getCustomerId() + ";");

			PreparedStatement select_statement = connection.prepareStatement(select.toString());
			ResultSet result = select_statement.executeQuery();

			while (result.next()) {
				modifiedWhen = result.getString("modified_when");
			}

			update_statement.close();
			select_statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return modifiedWhen;
	}

	/**
	 * This method checks if DB table is available. If DB table is not
	 * available, it is created via xml parsing.
	 * */

	@Override
	public boolean checkTables(String tableName) throws IllegalArgumentException {

		try {
			Connection connection = DriverManager.getConnection(url);
			DatabaseMetaData dbm = connection.getMetaData();
			// check if "customers" table is there
			ResultSet tables = dbm.getTables(null, null, tableName, null);

			if (tables.next()) {
				return true;
			} else {
				DBCreator.createTableCustomerTypes();
				DBCreator.populateTableCustomerTypes();
				DBCreator.createTableCustomers();
				DBCreator.populateTableCustomers();
				return true;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	@Override
	public void deleteCustomer(Customer customer) throws IllegalArgumentException {

		try {
			Connection connection = DriverManager.getConnection(url);

			StringBuilder delete = new StringBuilder();
			delete.append("DELETE FROM customers WHERE customer_id = " + customer.getCustomerId()
					+ ";");

			PreparedStatement update_statement = connection.prepareStatement(delete.toString());
			update_statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
