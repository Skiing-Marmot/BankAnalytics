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

/*
 * Dialog box displaying the account's categories. We can add or update categories.
 */
public class CategoryManagerDialog extends DialogBox {

	private VerticalPanel panel = new VerticalPanel();
	private Button ok = new Button("Close");
	private FlexTable categoriesFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private Label nameLabel = new Label("Category name");
	private TextBox nameTextBox = new TextBox();
	private Label colorLabel = new Label("Category color");
	private TextBox colorTextBox = new TextBox();
	private Button addButton = new Button("Add category");

	private AccountServiceAsync accountService = GWT.create(AccountService.class);
	private StatementsPanel statPanel;

	public CategoryManagerDialog(final AccountInfo accountInfo, StatementsPanel statPanel) {

		this.statPanel = statPanel;

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

		// Button to close the dialog box
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

	/*
	 * Add a new category.
	 */
	protected void addCategory(AccountInfo account) {
		// Get name
		final String name = nameTextBox.getText().trim();
		nameTextBox.setText("");
		nameTextBox.setFocus(true);
		// Get color
		final String color = colorTextBox.getText().trim();
		colorTextBox.setText("");

		accountService.addCategory(account, name, color, new AsyncCallback<CategoryInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(CategoryInfo result) {
				if (result != null) {
					displayCategory(result);
					statPanel.addCategory(result); // Add the new category to the listbox of the add transaction panel
				}
			}
		});
	}

	/*
	 * Load and display the account's categories
	 */
	private void loadCategories(AccountInfo accountInfo) {
		// Set up the header row of the table.
		categoriesFlexTable.setText(0, 0, "Category name");
		categoriesFlexTable.setText(0, 1, "Highligthing color");
		categoriesFlexTable.setText(0, 2, "Update");
//		categoriesFlexTable.setText(0, 3, "Delete");

		// Add styles to the statements flex table.
		categoriesFlexTable.getRowFormatter().addStyleName(0, "statementsTableHeader");
		categoriesFlexTable.addStyleName("statementsTable");

		accountService.getCategories(accountInfo, new AsyncCallback<CategoryInfo[]>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO
			}

			@Override
			public void onSuccess(CategoryInfo[] result) {
				if (result != null) {
					for (int i = 0; i < result.length; i++) {
						displayCategory(result[i]);
					}
				}
			}
		});
	}

	/*
	 * Display a line of the categories table
	 */
	private void displayCategory(final CategoryInfo category) {
		int rowNum = categoriesFlexTable.getRowCount();

		final TextBox nameTextBox = new TextBox();
		nameTextBox.setText(category.getCategoryName());
		final TextBox colorTextBox = new TextBox();
		colorTextBox.setText(category.getColor());
		categoriesFlexTable.setWidget(rowNum, 0, nameTextBox);
		categoriesFlexTable.setWidget(rowNum, 1, colorTextBox);
		
		// Add a button to update the category
		Button updateCategoryButton = new Button("OK");
		updateCategoryButton.addStyleDependentName("update");
		updateCategoryButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				updateCategory(category, nameTextBox, colorTextBox);
			}
		});
		categoriesFlexTable.setWidget(rowNum, 2, updateCategoryButton);

		// TODO Add a button to remove this category from the table.

		
	}
	
	/*
	 * Called when the updateCategoryButton is clicked. Update the category if name or color has been changed.
	 */
	private void updateCategory(CategoryInfo category, TextBox nameTextBox, TextBox colorTextBox) {
		if(!nameTextBox.getText().equals(category.getCategoryName()) || !colorTextBox.getText().equals(category.getColor())) {
			updateCategory(category, nameTextBox.getText(), colorTextBox.getText());
		}
	}
	
	/*
	 * Update the category in the table
	 */
	private void updateCategory(final CategoryInfo category, final String newName, final String newColor) {
		accountService.updateCategory(category.getId(), newName, newColor, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(Void result) {
				statPanel.updateCategory(category, newName, newColor);
			}
		});
	}
	
	private void removeCategory(final CategoryInfo category) {
		// TODO
	}
}
