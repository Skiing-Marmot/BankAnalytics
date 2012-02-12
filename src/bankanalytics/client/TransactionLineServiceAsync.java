package bankanalytics.client;

import bankanalytics.server.TransactionLine;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TransactionLineServiceAsync {

	void addTransactionLine(String description, String categoryName, double amount, AsyncCallback<TransactionLineInfo> callback);

	void getTransactionLine(AsyncCallback<TransactionLineInfo[]> callback);

	void removeTransactionLine(long id, AsyncCallback<Void> callback);

}
