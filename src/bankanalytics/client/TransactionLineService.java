package bankanalytics.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("transaction")
public interface TransactionLineService extends RemoteService {
  public void addTransactionLine(String symbol) throws NotLoggedInException;
  public void removeTransactionLine(String symbol) throws NotLoggedInException;
  public String[] getTransactionLine() throws NotLoggedInException;
}