package bankanalytics.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CategoryManagerDialog extends DialogBox {

	private VerticalPanel panel = new VerticalPanel();
	private Label soonLabel = new Label("Coming Soon");
	private Button ok = new Button("OK");
	private FlexTable categoriesFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private Label nameLabel = new Label("Category name");
	private TextBox nameTextBox = new TextBox();
	private Label colorLabel = new Label("Category color");
	private TextBox colorTextBox = new TextBox();
	private Button addButton = new Button("Add category");

	private AccountServiceAsync accountService = GWT
			.create(AccountService.class);

	public CategoryManagerDialog(final AccountInfo accountInfo) {
		// Set the dialog box's caption.
		setText("Categories");

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		this.setSize("80%", "300px");
	      panel.setSize("400px", "400px");

		loadCategories(accountInfo);

		addPanel.add(nameLabel);
		addPanel.add(nameTextBox);
		addPanel.add(colorLabel);
		addPanel.add(colorTextBox);

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addCategory(accountInfo);
			}
		});
		addPanel.add(addButton);

		// DialogBox is a SimplePanel, so you have to set its widget property to
		// whatever you want its contents to be.
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CategoryManagerDialog.this.hide();
			}
		});
		
		panel.add(categoriesFlexTable);

		panel.add(addPanel);

		panel.add(ok);

		setWidget(panel);
	}

	protected void addCategory(AccountInfo account) {
		// Get name
		final String name = nameTextBox.getText().trim();
		nameTextBox.setText("");
		nameTextBox.setFocus(true);
		// Get color
		final String color = colorTextBox.getText().trim();
		colorTextBox.setText("");

		accountService.addCategory(account, name, color,
				new AsyncCallback<CategoryInfo>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(CategoryInfo result) {
						if (result != null) {
							int rowNum = categoriesFlexTable.getRowCount();
							categoriesFlexTable.setText(rowNum, 0,
									result.getCategoryName());
							categoriesFlexTable.setText(rowNum, 1,
									result.getColor());
						}
					}
				});
		
		// TODO update listbox

	}

	private void loadCategories(AccountInfo accountInfo) {
		// Set up the header row of the table.
		categoriesFlexTable.setText(0, 0, "Category name");
		categoriesFlexTable.setText(0, 1, "Highligthing color");

		// Add styles to the statements flex table.
		categoriesFlexTable.getRowFormatter().addStyleName(0,
				"statementsTableHeader");
		categoriesFlexTable.addStyleName("statementsTable");

		// Create table for statements line

		accountService.getCategories(accountInfo,
				new AsyncCallback<CategoryInfo[]>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO
					}

					@Override
					public void onSuccess(CategoryInfo[] result) {
						if (result != null) {
							for (int i = 0; i < result.length; i++) {
								int rowNum = categoriesFlexTable.getRowCount();
								categoriesFlexTable.setText(rowNum, 0,
										result[i].getCategoryName());
								categoriesFlexTable.setText(rowNum, 1,
										result[i].getColor());
							}
						}
					}
				});
	}
}
