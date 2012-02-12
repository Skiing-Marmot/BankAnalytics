package bankanalytics.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import bankanalytics.client.NotLoggedInException;
import bankanalytics.client.TransactionLineInfo;
import bankanalytics.client.TransactionLineService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TransactionLineServiceImpl extends RemoteServiceServlet implements
		TransactionLineService {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger
			.getLogger(TransactionLineServiceImpl.class.getName());

	public TransactionLineInfo addTransactionLine(String description,
			String categoryName, double amount) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TransactionLine newTransaction = new TransactionLine(description,
				amount, categoryName);
		try {
			pm.makePersistent(newTransaction);
		} finally {
			pm.close();
		}
		// TODO manage balance
		return new TransactionLineInfo(newTransaction.getId(), newTransaction.getAddDate(), description, amount, categoryName, newTransaction.getLineBalance());
	}

	public void removeTransactionLine(long id) throws NotLoggedInException { // TODO update the other lines when one is removed
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(TransactionLine.class);
		    query.setFilter("id == idParam");
		    query.declareParameters("long idParam");
			query.deletePersistentAll(id);
		} finally {
			pm.close();
		}
	}

	public TransactionLineInfo[] getTransactionLine() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query q = pm.newQuery(TransactionLine.class);
			q.setOrdering("addDate");
			List<TransactionLine> transactions = (List<TransactionLine>) q.execute();
			TransactionLineInfo[] transactionsList = new TransactionLineInfo[transactions.size()];
			for(int i=0; i<transactions.size(); i++) {
				TransactionLine transaction = transactions.get(i);
				transactionsList[i] = new TransactionLineInfo(transaction.getId(), transaction.getAddDate(), transaction.getDescription(), transaction.getAmount(), transaction.getCategory(), transaction.getLineBalance());
			}
			int length = transactionsList.length;
			if(length>0) {
				TransactionLine.setCurrentRunningBalance(transactionsList[length-1].getLineBalance());
			}
			return transactionsList;
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