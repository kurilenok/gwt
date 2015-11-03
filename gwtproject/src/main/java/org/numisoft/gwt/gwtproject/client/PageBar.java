package org.numisoft.gwt.gwtproject.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This class represents page bar below result table. User can switch pages by
 * clicking buttons on page bar.
 * 
 * */
public class PageBar extends FlexTable {

	public PageBar(int pages) {

		// RootPanel.get("pageBar").clear();

		for (int p = 1; p <= pages; p++) {
			final Button pageButton = new Button(Integer.toString(p));
			pageButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Main.currentPage = Integer.parseInt(pageButton.getText());
					/* Update result table according to page number selected */
					RootPanel.get("tableDiv").clear();
					RootPanel.get("tableDiv").add(new ResultTable(Main.currentPage));
				}
			});
			setWidget(0, p, pageButton);
		}
		// RootPanel.get("pageBar").add(this);
	}
}
