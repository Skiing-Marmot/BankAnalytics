package bankanalytics.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

public class StatementsByCategoryDialog extends DialogBox {
	public StatementsByCategoryDialog() {
	      // Set the dialog box's caption.
	      setText("Statements by category");

	      // Enable animation.
	      setAnimationEnabled(true);

	      // Enable glass background.
	      setGlassEnabled(true);

	      // DialogBox is a SimplePanel, so you have to set its widget property to
	      // whatever you want its contents to be.
	      Button ok = new Button("OK");
	      ok.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          StatementsByCategoryDialog.this.hide();
	        }
	      });
	      setWidget(ok);
	    }
}
