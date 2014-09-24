import java.util.ArrayList;


public class Database {
	User[] users;
	Bicycle[] bikes;
	
	public Database() {
		users = new User[2];
		users[0] = new User("Max Leander", "1234", 0001);
		users[1] = new User("Sebastian Fabian", "9876", 0002);
		bikes = new Bicycle[5];
		bikes[0] = new Bicycle(users[0], "12345");
		bikes[1] = new Bicycle(users[0], "12346");
		bikes[2] = new Bicycle(users[0], "12347");
		
		bikes[3] = new Bicycle(users[1], "98761");
		bikes[4] = new Bicycle(users[1], "98769");
	}
	
	public Bicycle[] bicycleList(User user) {
		ArrayList<Bicycle> bikesTemp = new ArrayList<Bicycle>(); 
		for (int i = 0; i < bikes.length; i++) {
			if (bikes[i].getOwner().equals(user)) {
				bikesTemp.add(bikes[i]);
			}
		}
		return bikesTemp.toArray(bikes);
	}
	
	public Bicycle[] bicycleList() {
		return bikes;
	}

	public int getNbrBikesInside() {
		return 1;
	}

	public int getMaxNbrBikesPerUser() {
		return 4;
	}

	public int getMaxNbrBikes() {
		return 300;
	}

	public void addUser(String name, String PIN) {

	}
	
	public User getUser(String name) {
		return null;
		
	}

	public boolean removeUser(int ID) {
		return true;
	}

	public User[] userList() {
		return users;
	}

	public Bicycle[] BicyclesInsideList() {
		Bicycle[] hej = {new Bicycle(users[0], "12346"), new Bicycle(users[0], "12347")};
		return hej;
	}

	public void setMaxNbrBikes(int global) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxNbrBikesPerUser(int user) {
		// TODO Auto-generated method stub
		
	}

	public void addBicycle(int ID) {
		// TODO Auto-generated method stub
		
	}

	public void removeBicycle(String id) {
		// TODO Auto-generated method stub
		
	}

}
