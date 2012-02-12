package bankanalytics.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("transaction")
public interface TransactionLineService extends RemoteService {
  public TransactionLineInfo addTransactionLine(String description, String categoryName, double amount) throws NotLoggedInException;
  public void removeTransactionLine(long id) throws NotLoggedInException;
  public TransactionLineInfo[] getTransactionLine() throws NotLoggedInException;
}