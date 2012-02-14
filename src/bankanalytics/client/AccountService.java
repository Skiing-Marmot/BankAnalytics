package bankanalytics.client;

import java.util.Date;
import java.util.List;

import bankanalytics.server.Account;
import bankanalytics.server.Category;
import bankanalytics.server.TransactionLine;

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
	TransactionLineInfo[] getTransactionsByCategory(AccountInfo account,
			String categoryName) throws NotLoggedInException;
	public TransactionLineInfo addTransaction(AccountInfo account, Date date, String description, String categoryName, double amount) throws NotLoggedInException;
	public void updateTransaction(long id, Date date, String description, String categoryName, double amount) throws NotLoggedInException;
	public TransactionLineInfo removeTransaction(long id) throws NotLoggedInException;
	
	public CategoryInfo[] getCategories(AccountInfo account) throws NotLoggedInException;
	public CategoryInfo addCategory(AccountInfo account, String name, String color) throws NotLoggedInException;
	public void updateCategory(long id, String name, String color) throws NotLoggedInException;
	public CategoryInfo removeCategory(long id) throws NotLoggedInException;
	
}
