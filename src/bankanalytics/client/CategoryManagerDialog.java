package bankanalytics.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

public class CategoryManagerDialog extends DialogBox {
	public CategoryManagerDialog() {
	      // Set the dialog box's caption.
	      setText("Categories");

	      // Enable animation.
	      setAnimationEnabled(true);

	      // Enable glass background.
	      setGlassEnabled(true);

	      // DialogBox is a SimplePanel, so you have to set its widget property to
	      // whatever you want its contents to be.
	      Button ok = new Button("OK");
	      ok.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          CategoryManagerDialog.this.hide();
	        }
	      });
	      setWidget(ok);
	    }
}
