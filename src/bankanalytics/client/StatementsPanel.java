package bankanalytics.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class StatementsPanel extends Composite {

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
	private FlexTable statementsFlexTable = new FlexTable(); // TODO see
																// dataGrid
	// Add panel
	private HorizontalPanel addPanel = new HorizontalPanel();
	private VerticalPanel detailsPanel = new VerticalPanel();
	// Date
	private Label dateLabel = new Label("Transaction Date: ");
	private DatePicker datePicker = new DatePicker();
	// Description
	private Label descriptionLabel = new Label("Description: ");
	private TextBox descriptionTextBox = new TextBox();
	// Category
	private Label categoryLabel = new Label("Category: ");
	private ListBox categoryListBox = new ListBox();
	// Amount
	private VerticalPanel amountPanel = new VerticalPanel();
	private Label paymentLabel = new Label("Payment: ");
	private TextBox paymentTextBox = new TextBox();
	private Label depositLabel = new Label("Deposit: ");
	private TextBox depositTextBox = new TextBox();
	// Add button
	private Button addTransactionLineButton = new Button("Add Transaction");
	// User login
	private Anchor signOutLink = new Anchor("Sign Out");

	private Anchor categoryManagerLink = new Anchor("Manage categories");
	private Anchor statementsByCategoryLink = new Anchor(
			"See statements by category");

	private LoginInfo loginInfo = null;
	private AccountInfo accountInfo = null;
	private final AccountServiceAsync accountService = GWT
			.create(AccountService.class);
	private ArrayList<TransactionLineInfo> transactionLines = new ArrayList<TransactionLineInfo>();
	private ArrayList<CategoryInfo> categories = new ArrayList<CategoryInfo>();
	private double accountBalance;

	/*
	 * Constructor
	 */
	public StatementsPanel(LoginInfo loginInfo, final AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
		this.loginInfo = loginInfo;
		accountBalance = 0;
		// Set up sign out hyperlink.
		signOutLink.setHref(loginInfo.getLogoutUrl());
		mainPanel.add(signOutLink);
		Label userLabel = new Label("Hello " + loginInfo.getNickname()
				+ ", \nOn that page you can manage your accounts.");
		mainPanel.add(userLabel);
		Label titleLabel = new Label(accountInfo.getAccountName() + " ("
				+ accountInfo.getRunningBalance() + " $)");
		mainPanel.add(titleLabel);

		// Link to open the popup displaying the amounts by category
		statementsByCategoryLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new StatementsByCategoryDialog(accountInfo).show();
			}
		});
		mainPanel.add(statementsByCategoryLink);

		// Link to open the popup to manage categories
		categoryManagerLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new CategoryManagerDialog(accountInfo, StatementsPanel.this).show();
			}
		});
		mainPanel.add(categoryManagerLink);

		// Add elements to the main panel
		loadMainPanel();

		// All composites must call initWidget() in their constructors.
		initWidget(mainPanel);

		// Give the overall composite a style name.
		setStyleName("Statements-Panel");
	}

	/*
	 * Add elements to the main panel
	 */
	private void loadMainPanel() {

		// Create table for statement lines.
		initializeTable();

		// Create table for statements line
		loadTransactionLines();

		// Assemble details panel.
		detailsPanel.add(dateLabel);
		detailsPanel.add(datePicker);
		detailsPanel.add(descriptionLabel);
		detailsPanel.add(descriptionTextBox);
		detailsPanel.add(categoryLabel);
		// Add available categories to the list.
		loadCategories();
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

	/*
	 * Load the existing categories and add them to the list
	 */
	private void loadCategories() {

		accountService.getCategories(accountInfo,
				new AsyncCallback<CategoryInfo[]>() {

					@Override
					public void onFailure(Throwable caught) {
						handleError(caught);
					}

					@Override
					public void onSuccess(CategoryInfo[] result) {
						if (result != null) {
							for (int i = 0; i < result.length; i++) {
								categoryListBox.addItem(result[i]
										.getCategoryName());
								categories.add(result[i]);
							}
						}
					}
				});
	}

	/*
	 * Load existing transactions and add them to the list and to the table
	 */
	private void loadTransactionLines() {
		accountService.getTransactions(accountInfo,
				new AsyncCallback<TransactionLineInfo[]>() {

					@Override
					public void onSuccess(TransactionLineInfo[] result) {
						if (result != null) {
							accountBalance = 0;
							for (TransactionLineInfo transaction : result) {
								accountBalance += transaction.getAmount();
								transaction.setLineBalance(accountBalance);
								transactionLines.add(transaction);
								displayTransactionLine(transaction);
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						handleError(caught);
					}
				});
	}

	/**
	 * Add statement line to FlexTable. Executed when the user clicks the
	 * addTransactionLineButton or presses enter.
	 */
	private void addTransactionLine() {

		// Get Date
		final Date date = datePicker.getValue();
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
		String errorMsg = "Transaction could not be added : \n";
		if (date == null) {
			errorMsg += "Please select the transaction date. \n";
			error = true;
		}
		// Transaction description must be 1 or more chars that are numbers,
		// letters, white-spaces, hyphens, dots, underscore, :, /, or #.
		if (!description.matches("^[-#:/\\w\\.\\s]+$")) {
			errorMsg += "Please check the transaction description : it must only contain letters, numbers, spaces, underscores, hyphens, dots, :, /, and #. \n";
			error = true;
		}
		// A category must be selected
		if (selectedIndex == -1) {
			errorMsg += "Please select the transaction category. \n";
			error = true;
		}
		// Payment and deposit are numbers and one of the two must be filled
		if (payment.isEmpty() && deposit.isEmpty()) {
			errorMsg += "Please fill in the transaction amount (payment or deposit). \n";
			error = true;
		}
		if ((!payment.isEmpty() && !payment.matches("^[\\.0-9]+$"))
				|| (!deposit.isEmpty() && !deposit.matches("^[0-9\\.]+$"))) {
			errorMsg += "The amount value must be a number. \n";
			error = true;
		}
		if (error) {
			Window.alert(errorMsg);
			return;
		}

		// Get statement category
		String category = categoryListBox.getValue(categoryListBox
				.getSelectedIndex());
		// categoryListBox.setSelectedIndex(0);

		// Get statement amount
		double amount;
		if (!payment.isEmpty()) { // The transaction is a payment, it is a
									// negative amount.
			amount = -1.0 * Double.parseDouble(payment);
			paymentTextBox.setText("");
		} else { // The transaction is a deposit.
			amount = Double.parseDouble(deposit);
			depositTextBox.setText("");
		}

		descriptionTextBox.setFocus(true);
		descriptionTextBox.setText("");
		datePicker.setCurrentMonth(new Date());
		paymentTextBox.setText("");
		depositTextBox.setText("");

		addTransactionLine(date, description, category, amount);
	}

	/*
	 * Add new transaction to the database and display it
	 */
	private void addTransactionLine(final Date date, final String description,
			final String categoryName, double amount) {

		accountService.addTransaction(accountInfo, date, description,
				categoryName, amount, new AsyncCallback<TransactionLineInfo>() {

					@Override
					public void onFailure(Throwable caught) {
						handleError(caught);
					}

					@Override
					public void onSuccess(TransactionLineInfo result) {
						transactionLines.add(result);
						if (transactionLines.size() > 1
								&& transactionLines
										.get(transactionLines.size() - 2)
										.getAddDate()
										.after(result.getAddDate())) {
							// if the new transaction was before the last one,
							// we have to sort the list an display again all the
							// line in the good order
							Collections.sort(transactionLines);
							accountBalance = 0;
							initializeTable();
							for (TransactionLineInfo transaction : transactionLines) {
								accountBalance += transaction.getAmount();
								transaction.setLineBalance(accountBalance);
								displayTransactionLine(transaction);
							}

						} else {
							// we only display the new transaction at the end
							accountBalance += result.getAmount();
							result.setLineBalance(accountBalance);
							displayTransactionLine(result);
						}
					}
				});
	}

	/*
	 * Display a transaction line in the table
	 */
	private void displayTransactionLine(final TransactionLineInfo transaction) {

		// Add the transaction line to the table.
		int rowNum = statementsFlexTable.getRowCount();

		// FIXME Fill the cells
		statementsFlexTable.setText(rowNum, NUM_COLUMN_NUMBER, ""
				+ (transactionLines.indexOf(transaction) + 1));
		statementsFlexTable.setText(rowNum, DATE_COLUMN_NUMBER, DateTimeFormat
				.getFormat("dd/MM/yyyy").format(transaction.getAddDate()));
		statementsFlexTable.setText(rowNum, DESC_COLUMN_NUMBER,
				transaction.getDescription());
		statementsFlexTable.setText(rowNum, CAT_COLUMN_NUMBER,
				transaction.getCategory());
		// statementsFlexTable.setHTML(rowNum, AMOUNT_COLUMN_NUMBER,
		// "<div style=\"background-color:red;\" />");
		statementsFlexTable.setText(rowNum, AMOUNT_COLUMN_NUMBER,
				Double.toString(transaction.getAmount()));
		statementsFlexTable.setText(rowNum, BALANCE_COLUMN_NUMBER,
				Double.toString(transaction.getLineBalance()));

		// Add a button to remove this line from the table.
		// Button removeTransactionLineButton = new Button("x");
		// removeTransactionLineButton.addStyleDependentName("remove");
		// removeTransactionLineButton.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// removeTransactionLine(transaction);
		// }
		// });
		// statementsFlexTable.setWidget(rowNum, REMOVE_COLUMN_NUMBER,
		// removeTransactionLineButton);

	}

	private void removeTransactionLine(final TransactionLineInfo transaction) {
		// TODO
		// accountService.removeTransactionLine(transaction.getId(),
		// new AsyncCallback<Void>() {
		// public void onFailure(Throwable error) {
		// handleError(error);
		// }
		//
		// public void onSuccess(Void ignore) {
		// undisplayTransactionLine(transaction);
		// }
		// });
	}

	private void initializeTable() {
		statementsFlexTable.removeAllRows();
		// Set up the header row of the table.
		statementsFlexTable.setText(0, NUM_COLUMN_NUMBER, "#");
		statementsFlexTable.setText(0, DATE_COLUMN_NUMBER, "Date");
		statementsFlexTable.setText(0, DESC_COLUMN_NUMBER, "Description");
		statementsFlexTable.setText(0, CAT_COLUMN_NUMBER, "Category");
		statementsFlexTable.setText(0, AMOUNT_COLUMN_NUMBER, "Amount");
		statementsFlexTable
				.setText(0, BALANCE_COLUMN_NUMBER, "Running Balance");
		// statementsFlexTable.setText(0, REMOVE_COLUMN_NUMBER, "Remove");
		// Add styles to the statements flex table.
		statementsFlexTable.getRowFormatter().addStyleName(0,
				"statementsTableHeader");
		statementsFlexTable.addStyleName("statementsTable");
	}

	private void undisplayTransactionLine(TransactionLineInfo transaction) {
		int removedIndex = transactionLines.indexOf(transaction);
		transactionLines.remove(removedIndex);
		statementsFlexTable.removeRow(removedIndex + 1);
	}

	private void handleError(Throwable error) {
		// Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
	
	protected void addCategory(CategoryInfo category) {
		categories.add(category);
		categoryListBox.addItem(category.getCategoryName());
	}
}
