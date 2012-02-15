package bankanalytics.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceRelativePath("account")
public interface AccountService extends RemoteService {
	
	public AccountInfo[] getAccounts() throws NotLoggedInException;

	public AccountInfo getAccountInfoByName(String name) throws NotLoggedInException;

	String addAccount(String name) throws NotLoggedInException;

	public void updateAccount(long id, String name) throws NotLoggedInException;

	public AccountInfo removeAccount(long id) throws NotLoggedInException;

	public TransactionLineInfo[] getTransactions(AccountInfo account) throws NotLoggedInException;

	public TransactionLineInfo addTransaction(AccountInfo account, Date date, String description, String categoryName, double amount) throws NotLoggedInException;

	public void updateTransaction(long id, Date date, String description, String categoryName, double amount) throws NotLoggedInException;

	public TransactionLineInfo removeTransaction(long id) throws NotLoggedInException;

	public CategoryInfo[] getCategories(AccountInfo account) throws NotLoggedInException;

	public CategoryInfo addCategory(AccountInfo account, String name, String color) throws NotLoggedInException;

	void updateCategory(String string, String name, String color) throws NotLoggedInException;

	void removeCategory(String string) throws NotLoggedInException;

}
