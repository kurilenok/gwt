package org.numisoft.gwt.gwtproject.client;

import java.util.List;

import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.CustomerRequest;
import org.numisoft.gwt.gwtproject.shared.Verifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Main implements EntryPoint {

	private CustomerRequest request;
	static List<Customer> customers;
	static Label console;
	final static int PAGE_MODULE = 10;
	int pages;
	static int currentPage;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This method is application entry point.
	 * */
	@Override
	public void onModuleLoad() {

		initializeUI();

		checkTables();

		/* Initial search = 10 most recently modified customers */
		getResult(new CustomerRequest("", ""));

	}

	/**
	 * This method initializes UI. Search text boxes and buttons are added to
	 * root panel.
	 * */
	private void initializeUI() {
		console = new Label();
		RootPanel.get("console").add(console);

		final TextBox tbSearchFirstName = new TextBox();
		RootPanel.get("tbFirstName").add(tbSearchFirstName);

		final TextBox tbSearchLastName = new TextBox();
		RootPanel.get("tbLastName").add(tbSearchLastName);

		/* Button 'SEARCH' start */
		Button bSearch = new Button("Search");
		bSearch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (!Verifier.isEnglish(tbSearchFirstName.getValue() + tbSearchLastName.getValue())) {
					console.setText("Wrong input, English please!");
					return;
				}
				request = new CustomerRequest(tbSearchFirstName.getValue(), tbSearchLastName
						.getValue());
				console.setText(request.getFirstName() + " " + request.getLastName());
				getResult(request);
			}
		});
		RootPanel.get("bSearch").add(bSearch);
		/* Button 'SEARCH' finish */

		/* Button 'CLEAR' start */
		Button bClear = new Button("Clear");
		bClear.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tbSearchFirstName.setText("");
				tbSearchLastName.setText("");
			}
		});
		RootPanel.get("bClear").add(bClear);
		/* Button 'CLEAR' finish */

	}

	/**
	 * This method checks if DB tables exist. If DB tables are not available -
	 * DB tables are created and initialized.
	 * */
	private void checkTables() {
		greetingService.checkTables("customers", new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				console.setText("connected to DB");
			}

			@Override
			public void onFailure(Throwable caught) {
				console.setText(caught.getMessage());
			}
		});
	}

	/**
	 * This method sends request to server and returns list of customers.
	 * */
	private void getResult(CustomerRequest request) {
		greetingService.searchCustomers(request, new AsyncCallback<List<Customer>>() {
			@Override
			public void onFailure(Throwable caught) {
				console.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(List<Customer> result) {
				customers = result;
				/* Calculate number of pages to show */
				if (customers.size() % PAGE_MODULE == 0) {
					pages = customers.size() / PAGE_MODULE;
				} else {
					pages = 1 + customers.size() / PAGE_MODULE;
				}
				console.setText(Integer.toString(customers.size()) + " customers detected ("
						+ Integer.toString(pages) + " pages)");

				/* Show first page */
				currentPage = 1;
				RootPanel.get("tableDiv").clear();
				RootPanel.get("tableDiv").add(new ResultTable(currentPage));
				RootPanel.get("pageBar").clear();
				RootPanel.get("pageBar").add(new PageBar(pages));
			}
		});
	}

}