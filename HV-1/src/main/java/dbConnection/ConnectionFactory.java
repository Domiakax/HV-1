package dbConnection;

import dev.hv.db.init.IConnection;
import dev.hv.db.init.IConnectionFactory;

public class ConnectionFactory implements IConnectionFactory{

	@Override
	public IConnection createConnection(String url, String user, String pw) {
		return Connection.getConnection().openConnection(url, user, pw);
	}
		

//	@Override
//	public IDatabaseConnection createConnection(String url, String user, String password) {
//		return DatabaseConnection.getDatabaseConnection(url, user, password);
//	}

}
