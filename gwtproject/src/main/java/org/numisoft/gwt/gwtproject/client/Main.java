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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Main implements EntryPoint {

	CustomerRequest request;
	List<Customer> customers;
	Label label1;
	final int PAGE_MODULE = 5;
	int pages = 0;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	@Override
	public void onModuleLoad() {

		label1 = new Label();
		RootPanel.get("console").add(label1);

		final TextBox tbFirstName = new TextBox();
		RootPanel.get("searchBar").add(tbFirstName);

		final TextBox tbLastName = new TextBox();
		RootPanel.get("searchBar").add(tbLastName);

		Button button1 = new Button("Enter");

		button1.addClickHandler(new ClickHandler() {

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

		RootPanel.get("searchBar").add(button1);

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
			public void onSuccess(List<Customer> cust) {

				customers = cust;

				if (customers.size() % PAGE_MODULE == 0) {
					pages = customers.size() / PAGE_MODULE;
				} else {
					pages = 1 + customers.size() / PAGE_MODULE;
				}

				label1.setText(Integer.toString(customers.size()) + " entries on "
						+ Integer.toString(pages) + " pages");

				updateResultTable(1);
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
		FlexTable resultTable = new FlexTable();

		resultTable.setText(0, 0, "#");
		resultTable.setText(0, 1, "Title");
		resultTable.setText(0, 2, "First Name");
		resultTable.setText(0, 3, "Last Name");
		resultTable.setText(0, 4, "Customer Type");
		resultTable.setText(0, 5, "Last Modified");

		for (int i = (page - 1) * PAGE_MODULE, j = 1; i <= (PAGE_MODULE * page - 1)
				&& i < customers.size(); i++, j++) {
			resultTable.setText(j, 0, Integer.toString(i + 1));
			resultTable.setText(j, 1, customers.get(i).getTitle());
			resultTable.setText(j, 2, customers.get(i).getFirstName());
			resultTable.setText(j, 3, customers.get(i).getLastName());
			resultTable.setText(j, 4, customers.get(i).getCustomerType());
			resultTable.setText(j, 5, customers.get(i).getModifiedWhen());
		}

		resultTable.addStyleName("resultTable");
		resultTable.getColumnFormatter().addStyleName(0, "column0");
		resultTable.getColumnFormatter().addStyleName(1, "column1");
		resultTable.getColumnFormatter().addStyleName(2, "column2");
		resultTable.getColumnFormatter().addStyleName(3, "column3");
		resultTable.getColumnFormatter().addStyleName(4, "column4");
		resultTable.getColumnFormatter().addStyleName(5, "column5");
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
					updateResultTable(Integer.parseInt(pageButton.getText()));
				}
			});
			pageTable.setWidget(0, p, pageButton);
		}
		RootPanel.get("pageBar").add(pageTable);

	}

}