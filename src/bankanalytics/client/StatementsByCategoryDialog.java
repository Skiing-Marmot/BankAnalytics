package bankanalytics.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatementsByCategoryDialog extends DialogBox {
	
	private VerticalPanel panel = new VerticalPanel();
	private Label soonLabel = new Label("Coming Soon");
    private Button ok = new Button("OK");
    
	private final AccountServiceAsync accountService = GWT.create(AccountService.class);
	
	public StatementsByCategoryDialog(AccountInfo account) {
	      // Set the dialog box's caption.
	      setText("Statements by category");

	      // Enable animation.
	      setAnimationEnabled(true);

	      // Enable glass background.
	      setGlassEnabled(true);
	      
	      this.setSize("80%", "300px");
	      //this.setPopupPosition(getOffsetWidth(), getOffsetHeight());
	      panel.setSize("400px", "400px");
	      
	     // mainPanel.add(soonLabel);
	      
	      loadCategorySums(account);

	      // DialogBox is a SimplePanel, so you have to set its widget property to
	      // whatever you want its contents to be.
	      ok.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          StatementsByCategoryDialog.this.hide();
	        }
	      });
	      
	      panel.add(ok);
	      
	      setWidget(panel);
	    }
	
	private void loadCategorySums(AccountInfo accountInfo) {
		
		accountService.getCategories(accountInfo, new AsyncCallback<CategoryInfo[]>() {
			
			@Override
			public void onSuccess(CategoryInfo[] result) {
				if(result != null) {
					for(int i=0; i<result.length; i++) {
						Label lab = new Label(result[i].getCategoryName() + ": " + result[i].getSum() + " $");
						panel.add(lab);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
