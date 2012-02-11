package bankanalytics.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import bankanalytics.client.NotLoggedInException;
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

	public void addTransactionLine(String symbol) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(new TransactionLine());
		} finally {
			pm.close();
		}
	}

	public void removeTransactionLine(String description)
			throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(TransactionLine.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<TransactionLine> stocks = (List<TransactionLine>) q
					.execute(getUser());
			for (TransactionLine stock : stocks) {
				if (description.equals(stock.getDescription())) {
					deleteCount++;
					pm.deletePersistent(stock);
				}
			}
			if (deleteCount != 1) {
				LOG.log(Level.WARNING, "removeStock deleted " + deleteCount
						+ " Stocks");
			}
		} finally {
			pm.close();
		}
	}

	public String[] getTransactionLine() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<String> symbols = new ArrayList<String>();
		try {
			Query q = pm.newQuery(TransactionLine.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("createDate");
			List<TransactionLine> stocks = (List<TransactionLine>) q
					.execute(getUser());
			for (TransactionLine stock : stocks) {
				symbols.add(stock.getDescription());
			}
		} finally {
			pm.close();
		}
		return (String[]) symbols.toArray(new String[0]);
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