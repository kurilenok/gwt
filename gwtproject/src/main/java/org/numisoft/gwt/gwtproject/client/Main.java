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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Main implements EntryPoint {

	CustomerRequest request;
	FlexTable resultTable;
	List<Customer> customers;
	Label console;
	final int PAGE_MODULE = 10;
	int pages;
	int currentPage;
	final String[] TITLES = { "Mr.", "Ms.", "Dr." };
	final String[] TYPES = { "Residential", "Small/Medium Business", "Enterprise" };
	Customer editCustomer, deleteCustomer;
	int rowEdit, rowDelete, rowConfirm, rowConfirmDelete;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This method is application entry point.
	 * */
	@Override
	public void onModuleLoad() {

		initializeUI();

		/* Check if DB relations exist. If not - create new DB relations. */
		greetingService.checkTables("customers", new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				console.setText("connected to DB");
			}

			@Override
			public void onFailure(Throwable caught) {
				console.setText(caught.getMessage());
			}
		});

		/* Initial search = 10 recently modified customers */
		getResult(new CustomerRequest("", ""));

	}

	/**
	 * This methods initializes UI. Search text boxes and buttons are added to
	 * root panel.
	 * */
	public void initializeUI() {
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
	 * This method sends request to server and returns list of customers.
	 * */
	public void getResult(CustomerRequest request) {
		greetingService.greetServer(request, new AsyncCallback<List<Customer>>() {
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
				currentPage = 1;

				/* Show first page */
				updateResultTable(currentPage);

				updatePageBar(pages);
			}
		});
	}

	/**
	 * This method updates main table and shows page according to page number
	 * provided.
	 * */
	public void updateResultTable(int page) {

		/* Set up table header */
		RootPanel.get("tableDiv").clear();
		resultTable = new FlexTable();
		resultTable.setText(0, 0, "#");
		resultTable.setText(0, 1, "Title");
		resultTable.setText(0, 2, "First Name");
		resultTable.setText(0, 3, "Last Name");
		resultTable.setText(0, 4, "Customer Type");
		resultTable.setText(0, 5, "Last Modified");
		resultTable.setText(0, 6, "Edit");
		resultTable.setText(0, 7, "Delete");

		/* Add customers according to page number */
		for (int i = (page - 1) * PAGE_MODULE, j = 1; i <= (PAGE_MODULE * page - 1)
				&& i < customers.size(); i++, j++) {
			resultTable.setText(j, 0, Integer.toString(i + 1));
			resultTable.setText(j, 1, customers.get(i).getTitle());
			resultTable.setText(j, 2, customers.get(i).getFirstName());
			resultTable.setText(j, 3, customers.get(i).getLastName());
			resultTable.setText(j, 4, customers.get(i).getCustomerType());
			resultTable.setText(j, 5, customers.get(i).getModifiedWhen());

			/* Button 'EDIT' start */
			Button bEdit = new Button("Edit");
			bEdit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/* Get customer from List<Customer> to edit */
					rowEdit = resultTable.getCellForEvent(event).getRowIndex();
					editCustomer = customers.get((currentPage - 1) * PAGE_MODULE + rowEdit - 1);
					console.setText("Editing customer: " + editCustomer.getFirstName() + " "
							+ editCustomer.getLastName());

					/* TITLE: switch label to list box */
					final ListBox lbTitle = new ListBox();
					for (int i = 0; i < TITLES.length; i++) {
						lbTitle.addItem(TITLES[i]);
						if (editCustomer.getTitle().equalsIgnoreCase(TITLES[i])) {
							lbTitle.setSelectedIndex(i);
						}
					}
					lbTitle.setStyleName("lbTitle");
					resultTable.setWidget(rowEdit, 1, lbTitle);

					/* FIRST NAME: switch label to text box */
					final TextBox tbFirstName = new TextBox();
					tbFirstName.setStyleName("tbFirstName");
					tbFirstName.setText(editCustomer.getFirstName());
					resultTable.setWidget(rowEdit, 2, tbFirstName);

					/* LAST NAME: switch label to text box */
					final TextBox tbLastName = new TextBox();
					tbLastName.setStyleName("tbLastName");
					tbLastName.setText(editCustomer.getLastName());
					resultTable.setWidget(rowEdit, 3, tbLastName);

					/* CUSTOMER TYPE: switch label to list box */
					final ListBox lbCustomerType = new ListBox();
					for (int i = 0; i < TYPES.length; i++) {
						lbCustomerType.addItem(TYPES[i]);
						if (editCustomer.getCustomerType().equalsIgnoreCase(TYPES[i])) {
							lbCustomerType.setSelectedIndex(i);
						}
					}
					lbCustomerType.setStyleName("lbCustomerType");
					resultTable.setWidget(rowEdit, 4, lbCustomerType);

					/* Switch button 'EDIT' to 'CONFIRM edit' */
					/* Button 'CONFIRM edit' start */
					Button bConfirmEdit = new Button("Confirm");
					bConfirmEdit.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (!Verifier.isEnglish(tbFirstName.getValue() + tbLastName.getValue())) {
								console.setText("Wrong input, English please!");
								return;
							}

							/* Get customer from List<Customer> to update */
							rowConfirm = resultTable.getCellForEvent(event).getRowIndex();
							editCustomer = customers.get((currentPage - 1) * PAGE_MODULE
									+ rowConfirm - 1);

							/*
							 * Set customer with new values from list boxes and
							 * text boxes
							 */
							editCustomer.setTitle(lbTitle.getSelectedItemText());
							editCustomer.setFirstName(tbFirstName.getValue());
							editCustomer.setLastName(tbLastName.getValue());
							editCustomer.setCustomerTypeId(lbCustomerType.getSelectedIndex() + 1);

							/* Update server */
							greetingService.modifyCustomer(editCustomer,
									new AsyncCallback<String>() {
										@Override
										public void onFailure(Throwable caught) {
											console.setText(caught.getMessage());
										}

										@Override
										public void onSuccess(String modifiedWhen) {
											resultTable.setText(rowConfirm, 5,
													modifiedWhen.substring(0, 19));
											resultTable.setText(rowConfirm, 6, "Updated");
											console.setText("Customer updated");
										}
									});
						}
					});
					resultTable.setWidget(rowEdit, 6, bConfirmEdit);
					/* Button 'CONFIRM edit' finish */
				}
			});
			resultTable.setWidget(j, 6, bEdit);
			/* Button 'EDIT' finish */

			/* Button 'DELETE' start */
			Button bDelete = new Button("Delete");
			bDelete.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/* Get customer from List<Customer> to delete */
					rowDelete = resultTable.getCellForEvent(event).getRowIndex();
					deleteCustomer = customers.get((currentPage - 1) * PAGE_MODULE + rowDelete - 1);
					console.setText("Deleting customer: " + editCustomer.getFirstName() + " "
							+ editCustomer.getLastName());

					/* Switch button 'DELETE' to 'CONFIRM delete' */
					/* Button 'CONFIRM delete' start */
					Button bConfirmDelete = new Button("Confirm");
					bConfirmDelete.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							/* Get customer from List<Customer> to delete delete */
							rowConfirmDelete = resultTable.getCellForEvent(event).getRowIndex();
							deleteCustomer = customers.get((currentPage - 1) * PAGE_MODULE
									+ rowConfirmDelete - 1);

							/* Update server */
							greetingService.deleteCustomer(deleteCustomer,
									new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable caught) {
											console.setText(caught.getMessage());
										}

										@Override
										public void onSuccess(Void result) {
											resultTable.setText(rowConfirmDelete, 7, "Deleted");
											console.setText("Customer terminated");
										}
									});
						}
					});
					resultTable.setWidget(rowDelete, 7, bConfirmDelete);
					/* Button 'CONFIRM delete' finish */
				}
			});
			resultTable.setWidget(j, 7, bDelete);
			/* Button 'DELETE' finish */
		}

		/* Set up all style names */
		resultTable.addStyleName("resultTable");
		resultTable.getColumnFormatter().addStyleName(0, "column0");
		resultTable.getColumnFormatter().addStyleName(1, "column1");
		resultTable.getColumnFormatter().addStyleName(2, "column2");
		resultTable.getColumnFormatter().addStyleName(3, "column3");
		resultTable.getColumnFormatter().addStyleName(4, "column4");
		resultTable.getColumnFormatter().addStyleName(5, "column5");
		resultTable.getColumnFormatter().addStyleName(6, "column6");
		resultTable.getColumnFormatter().addStyleName(7, "column7");
		resultTable.getRowFormatter().addStyleName(0, "resultTableHeader");

		RootPanel.get("tableDiv").add(resultTable);
	}

	/**
	 * This method updates page buttons in page bar.
	 * 
	 * */
	public void updatePageBar(int pages) {

		RootPanel.get("pageBar").clear();
		FlexTable pageTable = new FlexTable();

		for (int p = 1; p <= pages; p++) {
			final Button pageButton = new Button(Integer.toString(p));
			pageButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					currentPage = Integer.parseInt(pageButton.getText());
					updateResultTable(currentPage);
				}
			});
			pageTable.setWidget(0, p, pageButton);
		}
		RootPanel.get("pageBar").add(pageTable);

	}

	// blue = 148aca | lt_gray = f0f1ed | dk_gray = 6a7073
	// moss = 158a5e | orange = ff7a1e

}