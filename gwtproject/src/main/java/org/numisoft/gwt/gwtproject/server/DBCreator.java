package org.numisoft.gwt.gwtproject.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.language.Metaphone;
import org.numisoft.gwt.gwtproject.shared.Customer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class DBCreator {

	private final static String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres";

	public static void createTableCustomerTypes() {

		try {
			Connection connection = DriverManager.getConnection(url);
			StringBuilder create = new StringBuilder();
			create.append("CREATE TABLE customer_types (");
			create.append("customer_type_id serial NOT NULL, ");
			create.append("customer_type_caption character varying(50), ");
			create.append("CONSTRAINT customer_types_pkey PRIMARY KEY (customer_type_id));");

			PreparedStatement update_statement = connection.prepareStatement(create.toString());
			update_statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void populateTableCustomerTypes() {
		addCustomerType("Residential");
		addCustomerType("Small/Medium Business");
		addCustomerType("Enterprise");
	}

	public static void createTableCustomers() {

		try {
			Connection connection = DriverManager.getConnection(url);
			StringBuilder create = new StringBuilder();
			create.append("CREATE TABLE customers (");
			create.append("customer_id serial NOT NULL, ");
			create.append("title character varying(5), ");
			create.append("first_name character varying(50), ");
			create.append("first_name_metaphone character varying(50), ");
			create.append("last_name character varying(50), ");
			create.append("last_name_metaphone character varying(50), ");
			create.append("modified_when timestamp without time zone DEFAULT now(), ");
			create.append("customer_type_id integer, ");
			// create.append("CONSTRAINT customers_pkey PRIMARY KEY (customer_id));");

			create.append("CONSTRAINT customers_pkey PRIMARY KEY (customer_id), ");
			create.append("CONSTRAINT customers_customer_type_id_fkey FOREIGN KEY (customer_type_id) ");
			create.append("REFERENCES customer_types (customer_type_id) MATCH SIMPLE ");
			create.append("ON UPDATE NO ACTION ON DELETE NO ACTION);");

			PreparedStatement update_statement = connection.prepareStatement(create.toString());
			update_statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void populateTableCustomers() {

		List<Customer> customers = new ArrayList<Customer>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		File f = new File("/home/kukolka/NetCracker/data.xml");

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(f);
			NodeList nodeList = document.getDocumentElement().getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node instanceof Element) {
					Customer customer = new Customer();
					customer.setTitle(node.getAttributes().getNamedItem("title").getNodeValue());
					customer.setFirstName(node.getAttributes().getNamedItem("first_name")
							.getNodeValue());
					customer.setLastName(node.getAttributes().getNamedItem("last_name")
							.getNodeValue());
					customer.setCustomerTypeId(Integer.parseInt(node.getAttributes()
							.getNamedItem("customer_type_id").getNodeValue()));
					customers.add(customer);
					addCustomer(customer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Customer customer : customers) {

			System.out.println(customer.getTitle() + " " + customer.getFirstName() + " "
					+ customer.getLastName() + " " + customer.getCustomerTypeId());

		}

	}

	public static void addCustomer(Customer customer) {

		Metaphone metaphone = new Metaphone();

		try {
			Connection connection = DriverManager.getConnection(url);
			StringBuilder add = new StringBuilder();
			add.append("INSERT INTO customers (title, first_name, first_name_metaphone, ");
			add.append("last_name, last_name_metaphone, customer_type_id) ");
			add.append("VALUES ('" + customer.getTitle() + "' ,'");
			add.append(customer.getFirstName() + "', '");
			add.append(metaphone.encode(customer.getFirstName()) + "', '");
			add.append(customer.getLastName() + "', '");
			add.append(metaphone.encode(customer.getLastName()) + "', '");
			add.append(customer.getCustomerTypeId() + "');");

			PreparedStatement update_statement = connection.prepareStatement(add.toString());
			update_statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void addCustomerType(String customerType) {

		try {
			Connection connection = DriverManager.getConnection(url);
			StringBuilder add = new StringBuilder();
			add.append("INSERT INTO customer_types (customer_type_caption) ");
			add.append("VALUES ('" + customerType + "');");

			PreparedStatement update_statement = connection.prepareStatement(add.toString());
			update_statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
