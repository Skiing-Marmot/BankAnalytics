package bankanalytics.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 * Panel displaying the list of the accounts
 */
public class AccountsPanel extends Composite {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FormPanel formPanel = new FormPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private Label csvFileLabel = new Label("Select the CSV File to upload to create your new account: \r\n File format must be : dd/mm/yyyy,description,category,balance");
	private FileUpload csvFileUpload = new FileUpload();
	private Button addAccountButton = new Button("Create account");
	private Anchor signOutLink = new Anchor("Sign Out");

	private LoginInfo loginInfo = null;
	private AccountServiceAsync accountService = GWT.create(AccountService.class);
	private List<AccountInfo> accounts = new ArrayList<AccountInfo>();

	public AccountsPanel(LoginInfo loginInfo) {

		this.loginInfo = loginInfo;
		// Set up sign out hyperlink.
		signOutLink.setHref(loginInfo.getLogoutUrl());
		mainPanel.add(signOutLink);
		Label userLabel = new Label("Hello " + loginInfo.getNickname() + ", \nOn that page you can manage your accounts.");
		mainPanel.add(userLabel);

		loadAccountList();
		loadAddAccountPanel();

		// All composites must call initWidget() in their constructors.
		initWidget(mainPanel);

		// Give the overall composite a style name.
		setStyleName("Accounts-Panel");

	}

	/*
	 * Load and display the accounts list.
	 */
	private void loadAccountList() {
		accountService.getAccounts(new AsyncCallback<AccountInfo[]>() {

			@Override
			public void onSuccess(AccountInfo[] result) {
				for (final AccountInfo a : result) {
					accounts.add(a);
					
					// Link to go to the statements list of that account
					Anchor link = new Anchor(a.getAccountName() + " (" + a.getRunningBalance() + " $)");
					link.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RootPanel.get("statementsList").clear();
							StatementsPanel statementsPanel = new StatementsPanel(loginInfo, a);
							RootPanel.get("statementsList").add(statementsPanel);
						}
					});
					mainPanel.add(link);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
		});
	}

	/*
	 * Add the new account to the list and display the link to its statements.
	 */
	private void addAccount(String name) {
		accountService.getAccountInfoByName(name, new AsyncCallback<AccountInfo>() {

					@Override
					public void onFailure(Throwable caught) {
						handleError(caught);
					}

					@Override
					public void onSuccess(final AccountInfo result) {
						accounts.add(result);
						Anchor link = new Anchor(result.getAccountName() + " (" + result.getRunningBalance() + " $)");
						link.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								RootPanel.get("statementsList").clear();
								StatementsPanel statementsPanel = new StatementsPanel(loginInfo, result);
								RootPanel.get("statementsList").add(statementsPanel);
							}
						});
						mainPanel.add(link);
					}
				});
	}

	/*
	 * Construct the addAccountPanel
	 */
	private void loadAddAccountPanel() {

		// TODO create new account without file (just name and opening balance)

		addPanel.add(csvFileLabel);
		csvFileUpload.setName("csvFile");
		addPanel.add(csvFileUpload);

		// Button to add a new account from the uploaded csv file
		addAccountButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				formPanel.submit();
			}
		});
		addPanel.add(addAccountButton);

		formPanel.setAction(GWT.getModuleBaseURL() + "uploadfile");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setWidget(addPanel);


		// Add an event handler to the form.
		formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can take this opportunity to perform validation.
				String filename = csvFileUpload.getFilename();
				if (filename.length() == 0 || !filename.endsWith(".csv")) {
					Window.alert("Please upload a CSV file to initialize your data account.");
					event.cancel();
				}
			}
		});
		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
					public void onSubmitComplete(SubmitCompleteEvent event) {
						// When the form submission is successfully completed, this event is fired.
						String newAccountName = event.getResults().split(">")[1].split("<")[0]; // Result gives the account name between <pre></pre>
						addAccount(newAccountName);
					}
				});

		mainPanel.add(formPanel);
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}
