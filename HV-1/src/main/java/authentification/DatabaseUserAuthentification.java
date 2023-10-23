package authentification;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import dao.UserDAO;
import model.User;

public class DatabaseUserAuthentification {
	
	public static final String urlDatabase = "";
	public static final String dbUser = "root";
	public static final String dbUserPW = "";

	private static DatabaseUserAuthentification userAuthentification;
	private static UserDAO userDAO;
	
	public static DatabaseUserAuthentification getDatabaseUserAuthentification() {
		if(userAuthentification == null) {
			userAuthentification = new DatabaseUserAuthentification();
		}
		return userAuthentification;
	}
	
	private DatabaseUserAuthentification() {
		final Jdbi jdbi  = Jdbi.create(urlDatabase, dbUser, dbUserPW);
		jdbi.installPlugin(new SqlObjectPlugin());
		final Handle handle = jdbi.open();
		handle.registerRowMapper(BeanMapper.factory(User.class));
		userDAO = handle.attach(UserDAO.class);
		System.out.println("User added to jdbi");
	}
	
	public boolean createUser(User u) {
		userDAO.createUser(u);
		return true;
	}
	
	public String getPassword(String username) {
		return userDAO.getPassword(username);
	}
	
}
