package bankanalytics.server;

import java.util.Collections;
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

public class AccountServiceImpl extends RemoteServiceServlet implements AccountService {

	private static final long serialVersionUID = 1L;

	/*
	 * Return all the accounts for the logged in user
	 */
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
			if (size > 0) {
				accounts = new AccountInfo[size];
				for (int i = 0; i < size; i++) {
					accounts[i] = result.get(i).getAccountInfo();
				}
			}

		} finally {
			pm.close();
		}
		return accounts;

	}

	/*
	 * Return the account whose name is name
	 */
	private Account getAccountByName(String name) throws NotLoggedInException {

		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Account.class);
		query.setFilter("user == u && accountName == name");
		query.declareParameters("com.google.appengine.api.users.User u, String name");
		query.setOrdering("accountName");
		List<Account> result = (List<Account>) query.execute(getUser(), name);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/*
	 * Return the AccountInfo corresponding to the account whose name is name
	 */
	public AccountInfo getAccountInfoByName(String name) throws NotLoggedInException {

		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Account.class);
		query.setFilter("user == u && accountName == name");
		query.declareParameters("com.google.appengine.api.users.User u, String name");
		query.setOrdering("accountName");
		List<Account> result = (List<Account>) query.execute(getUser(), name);
		if (result.size() > 0) {
			return result.get(0).getAccountInfo();
		}

		return null;

	}

	/*
	 * Add a new account to the base
	 */
	@Override
	public String addAccount(String name) throws NotLoggedInException {

		checkLoggedIn();
		System.out.println("Check login: " + getUser().getEmail());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Account account = null;

		if (getAccountByName(name) == null) {
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

	/*
	 * Return all the transactions of an account
	 */
	@Override
	public TransactionLineInfo[] getTransactions(AccountInfo account) throws NotLoggedInException {
		checkLoggedIn();

		TransactionLineInfo[] transactions = null;

		Account a = getAccountByName(account.getAccountName());

		List<TransactionLine> transactionsList = a.getStatements();
		Collections.sort(transactionsList);
		if (transactionsList.size() > 0) {
			transactions = new TransactionLineInfo[transactionsList.size()];
			for (int i = 0; i < transactionsList.size(); i++) {
				transactions[i] = transactionsList.get(i).getTransactionLineInfo();
			}
		}
		return transactions;
	}

	/*
	 * Add a new transaction
	 */
	@Override
	public TransactionLineInfo addTransaction(AccountInfo accountInfo, Date date, String description, String categoryName, double amount) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Account account = getAccountByName(accountInfo.getAccountName());
		Category category = getCategoryByName(categoryName, account);

		Account updatedAccount = pm.getObjectById(Account.class, account.getId());
		TransactionLine tl = new TransactionLine(date, description, categoryName, amount);
		updatedAccount.addToRunningBalance(amount);
		updatedAccount.addStatement(tl);
		Category updatedCategory = pm.getObjectById(Category.class, category.getId());
		updatedCategory.addAmountToSum(amount);
		pm.close();

		return tl.getTransactionLineInfo();
	}

	/*
	 * Add the transaction to the account
	 */
	public void addTransactionLine(TransactionLine transaction, String accountName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Account account = getAccountByName(accountName);
		Category category = getCategoryByName(transaction.getCategoryName(), account);

		Account updatedAccount = pm.getObjectById(Account.class, account.getId());
		updatedAccount.addToRunningBalance(transaction.getAmount());
		updatedAccount.addStatement(transaction);
		Category updatedCategory = pm.getObjectById(Category.class, category.getId());
		updatedCategory.addAmountToSum(transaction.getAmount());
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

	/*
	 * Return all the categories of an account
	 */
	@Override
	public CategoryInfo[] getCategories(AccountInfo account) throws NotLoggedInException {

		checkLoggedIn();
		CategoryInfo[] categories = null;

		Account a = getAccountByName(account.getAccountName());

		List<Category> categoriesList = a.getCategories();

		if (categoriesList.size() > 0) {
			categories = new CategoryInfo[categoriesList.size()];
			for (int i = 0; i < categoriesList.size(); i++) {
				categories[i] = categoriesList.get(i).getCategoryInfo();
			}
		}

		return categories;

	}

	/*
	 * Return the category whose name is name
	 */
	private Category getCategoryByName(String name, Account account) throws NotLoggedInException {

		checkLoggedIn();

		List<Category> categories = account.getCategories();

		for (Category c : categories) {
			if (c.getCategoryName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	/*
	 * Add a new category
	 */
	@Override
	public CategoryInfo addCategory(AccountInfo accountInfo, String name, String color) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Account account = getAccountByName(accountInfo.getAccountName());
		Account updatedAccount = pm.getObjectById(Account.class,
				account.getId());
		if (!updatedAccount.isInCategories(name)) {
			Category category = new Category(name, color);
			updatedAccount.addCategory(category);
			return category.getCategoryInfo();
		}
		return null;
	}

	/*
	 * Add the category to the table
	 */
	public void addCategory(String categoryName, String accountName)
			throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Account account = getAccountByName(accountName);

		Account updatedAccount = pm.getObjectById(Account.class, account.getId());

		if (!updatedAccount.isInCategories(categoryName)) {
			updatedAccount.addCategory(new Category(categoryName, "white"));
		}
		pm.close();
	}

	@Override
	public void updateCategory(String id, String name, String color) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Category.class);
			query.setFilter("id == idParam");
			query.declareParameters("String idParam");
			List<Category> list = (List<Category>) query.execute(id);
			if (!list.isEmpty()) {
				Category updatedCategory = list.get(0);
				updatedCategory.setCategoryName(name);
				updatedCategory.setColor(color);
			}
		} finally {
			pm.close();
		}
	}

	@Override
	public void removeCategory(String id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		// TODO Manage transactions which were in that category

		try {
			Query query = pm.newQuery(Category.class);
			query.setFilter("id == idParam");
			query.declareParameters("String idParam");
			query.deletePersistentAll(id);
		} finally {
			pm.close();
		}
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
