package org.numisoft.gwt.gwtproject.client;

import org.numisoft.gwt.gwtproject.shared.Customer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {

	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	@Override
	public void onModuleLoad() {

		Label label1 = new Label();
		label1.setText("MY TEXT HERE");
		RootPanel.get().add(label1);

		greetingService.greetServer("Tom", new AsyncCallback<Customer[]>() {
			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();

				Label label2 = new Label();

				label2.setText("Error: " + details);
				RootPanel.get().add(label2);
			}

			@Override
			public void onSuccess(Customer[] customers) {

				// CellTable<Customer> table = new CellTable<Customer>();
				// TextColumn<Customer> nameColumn = new TextColumn<Customer>()
				// {
				// @Override
				// public String getValue(Customer customer) {
				// return customer.getFirstName();
				// }
				// };
				// table.addColumn(nameColumn, "Name");
				// ListDataProvider<Customer> dataProvider = new
				// ListDataProvider<Customer>();
				// dataProvider.addDataDisplay(table);
				//
				// dataProvider.setList(customers);
				//
				// RootPanel.get().add(table);
				//
				// Label label3 = new Label();
				// label3.setText(Integer.toString(customers.size()));
				//
				// RootPanel.get().add(label3);

				// FlexTable table = new FlexTable();

				// table.setText(0, 0, customers[0].getFirstName());
				// table.setText(0, 1, customers[0].getLastName());
				//
				// table.setText(1, 0, customers[1].getFirstName());
				// table.setText(1, 1, customers[1].getLastName());
				//
				// table.setText(2, 0, customers[2].getFirstName());
				// table.setText(2, 1, customers[2].getLastName());
				//
				// table.setText(3, 0, customers[3].getFirstName());
				// table.setText(3, 1, customers[3].getLastName());
				//
				// RootPanel.get().add(table);

				Label label4 = new Label();
				label4.setText("Customer 0 is " + customers[0].getFirstName()
						+ "" + customers[0].getLastName());
				RootPanel.get().add(label4);

				Label label5 = new Label();
				label5.setText("Customer 1 is " + customers[1].getFirstName()
						+ "" + customers[1].getLastName());
				RootPanel.get().add(label5);

				Label label6 = new Label();
				label6.setText("Customer 2 is " + customers[2].getFirstName()
						+ "" + customers[2].getLastName());
				RootPanel.get().add(label6);

				Label label7 = new Label();
				label7.setText("Customer 3 is " + customers[3].getFirstName()
						+ "" + customers[3].getLastName());
				RootPanel.get().add(label7);

			}
		});

		//
		// final Button sendButton = new Button(messages.sendButton());
		// final TextBox nameField = new TextBox();
		// nameField.setText(messages.nameField());
		// final Label errorLabel = new Label();
		//
		// // We can add style names to widgets
		// sendButton.addStyleName("sendButton");
		//
		// // Add the nameField and sendButton to the RootPanel
		// // Use RootPanel.get() to get the entire body element
		// RootPanel.get("nameFieldContainer").add(nameField);
		// RootPanel.get("sendButtonContainer").add(sendButton);
		// RootPanel.get("errorLabelContainer").add(errorLabel);
		//
		// // Focus the cursor on the name field when the app loads
		// nameField.setFocus(true);
		// nameField.selectAll();
		//
		// // Create the popup dialog box
		// final DialogBox dialogBox = new DialogBox();
		// dialogBox.setText("Remote Procedure Call");
		// dialogBox.setAnimationEnabled(true);
		// final Button closeButton = new Button("Close");
		// // We can set the id of a widget by accessing its Element
		// closeButton.getElement().setId("closeButton");
		// final Label textToServerLabel = new Label();
		// final HTML serverResponseLabel = new HTML();
		// VerticalPanel dialogVPanel = new VerticalPanel();
		// dialogVPanel.addStyleName("dialogVPanel");
		// dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		// dialogVPanel.add(textToServerLabel);
		// dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		// dialogVPanel.add(serverResponseLabel);
		// dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		// dialogVPanel.add(closeButton);
		// dialogBox.setWidget(dialogVPanel);
		//
		// // Add a handler to close the DialogBox
		// closeButton.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// dialogBox.hide();
		// sendButton.setEnabled(true);
		// sendButton.setFocus(true);
		// }
		// });
		//
		// // Create a handler for the sendButton and nameField
		// class MyHandler implements ClickHandler, KeyUpHandler {
		// /**
		// * Fired when the user clicks on the sendButton.
		// */
		// @Override
		// public void onClick(ClickEvent event) {
		// sendNameToServer();
		// }
		//
		// /**
		// * Fired when the user types in the nameField.
		// */
		// @Override
		// public void onKeyUp(KeyUpEvent event) {
		// if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		// sendNameToServer();
		// }
		// }
		//
		// /**
		// * Send the name from the nameField to the server and wait for a
		// * response.
		// */
		// private void sendNameToServer() {
		// // First, we validate the input.
		// errorLabel.setText("");
		// String textToServer = nameField.getText();
		// if (!FieldVerifier.isValidName(textToServer)) {
		// errorLabel.setText("Please enter at least four characters");
		// return;
		// }
		//
		// // Then, we send the input to the server.
		// sendButton.setEnabled(false);
		// textToServerLabel.setText(textToServer);
		// serverResponseLabel.setText("");
		//
		// greetingService.greetServer(textToServer,
		// new AsyncCallback<ArrayList<Customer>>() {
		// @Override
		// public void onFailure(Throwable caught) {
		// // Show the RPC error message to the user
		// dialogBox
		// .setText("Remote Procedure Call - Failure");
		// serverResponseLabel
		// .addStyleName("serverResponseLabelError");
		// serverResponseLabel.setHTML(SERVER_ERROR);
		// dialogBox.center();
		// closeButton.setFocus(true);
		// }
		//
		// @Override
		// public void onSuccess(ArrayList<Customer> customers) {
		// dialogBox.setText("Remote Procedure Call");
		// serverResponseLabel
		// .removeStyleName("serverResponseLabelError");
		// serverResponseLabel.setHTML(result);
		// dialogBox.center();
		// closeButton.setFocus(true);
		// }
		// });
		// }
		// }
		//
		// // Add a handler to send the name to the server
		// MyHandler handler = new MyHandler();
		// sendButton.addClickHandler(handler);
		// nameField.addKeyUpHandler(handler);
		// }
	}
}