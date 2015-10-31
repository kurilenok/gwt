package org.numisoft.gwt.gwtproject.client;

import java.util.List;

import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.CustomerRequest;

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
	Label label1;
	final int PAGE_MODULE = 5;
	int pages;
	int currentPage;
	final String[] TITLES = { "Mr.", "Ms.", "Dr." };
	final String[] TYPES = { "Residential", "Small/Medium Business", "Enterprise" };
	Customer editCustomer;
	int rowEdit, rowConfirm;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	@Override
	public void onModuleLoad() {

		label1 = new Label();
		RootPanel.get("console").add(label1);

		final TextBox tbFirstName = new TextBox();
		RootPanel.get("searchBar").add(tbFirstName);

		final TextBox tbLastName = new TextBox();
		RootPanel.get("searchBar").add(tbLastName);

		Button bSearch = new Button("Enter");
		// bSearch.setStyleName("bSearch");
		bSearch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (!verifyInput(tbFirstName.getValue() + tbLastName.getValue())) {
					label1.setText("Wrong input");
					tbFirstName.setText("");
					tbLastName.setText("");
					return;
				}
				request = new CustomerRequest(tbFirstName.getValue(), tbLastName.getValue());
				label1.setText(request.getFirstName() + " " + request.getLastName());
				getResult(request);
			}
		});
		RootPanel.get("searchBar").add(bSearch);

		getResult(new CustomerRequest("", ""));

	}

	public void getResult(CustomerRequest request) {
		greetingService.greetServer(request, new AsyncCallback<List<Customer>>() {

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				Label label2 = new Label();
				label2.setText("Error: " + details);
				RootPanel.get().add(label2);
			}

			@Override
			public void onSuccess(List<Customer> result) {
				customers = result;
				if (customers.size() % PAGE_MODULE == 0) {
					pages = customers.size() / PAGE_MODULE;
				} else {
					pages = 1 + customers.size() / PAGE_MODULE;
				}
				label1.setText(Integer.toString(customers.size()) + " entries on "
						+ Integer.toString(pages) + " pages");
				currentPage = 1;
				updateResultTable(currentPage);
				updatePageBar(pages);

				// blue = 148aca | lt_gray = f0f1ed | dk_gray = 6a7073
				// moss = 158a5e | orange = ff7a1e

			}
		});
	}

	public boolean verifyInput(String input) {
		if (input.matches("[a-zA-Z]+") || input == "") {
			return true;
		}
		return false;
	}

	public void updateResultTable(int page) {

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

		for (int i = (page - 1) * PAGE_MODULE, j = 1; i <= (PAGE_MODULE * page - 1)
				&& i < customers.size(); i++, j++) {
			resultTable.setText(j, 0, Integer.toString(i + 1));
			resultTable.setText(j, 1, customers.get(i).getTitle());
			resultTable.setText(j, 2, customers.get(i).getFirstName());
			resultTable.setText(j, 3, customers.get(i).getLastName());
			resultTable.setText(j, 4, customers.get(i).getCustomerType());
			resultTable.setText(j, 5, customers.get(i).getModifiedWhen());

			Button bEdit = new Button("Edit");
			bEdit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					int cellIndex = resultTable.getCellForEvent(event).getCellIndex();
					rowEdit = resultTable.getCellForEvent(event).getRowIndex();

					editCustomer = customers.get((currentPage - 1) * PAGE_MODULE + rowEdit - 1);

					label1.setText("cellIndex: " + cellIndex + ", rowIndex: " + rowEdit
							+ ", customer ID: " + editCustomer.getCustomerId());

					final ListBox lbTitle = new ListBox();
					for (int i = 0; i < TITLES.length; i++) {
						lbTitle.addItem(TITLES[i]);
						if (editCustomer.getTitle().equalsIgnoreCase(TITLES[i])) {
							lbTitle.setSelectedIndex(i);
						}
					}
					lbTitle.setStyleName("lbTitle");
					resultTable.setWidget(rowEdit, 1, lbTitle);

					final TextBox tbFirstName = new TextBox();
					tbFirstName.setStyleName("tbFirstName");
					tbFirstName.setText(editCustomer.getFirstName());
					resultTable.setWidget(rowEdit, 2, tbFirstName);

					final TextBox tbLastName = new TextBox();
					tbLastName.setStyleName("tbLastName");
					tbLastName.setText(editCustomer.getLastName());
					resultTable.setWidget(rowEdit, 3, tbLastName);

					ListBox lbCustomerType = new ListBox();
					for (int i = 0; i < TYPES.length; i++) {
						lbCustomerType.addItem(TYPES[i]);
						if (editCustomer.getCustomerType().equalsIgnoreCase(TYPES[i])) {
							lbCustomerType.setSelectedIndex(i);
						}
					}
					lbCustomerType.setStyleName("lbCustomerType");
					resultTable.setWidget(rowEdit, 4, lbCustomerType);

					Button bConfirmEdit = new Button("Confirm");

					resultTable.setWidget(rowEdit, 6, bConfirmEdit);
					bConfirmEdit.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							rowConfirm = resultTable.getCellForEvent(event).getRowIndex();
							editCustomer = customers.get((currentPage - 1) * PAGE_MODULE
									+ rowConfirm - 1);

							editCustomer.setTitle(lbTitle.getSelectedItemText());
							editCustomer.setFirstName(tbFirstName.getValue());
							editCustomer.setLastName(tbLastName.getValue());

							greetingService.modifyCustomer(editCustomer,
									new AsyncCallback<String>() {

										@Override
										public void onFailure(Throwable caught) {
											label1.setText(caught.getMessage());
										}

										@Override
										public void onSuccess(String modifiedWhen) {
											resultTable.setText(rowConfirm, 5,
													modifiedWhen.substring(0, 19));
											resultTable.setText(rowConfirm, 6, "Updated");
										}
									});
						}
					});

				}
			});
			resultTable.setWidget(j, 6, bEdit);

			Button bDelete = new Button("Delete");
			bDelete.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					int cellIndex = resultTable.getCellForEvent(event).getCellIndex();
					int rowIndex = resultTable.getCellForEvent(event).getRowIndex();
					label1.setText("cellIndex: " + cellIndex + ", rowIndex: " + rowIndex);
				}
			});
			resultTable.setWidget(j, 7, bDelete);
		}

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

}