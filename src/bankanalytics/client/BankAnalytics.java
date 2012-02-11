package bankanalytics.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BankAnalytics implements EntryPoint {

	// Constant for column number in statementsFlexTable
	private final static int NUM_COLUMN_NUMBER = 0;
	private final static int DATE_COLUMN_NUMBER = 1;
	private final static int DESC_COLUMN_NUMBER = 2;
	private final static int CAT_COLUMN_NUMBER = 3;
	private final static int AMOUNT_COLUMN_NUMBER = 4;
	private final static int BALANCE_COLUMN_NUMBER = 5;
	private final static int REMOVE_COLUMN_NUMBER = 6;

	// UI components
	// Main panel
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable statementsFlexTable = new FlexTable();
	// Add panel
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newDescriptionTextBox = new TextBox();
	private Button addTransactionLineButton = new Button("Add");
	// User login
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access your bank account information.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	// private Label lastUpdatedLabel = new Label();

	private LoginInfo loginInfo = null;
	private ArrayList<String> transactionLines = new ArrayList<String>();

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {

		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							loadAccountInformation();
						} else {
							loadLogin();
						}
					}
				});
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("statementsList").add(loginPanel);
	}

	private void loadAccountInformation() {
		
		// Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());

		// Create table for statement lines.
		// Set up the header row of the table.
		statementsFlexTable.setText(0, NUM_COLUMN_NUMBER, "#");
		statementsFlexTable.setText(0, DATE_COLUMN_NUMBER, "Date");
		statementsFlexTable.setText(0, DESC_COLUMN_NUMBER, "Description");
		statementsFlexTable.setText(0, CAT_COLUMN_NUMBER, "Category");
		statementsFlexTable.setText(0, AMOUNT_COLUMN_NUMBER, "Amount");
		statementsFlexTable
				.setText(0, BALANCE_COLUMN_NUMBER, "Running Balance");
		statementsFlexTable.setText(0, REMOVE_COLUMN_NUMBER, "Remove");
		// Add styles to the statements flex table.
		statementsFlexTable.getRowFormatter().addStyleName(0,
				"statementsTableHeader");
		statementsFlexTable.addStyleName("statementsTable");

		// Assemble Add Transaction panel.
		addPanel.add(newDescriptionTextBox);
		addPanel.add(addTransactionLineButton);
		addPanel.addStyleName("addPanel");

		// Assemble Main panel.
		mainPanel.add(statementsFlexTable);
		mainPanel.add(addPanel);
		// mainPanel.add(lastUpdatedLabel);
		mainPanel.addStyleName("mainPanel");
		mainPanel.add(signOutLink);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("statementsList").add(mainPanel);

		// Move cursor focus to the input box.
		newDescriptionTextBox.setFocus(true);

		// Listen for mouse events on the Add button.
		addTransactionLineButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addTransactionLine();
			}
		});

		// Listen for keyboard events in the input box.
		newDescriptionTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addTransactionLine();
				}
			}
		});

	}

	/**
	 * Add statement line to FlexTable. Executed when the user clicks the
	 * addTransactionLineButton or presses enter.
	 */
	private void addTransactionLine() {

		final String description = newDescriptionTextBox.getText().trim();
		newDescriptionTextBox.setFocus(true);

		// Transaction description must be 1 or more chars that are numbers,
		// letters, white-spaces, hyphens, dots, underscore, :, /, or #.
		if (!description.matches("^[-#:/\\w\\.\\s]+$")) {
			Window.alert("'" + description + "' is not a valid symbol.");
			newDescriptionTextBox.selectAll();
			return;
		}

		newDescriptionTextBox.setText("");

		// Don't add the transaction line if it's already in the table.
		if (transactionLines.contains(description)) {
			return;
		}

		// Add the transaction line to the table.
		int rowNum = statementsFlexTable.getRowCount();
		transactionLines.add(description);
		// statementsFlexTable.setText(rowNum, NUM_COLUMN_NUMBER,
		// ""+(transactionLines.indexOf(description)+1));
		statementsFlexTable.setText(rowNum, DESC_COLUMN_NUMBER, description);
		// TODO Add the other attributes.

		// Add a button to remove this line from the table.
		Button removeTransactionLineButton = new Button("x");
		removeTransactionLineButton.addStyleDependentName("remove");
		removeTransactionLineButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = transactionLines.indexOf(description);
				transactionLines.remove(removedIndex);
				statementsFlexTable.removeRow(removedIndex + 1);
			}
		});
		statementsFlexTable.setWidget(rowNum, REMOVE_COLUMN_NUMBER,
				removeTransactionLineButton);

		// Get the other information.
		refreshStatementsTable();

	}

	private void refreshStatementsTable() {
		// TODO Auto-generated method stub

	}

}