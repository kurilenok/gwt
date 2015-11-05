package org.numisoft.gwt.gwtproject.client;

import java.util.List;

import org.numisoft.gwt.gwtproject.client.ui.UICreator;
import org.numisoft.gwt.gwtproject.shared.Customer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;

public class Main implements EntryPoint {

	public static List<Customer> customers;
	public static Label console = new Label();
	public final static int PAGE_MODULE = 10;
	public static int pages;
	public static int currentPage;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This method is application entry point.
	 * */
	@Override
	public void onModuleLoad() {

		UICreator.logIn();

	}

}