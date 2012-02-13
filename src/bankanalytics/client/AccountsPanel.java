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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AccountsPanel extends Composite {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FormPanel formPanel = new FormPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private Label newNameLabel = new Label("Account name: ");
	private TextBox newNameTextBox = new TextBox();
	private Label csvFileLabel = new Label("CSV File to upload: ");
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
	
	private void loadAccountList() {
		accountService.getAccounts(new AsyncCallback<AccountInfo[]>() {
			
			@Override
			public void onSuccess(AccountInfo[] result) {
				for(AccountInfo a : result) {
					accounts.add(a);
					Anchor link = new Anchor(a.getAccountName() + "(" + a.getRunningBalance() + ")");
					if(mainPanel.getWidgetIndex(link) == -1) {
						mainPanel.add(link);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
		});
	}
	
	private void loadAddAccountPanel() {
		addPanel.add(newNameLabel);
		newNameTextBox.setName("newName");
		addPanel.add(newNameTextBox);
		addPanel.add(csvFileLabel);
		csvFileUpload.setName("csvFile");
		addPanel.add(csvFileUpload);
		
		addAccountButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		          String filename = csvFileUpload.getFilename();
		          String accountName = newNameTextBox.getText();
		          if (filename.length() == 0 || !filename.endsWith(".csv") || accountName.isEmpty()) {
		            Window.alert("Please upload a CSV file to initialize your data account and fill the new account name. " + filename);
		          } else {
		            // TODO upload and parse file
		        	  formPanel.submit();
		        	  loadAccountList();
		          }
		        }
		      });
		addPanel.add(addAccountButton);
		
		formPanel.setAction(GWT.getModuleBaseURL() + "uploadfile");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	    formPanel.setMethod(FormPanel.METHOD_POST);
	    formPanel.setWidget(addPanel);
	    
	    
//	 // Add a 'submit' button.
//	    formPanel.add(new Button("Create Account", new ClickHandler() {
//	      public void onClick(ClickEvent event) {
//	        formPanel.submit();
//	      }
//	    }));
//
//	    // Add an event handler to the form.
//	    formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
//	      public void onSubmit(SubmitEvent event) {
//	        // This event is fired just before the form is submitted. We can take
//	        // this opportunity to perform validation.
//	    	  String filename = csvFileUpload.getFilename();
//	          String accountName = newNameTextBox.getText();
//	          if (filename.length() == 0 || !filename.endsWith(".csv") || !accountName.isEmpty()) {
//	            Window.alert("Please upload a CSV file to initialize your data account and fill the new account name.");
//	          event.cancel();
//	        }
//	      }
//	    });
//	    formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
//	      public void onSubmitComplete(SubmitCompleteEvent event) {
//	        // When the form submission is successfully completed, this event is
//	        // fired. Assuming the service returned a response of type text/html,
//	        // we can get the result text here (see the FormPanel documentation for
//	        // further explanation).
//	        Window.alert(event.getResults());
//	      }
//	    });
	    
		
		
		mainPanel.add(formPanel);
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}