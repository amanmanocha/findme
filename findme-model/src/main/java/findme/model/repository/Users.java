package findme.model.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sample.persistence.AddUserEvent;
import findme.model.User;

public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<User> users;
    
    public Users() {
    	users = new ArrayList<>();
    }

    public Users(List<User> users) {
        this.users = users;
    }

    public Users copy() {
        return new Users(new ArrayList<User>(users));
    }

    public int size() {
        return users.size();
    }

    @Override
    public String toString() {
        return users.toString();
    }

	public void update(AddUserEvent addUserEvent) {
		users.add(addUserEvent.getUser());
	}
}
