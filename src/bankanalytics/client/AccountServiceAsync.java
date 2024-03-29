package bankanalytics.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AccountServiceAsync {

	void addAccount(String name, AsyncCallback<String> callback);

	void addCategory(AccountInfo account, String name, String color, AsyncCallback<CategoryInfo> callback);

	void addTransaction(AccountInfo account, Date date, String description, String categoryName, double amount, AsyncCallback<TransactionLineInfo> callback);

	void getAccountInfoByName(String name, AsyncCallback<AccountInfo> callback);

	void getAccounts(AsyncCallback<AccountInfo[]> callback);

	void getCategories(AccountInfo account, AsyncCallback<CategoryInfo[]> callback);

	void getTransactions(AccountInfo account, AsyncCallback<TransactionLineInfo[]> callback);

	void removeAccount(long id, AsyncCallback<AccountInfo> callback);

	void removeCategory(String string, AsyncCallback<Void> asyncCallback);

	void removeTransaction(long id, AsyncCallback<TransactionLineInfo> callback);

	void updateAccount(long id, String name, AsyncCallback<Void> callback);

	void updateCategory(String id, String newName, String newColor, AsyncCallback<Void> asyncCallback);

	void updateTransaction(long id, Date date, String description, String categoryName, double amount, AsyncCallback<Void> callback);

}
