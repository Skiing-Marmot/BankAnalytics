package bankanalytics.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
	private FlexTable statementsFlexTable = new FlexTable(); // TODO change to a cellTable
	// Add panel
	private HorizontalPanel addPanel = new HorizontalPanel();
	private VerticalPanel detailsPanel = new VerticalPanel();
	private Label descriptionLabel = new Label("Description: ");
	private TextBox descriptionTextBox = new TextBox();
	private Label categoryLabel = new Label("Category: ");
	private ListBox categoryListBox = new ListBox();
	private VerticalPanel amountPanel = new VerticalPanel();
	private Label paymentLabel = new Label("Payment: ");
	private TextBox paymentTextBox = new TextBox();
	private Label depositLabel = new Label("Deposit: ");
	private TextBox depositTextBox = new TextBox();
	private Button addTransactionLineButton = new Button("Add Transaction");
	// User login
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access your bank account information.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	private LoginInfo loginInfo = null;
	private final TransactionLineServiceAsync transactionLineService = GWT
			.create(TransactionLineService.class);
	private ArrayList<TransactionLineInfo> transactionLines = new ArrayList<TransactionLineInfo>();

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {

		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						handleError(error);
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

		loadCategories();
		loadTransactionLines();

		// Assemble details panel.
		detailsPanel.add(descriptionLabel);
		detailsPanel.add(descriptionTextBox);
		detailsPanel.add(categoryLabel);
		// TODO Add available categories to the list.
		detailsPanel.add(categoryListBox);
		// Assemble amount panel.
		amountPanel.add(paymentLabel);
		amountPanel.add(paymentTextBox);
		amountPanel.add(depositLabel);
		amountPanel.add(depositTextBox);
		// Assemble Add Transaction panel.
		addPanel.add(detailsPanel);
		addPanel.add(amountPanel);
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
		descriptionTextBox.setFocus(true);

		// Listen for mouse events on the Add button.
		addTransactionLineButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addTransactionLine();
			}
		});

		// TODO Listen for keyboard events in the input boxes.
		/*
		 * // Listen for keyboard events in the input box.
		 * descriptionTextBox.addKeyPressHandler(new KeyPressHandler() { public
		 * void onKeyPress(KeyPressEvent event) { if (event.getCharCode() ==
		 * KeyCodes.KEY_ENTER) { addTransactionLine(); } } });
		 */
		
		// TODO Delete paymentTextBox content when user clicks on depositTextBox
		
		// TODO Remove depositTextBox content when user clicks in paymentTextBox

	}
	
	private void loadCategories() {
		categoryListBox.addItem("Clothing");
		categoryListBox.addItem("Food");
		categoryListBox.addItem("Medical care");
	}

	private void loadTransactionLines() {
		transactionLineService.getTransactionLine(new AsyncCallback<TransactionLineInfo[]>() {
					public void onFailure(Throwable error) {
						handleError(error);
					}

					public void onSuccess(TransactionLineInfo[] transactions) {
						if(transactions != null) {
							displayTransactionLines(transactions);
						}
					}
				});
	}

	private void displayTransactionLines(TransactionLineInfo[] transactions) {
		for (TransactionLineInfo transaction : transactions) {
			displayTransactionLine(transaction);
		}
	}

	/**
	 * Add statement line to FlexTable. Executed when the user clicks the
	 * addTransactionLineButton or presses enter.
	 */
	private void addTransactionLine() {
		
		// Get description
		final String description = descriptionTextBox.getText().trim();
		// Get category index
		final int selectedIndex = categoryListBox.getSelectedIndex();
		// Get payment
		final String payment = paymentTextBox.getText().trim();
		// Get deposit
		final String deposit = depositTextBox.getText().trim();
		
		// Check possible errors
		boolean error = false;
		// Transaction description must be 1 or more chars that are numbers,
		// letters, white-spaces, hyphens, dots, underscore, :, /, or #.
		if (!description.matches("^[-#:/\\w\\.\\s]+$")) {
			Window.alert("'" + description + "' is not a valid description.");
			descriptionTextBox.selectAll();
			error = true;
		}
		// A category must be selected
		if(selectedIndex == -1) {
			Window.alert("You must select a category.");
			error = true;
		}
		// Payment and deposit are numbers and one of the two must be filled
		if(payment.isEmpty() && deposit.isEmpty()) {
			Window.alert("You must fill in the transaction amount (payment or deposit).");
			error = true;
		}
		if((!payment.isEmpty() && !payment.matches("^[\\.0-9]+$")) || (!deposit.isEmpty() && !deposit.matches("^[0-9\\.]+$"))) {
			Window.alert("The amount value must be a number.");
			error = true;
		}
		if(error) { return; }

		
		descriptionTextBox.setFocus(true);
		descriptionTextBox.setText("");

		// Get statement category
		String category = categoryListBox.getValue(categoryListBox.getSelectedIndex());
		categoryListBox.setSelectedIndex(-1);
		
		// Get statement amount
		double amount;
		if(!payment.isEmpty()) { // The transaction is a payment, it is a negative amount.
			amount = -1.0 * Double.parseDouble(payment);
			paymentTextBox.setText("");
		}
		else { // The transaction is a deposit.
			amount = Double.parseDouble(deposit);
			depositTextBox.setText("");
		}

		addTransactionLine(description, category, amount);
	}

	private void addTransactionLine(final String description, final String categoryName, double amount) {
		transactionLineService.addTransactionLine(description, categoryName, amount, new AsyncCallback<TransactionLineInfo>() {
					public void onFailure(Throwable error) {
						handleError(error);
					}
					public void onSuccess(TransactionLineInfo result) {
						displayTransactionLine(result);
					}
				});
	}

	private void displayTransactionLine(final TransactionLineInfo transaction) {

		// Add the transaction line to the table.
		int rowNum = statementsFlexTable.getRowCount();
		transactionLines.add(transaction);
		
		// FIXME Fill the cells
		// statementsFlexTable.setText(rowNum, NUM_COLUMN_NUMBER,
		// ""+(transactionLines.indexOf(description)+1));
		statementsFlexTable.setText(rowNum, DATE_COLUMN_NUMBER, DateTimeFormat.getFormat("dd/MM/yyyy").format(transaction.getAddDate()));
		statementsFlexTable.setText(rowNum, DESC_COLUMN_NUMBER, transaction.getDescription());
		statementsFlexTable.setText(rowNum, CAT_COLUMN_NUMBER, transaction.getCategory());
		statementsFlexTable.setText(rowNum, AMOUNT_COLUMN_NUMBER, Double.toString(transaction.getAmount()));
		statementsFlexTable.setText(rowNum, BALANCE_COLUMN_NUMBER, Double.toString(transaction.getLineBalance()));
		// TODO Add the other attributes.

		// Add a button to remove this line from the table.
		Button removeTransactionLineButton = new Button("x");
		removeTransactionLineButton.addStyleDependentName("remove");
		removeTransactionLineButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeTransactionLine(transaction);
			}
		});
		statementsFlexTable.setWidget(rowNum, REMOVE_COLUMN_NUMBER,
				removeTransactionLineButton);

		// FIXME Get the other information.
		refreshStatementsTable();

	}

	private void removeTransactionLine(final TransactionLineInfo transaction) {
		transactionLineService.removeTransactionLine(transaction.getId(),
				new AsyncCallback<Void>() {
					public void onFailure(Throwable error) {
						handleError(error);
					}

					public void onSuccess(Void ignore) {
						undisplayTransactionLine(transaction);
					}
				});
	}

	private void undisplayTransactionLine(TransactionLineInfo transaction) {
		int removedIndex = transactionLines.indexOf(transaction);
		transactionLines.remove(removedIndex);
		statementsFlexTable.removeRow(removedIndex + 1);
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}

	// FIXME delete method
	private void refreshStatementsTable() {
		// TODO Auto-generated method stub

	}

}