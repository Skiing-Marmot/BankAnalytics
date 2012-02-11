package bankanalytics.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TransactionLineServiceAsync {

	void addTransactionLine(String symbol, AsyncCallback<Void> callback);

	void getTransactionLine(AsyncCallback<String[]> callback);

	void removeTransactionLine(String symbol, AsyncCallback<Void> callback);

}
