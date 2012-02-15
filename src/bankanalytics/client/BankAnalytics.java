package bankanalytics.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BankAnalytics implements EntryPoint {

	// UI components
	// User login
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access your bank account information.");
	private Anchor signInLink = new Anchor("Sign In");

	private LoginInfo loginInfo = null;

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
						if (loginInfo.isLoggedIn()) { // If user is logged in, we can display the data.
							loadAccountInformation();
						} else {
							loadLogin();
						}
					}
				});
	}

	/*
	 * Display the login link
	 */
	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("statementsList").add(loginPanel);
	}

	/*
	 * Display the accounts list panel
	 */
	private void loadAccountInformation() {

		AccountsPanel accountsPanel = new AccountsPanel(loginInfo);
		// We add the panel displaying the accounts list
		RootPanel.get("statementsList").add(accountsPanel);

	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}

}