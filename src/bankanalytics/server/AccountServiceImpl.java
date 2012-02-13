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
	
	// TODO getAccountByName

	@Override
	public AccountInfo addAccount(String name, double initialBalance) throws NotLoggedInException {
		
		checkLoggedIn();
		System.out.println("Check login: " + getUser().getEmail());
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Account account = new Account(name, getUser(), initialBalance);
	    try {
	      pm.makePersistent(account);
	    } finally {
	      pm.close();
	    }
	      return account.getAccountInfo();
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
	public TransactionLineInfo[] getTransactions(AccountInfo account)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionLineInfo[] getTransactionsByCategory(AccountInfo account)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionLineInfo addTransaction(AccountInfo account, Date date,
			String description, String categoryName, double amount)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
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
