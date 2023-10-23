package dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import model.User;

public interface UserDAO {
	
	@SqlUpdate("""
			Insert into User(name, password) values(:username, :password)
			""")
	void createUser(@BindBean User u);
	
	@SqlQuery("""
			Select password from user where name = :uname
			""")
	String getPassword(@Bind("uname") String username);

}
