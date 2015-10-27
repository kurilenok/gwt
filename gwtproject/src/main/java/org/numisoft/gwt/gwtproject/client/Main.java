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
	Label label1;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	@Override
	public void onModuleLoad() {

		label1 = new Label();
		label1.setText("Enter first name:");
		RootPanel.get().add(label1);

		final TextBox tbFirstName = new TextBox();
		RootPanel.get().add(tbFirstName);

		final TextBox tbLastName = new TextBox();
		RootPanel.get().add(tbLastName);

		Button button1 = new Button("Enter");

		button1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				request = new CustomerRequest(tbFirstName.getValue(), tbLastName.getValue());
				label1.setText(request.getFirstName() + " " + request.getLastName());
				createTable(request);
			}
		});

		RootPanel.get().add(button1);

	}

	public void createTable(CustomerRequest request) {
		greetingService.greetServer(request, new AsyncCallback<List<Customer>>() {

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				Label label2 = new Label();
				label2.setText("Error: " + details);
				RootPanel.get().add(label2);
			}

			@Override
			public void onSuccess(List<Customer> customers) {
				FlexTable table = new FlexTable();
				int i = 0;
				for (Customer c : customers) {
					i++;
					table.setText(i, 0, c.getFirstName());
					table.setText(i, 1, c.getLastName());
				}
				RootPanel.get().add(table);
			}
		});
	}
}