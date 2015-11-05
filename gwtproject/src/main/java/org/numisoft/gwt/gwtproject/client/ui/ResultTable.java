package org.numisoft.gwt.gwtproject.client.ui;

import org.numisoft.gwt.gwtproject.client.GreetingService;
import org.numisoft.gwt.gwtproject.client.GreetingServiceAsync;
import org.numisoft.gwt.gwtproject.client.Main;
import org.numisoft.gwt.gwtproject.shared.Customer;
import org.numisoft.gwt.gwtproject.shared.Verifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This class represents main result table. Table displays list of customers
 * according to user request and page number selected.
 * 
 * */
public class ResultTable extends FlexTable {

	private final String[] TITLES = { "Mr.", "Ms.", "Dr." };
	private final String[] TYPES = { "Residential", "Small/Medium Business", "Enterprise" };
	private Customer editCustomer, deleteCustomer;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public ResultTable(int page) {

		// RootPanel.get("tableDiv").clear();

		/* Set up table header */
		setText(0, 0, "#");
		setText(0, 1, "Title");
		setText(0, 2, "First Name");
		setText(0, 3, "Last Name");
		setText(0, 4, "Customer Type");
		setText(0, 5, "Last Modified");
		setText(0, 6, "Edit");
		setText(0, 7, "Delete");

		for (int i = (page - 1) * Main.PAGE_MODULE, j = 1; i <= (Main.PAGE_MODULE * page - 1)
				&& i < Main.customers.size(); i++, j++) {
			setText(j, 0, Integer.toString(i + 1));
			setText(j, 1, Main.customers.get(i).getTitle());
			setText(j, 2, Main.customers.get(i).getFirstName());
			setText(j, 3, Main.customers.get(i).getLastName());
			setText(j, 4, Main.customers.get(i).getCustomerType());
			setText(j, 5, Main.customers.get(i).getModifiedWhen());

			/* Button 'EDIT' start */
			Button bEdit = new Button("Edit");
			bEdit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/* Get customer from List<Customer> to edit */
					int rowEdit = getCellForEvent(event).getRowIndex();
					editCustomer = Main.customers.get((Main.currentPage - 1) * Main.PAGE_MODULE
							+ rowEdit - 1);
					Main.console.setText("Editing customer: " + editCustomer.getFirstName() + " "
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
					setWidget(rowEdit, 1, lbTitle);

					/* FIRST NAME: switch label to text box */
					final TextBox tbFirstName = new TextBox();
					tbFirstName.setStyleName("tbFirstName");
					tbFirstName.setText(editCustomer.getFirstName());
					setWidget(rowEdit, 2, tbFirstName);

					/* LAST NAME: switch label to text box */
					final TextBox tbLastName = new TextBox();
					tbLastName.setStyleName("tbLastName");
					tbLastName.setText(editCustomer.getLastName());
					setWidget(rowEdit, 3, tbLastName);

					/* CUSTOMER TYPE: switch label to list box */
					final ListBox lbCustomerType = new ListBox();
					for (int i = 0; i < TYPES.length; i++) {
						lbCustomerType.addItem(TYPES[i]);
						if (editCustomer.getCustomerType().equalsIgnoreCase(TYPES[i])) {
							lbCustomerType.setSelectedIndex(i);
						}
					}
					lbCustomerType.setStyleName("lbCustomerType");
					setWidget(rowEdit, 4, lbCustomerType);

					/* Switch button 'EDIT' to 'CONFIRM edit' */
					/* Button 'CONFIRM edit' start */
					Button bConfirmEdit = new Button("Confirm");
					bConfirmEdit.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (!Verifier.isEnglish(tbFirstName.getValue() + tbLastName.getValue())) {
								Main.console.setText("Wrong input, English please!");
								return;
							} else if (!Verifier.isNotTooShortOrTooLong(tbFirstName.getValue())) {
								Main.console
										.setText("Wrong input, FIRST NAME is too short or too long!");
								return;
							} else if (!Verifier.isNotTooShortOrTooLong(tbLastName.getValue())) {
								Main.console
										.setText("Wrong input, LAST NAME is too short or too long!");
								return;
							}

							/* Get customer from List<Customer> to update */
							final int rowConfirm = getCellForEvent(event).getRowIndex();
							editCustomer = Main.customers.get((Main.currentPage - 1)
									* Main.PAGE_MODULE + rowConfirm - 1);

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
											Main.console.setText(caught.getMessage());
										}

										@Override
										public void onSuccess(String modifiedWhen) {
											setText(rowConfirm, 5, modifiedWhen.substring(0, 19));
											setText(rowConfirm, 6, "Updated");
											Main.console.setText("Customer updated");
										}
									});
						}
					});
					setWidget(rowEdit, 6, bConfirmEdit);
					/* Button 'CONFIRM edit' finish */
				}
			});
			setWidget(j, 6, bEdit);
			/* Button 'EDIT' finish */

			/* Button 'DELETE' start */
			Button bDelete = new Button("Delete");
			bDelete.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					/* Get customer from List<Customer> to delete */
					int rowDelete = getCellForEvent(event).getRowIndex();
					deleteCustomer = Main.customers.get((Main.currentPage - 1) * Main.PAGE_MODULE
							+ rowDelete - 1);
					Main.console.setText("Deleting customer: " + deleteCustomer.getFirstName()
							+ " " + deleteCustomer.getLastName());

					/* Switch button 'DELETE' to 'CONFIRM delete' */
					/* Button 'CONFIRM delete' start */
					Button bConfirmDelete = new Button("Confirm");
					bConfirmDelete.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							/* Get customer from List<Customer> to delete delete */
							final int rowConfirmDelete = getCellForEvent(event).getRowIndex();
							deleteCustomer = Main.customers.get((Main.currentPage - 1)
									* Main.PAGE_MODULE + rowConfirmDelete - 1);

							/* Update server */
							greetingService.deleteCustomer(deleteCustomer,
									new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable caught) {
											Main.console.setText(caught.getMessage());
										}

										@Override
										public void onSuccess(Void result) {
											setText(rowConfirmDelete, 7, "Terminated");
											Main.console.setText("Customer terminated");
										}
									});
						}
					});
					setWidget(rowDelete, 7, bConfirmDelete);
					/* Button 'CONFIRM delete' finish */
				}
			});
			setWidget(j, 7, bDelete);
			/* Button 'DELETE' finish */
		}

		/* Set up all style names */
		addStyleName("resultTable");
		getColumnFormatter().addStyleName(0, "column0");
		getColumnFormatter().addStyleName(1, "column1");
		getColumnFormatter().addStyleName(2, "column2");
		getColumnFormatter().addStyleName(3, "column3");
		getColumnFormatter().addStyleName(4, "column4");
		getColumnFormatter().addStyleName(5, "column5");
		getColumnFormatter().addStyleName(6, "column6");
		getColumnFormatter().addStyleName(7, "column7");
		getRowFormatter().addStyleName(0, "resultTableHeader");

		// RootPanel.get("tableDiv").add(this);
	}
}
