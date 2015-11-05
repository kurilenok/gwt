package org.numisoft.gwt.gwtproject.client.ui;

import java.util.List;

import org.numisoft.gwt.gwtproject.client.GreetingService;
import org.numisoft.gwt.gwtproject.client.GreetingServiceAsync;
import org.numisoft.gwt.gwtproject.client.Main;
import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.CustomerRequest;
import org.numisoft.gwt.gwtproject.shared.Verifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This class is a helper class to deal with UI
 * */
public abstract class UICreator {

	private final static GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/** This method builds login bar */
	public static void logIn() {

		Main.console.setText("please log in to user customer database");
		RootPanel.get("console").add(Main.console);

		final TextBox tbPort = new TextBox();
		final TextBox tbUser = new TextBox();
		final TextBox tbPass = new PasswordTextBox();
		tbPort.setStyleName("tbLogin");
		tbUser.setStyleName("tbLogin");
		tbPass.setStyleName("tbLogin");

		RootPanel.get("lPort").add(new Label("DB port: "));
		RootPanel.get("tbPort").add(tbPort);
		RootPanel.get("lUser").add(new Label(" User: "));
		RootPanel.get("tbUser").add(tbUser);
		RootPanel.get("lPass").add(new Label(" Pass: "));
		RootPanel.get("tbPass").add(tbPass);

		/* Button 'LOGIN' start */
		Button bLogin = new Button("Login");
		bLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Main.console.setText("connection fail!");
				greetingService.connectToDB(tbPort.getValue(), tbUser.getValue(),
						tbPass.getValue(), new AsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean isLoggedIn) {
								if (isLoggedIn) {
									Main.console.setText("connected to DB");
									/* Remove login bar */
									RootPanel.get("lPort").clear();
									RootPanel.get("tbPort").clear();
									RootPanel.get("lUser").clear();
									RootPanel.get("tbUser").clear();
									RootPanel.get("lPass").clear();
									RootPanel.get("tbPass").clear();
									RootPanel.get("bLogin").clear();

									initializeUI();
									checkTables();
									/*
									 * Initial search = 10 most recently
									 * modified customers
									 */
									getResult(new CustomerRequest("", ""));
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								Main.console.setText(caught.getMessage());
							}
						});
			}
		});
		RootPanel.get("bLogin").add(bLogin);
		/* Button 'LOGIN' finish */
	}

	/**
	 * This method builds search bar
	 * */
	static void initializeUI() {

		RootPanel.get("lFirstName").add(new Label(" First Name:"));
		RootPanel.get("lLastName").add(new Label(" Last Name:"));

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
					Main.console.setText("Wrong input, English please!");
					return;
				}
				CustomerRequest request = new CustomerRequest(tbSearchFirstName.getValue(),
						tbSearchLastName.getValue());
				Main.console.setText(request.getFirstName() + " " + request.getLastName());
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
	 * This method sends request to server and builds result table.
	 * */
	static void getResult(CustomerRequest request) {
		greetingService.searchCustomers(request, new AsyncCallback<List<Customer>>() {
			@Override
			public void onFailure(Throwable caught) {
				Main.console.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(List<Customer> result) {
				Main.customers = result;
				/* Calculate number of pages to show */
				if (Main.customers.size() % Main.PAGE_MODULE == 0) {
					Main.pages = Main.customers.size() / Main.PAGE_MODULE;
				} else {
					Main.pages = 1 + Main.customers.size() / Main.PAGE_MODULE;
				}
				Main.console.setText(Integer.toString(Main.customers.size())
						+ " customers detected (" + Integer.toString(Main.pages) + " pages)");

				/* Show first page */
				Main.currentPage = 1;
				RootPanel.get("tableDiv").clear();
				RootPanel.get("tableDiv").add(new ResultTable(Main.currentPage));
				RootPanel.get("pageBar").clear();
				RootPanel.get("pageBar").add(new PageBar(Main.pages));
			}
		});
	}

	/**
	 * This method checks if DB tables exist. If DB tables are not available -
	 * DB tables are created and initialized.
	 * */
	static void checkTables() {

		greetingService.checkTables(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Main.console.setText("connected to DB");
			}

			@Override
			public void onFailure(Throwable caught) {
				Main.console.setText(caught.getMessage());
			}
		});
	}

}
