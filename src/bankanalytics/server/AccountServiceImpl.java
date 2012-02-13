package bankanalytics.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import bankanalytics.client.AccountInfo;
import bankanalytics.client.AccountService;
import bankanalytics.client.CategoryInfo;
import bankanalytics.client.NotLoggedInException;
import bankanalytics.client.TransactionLineInfo;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AccountServiceImpl extends RemoteServiceServlet implements
		AccountService {

	private static final long serialVersionUID = 1L;

	@Override
	public AccountInfo[] getAccounts() throws NotLoggedInException {
		
			checkLoggedIn();
			System.out.println("Check login: " + getUser().getEmail());
		    PersistenceManager pm = PMF.get().getPersistenceManager();
			AccountInfo[] accounts = null;
		    try {
		    	Query query = pm.newQuery(Account.class);
				query.setFilter("user == u");
				query.declareParameters("com.google.appengine.api.users.User u");
				query.setOrdering("accountName");
				List<Account> result = (List<Account>) query.execute(getUser());
				int size = result.size();
				if(size > 0) {
					accounts = new AccountInfo[size];
					for(int i=0; i<size; i++) {
						accounts[i] = result.get(i).getAccountInfo();
					}
				}
				
		    } finally {
		      pm.close();
		    }
		    return accounts;
		
	}
	
private Account getAccountByName(String name) throws NotLoggedInException {
		
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Account.class);
		query.setFilter("user == u && accountName == name");
		query.declareParameters("com.google.appengine.api.users.User u, String name");
		query.setOrdering("accountName");
		List<Account> result = (List<Account>) query.execute(getUser(), name);
		if(result.size()>0) {
			return result.get(0);
		}
		
		return null;
		
	}
	
	public AccountInfo getAccountInfoByName(String name) throws NotLoggedInException {
		
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Account.class);
		query.setFilter("user == u && accountName == name");
		query.declareParameters("com.google.appengine.api.users.User u, String name");
		query.setOrdering("accountName");
		List<Account> result = (List<Account>) query.execute(getUser(), name);
		if(result.size()>0) {
			return result.get(0).getAccountInfo();
		}
		
		return null;
		
	}

	@Override
	public String addAccount(String name) throws NotLoggedInException {
		
		checkLoggedIn();
		System.out.println("Check login: " + getUser().getEmail());
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Account account = null;
	    
		if(getAccountByName(name) == null) {
		    account = new Account(name, getUser());
	    try {
	      pm.makePersistent(account);
	    } finally {
	      pm.close();
	    }
	    return account.getAccountName();
		}
	      return null;
	}

	@Override
	public void updateAccount(long id, String name) throws NotLoggedInException {
		// TODO Auto-generated method stub

	}

	@Override
	public AccountInfo removeAccount(long id) throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionLineInfo[] getTransactions(AccountInfo account) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TransactionLineInfo[] transactions = null;
		
		Account a = getAccountByName(account.getAccountName());
		
		List<TransactionLine> transactionsList = a.getStatements();
		
		if(transactionsList.size()>0) {
			transactions = new TransactionLineInfo[transactionsList.size()];
			for(int i=0; i<transactionsList.size(); i++) {
				transactions[i] = transactionsList.get(i).getTransactionLineInfo();
			}
		}
		
		return transactions;
	}

	@Override
	public TransactionLineInfo[] getTransactionsByCategory(AccountInfo account)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionLineInfo addTransaction(AccountInfo accountInfo, Date date,
			String description, String categoryName, double amount)
			throws NotLoggedInException {		
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Account account = getAccountByName(accountInfo.getAccountName());
		
		//Long id = (Long) pm.getObjectId(account);
		Account updatedAccount = pm.getObjectById(Account.class, account.getId());
		TransactionLine tl = new TransactionLine(date, description, categoryName, amount, updatedAccount.getRunningBalance()+amount);
		updatedAccount.addStatement(tl);
		updatedAccount.setRunningBalance(tl.getLineBalance());
		pm.close();
		
		
		return tl.getTransactionLineInfo();
	}
	
	public void addTransactionLine(TransactionLine transaction, String accountName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Account account = getAccountByName(accountName);
		
		//Long id = (Long) pm.getObjectId(account);
		Account updatedAccount = pm.getObjectById(Account.class, account.getId());
		updatedAccount.addStatement(transaction);
		updatedAccount.setRunningBalance(transaction.getLineBalance());
		pm.close();
		
	}

	@Override
	public void updateTransaction(long id, Date date, String description,
			String categoryName, double amount) throws NotLoggedInException {
		// TODO Auto-generated method stub

	}

	@Override
	public TransactionLineInfo removeTransaction(long id)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoryInfo[] getCategories(AccountInfo account)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoryInfo addCategory(AccountInfo account, String name, String color)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCategory(long id, String name, String color)
			throws NotLoggedInException {
		// TODO Auto-generated method stub

	}

	@Override
	public CategoryInfo removeCategory(long id) throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkLoggedIn() throws NotLoggedInException {
	    if (getUser() == null) {
	      throw new NotLoggedInException("Not logged in.");
	    }
	  }

	  private User getUser() {
	    UserService userService = UserServiceFactory.getUserService();
	    return userService.getCurrentUser();
	  }

}
