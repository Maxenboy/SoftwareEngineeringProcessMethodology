import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public class ManagerGUI {
	private Database database;
	private User[] users;
	private UserList userList;
	private BikeList bikeList;

	public ManagerGUI(Database database) {
		this.database = database;
		JFrame frame = new JFrame("Bicycle Garage");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.add(panel, BorderLayout.WEST);

		userList = new UserList(20);
		userList.update();

		JScrollPane userListPane = new JScrollPane(userList);
		panel.add(userListPane, BorderLayout.WEST);

		bikeList = new BikeList(20);
		JScrollPane bikeListPane = new JScrollPane(bikeList);
		panel.add(bikeListPane, BorderLayout.EAST);

		JPanel buttonPanel = new JPanel(); // Buttons
		buttonPanel.setLayout(new GridLayout(0, 5));
		frame.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(new addUserButton());
		buttonPanel.add(new removeUserButton());
		buttonPanel.add(new editUserButton());
		buttonPanel.add(new addBicycleButton());
		buttonPanel.add(new removeBicycleButton());

		JMenuBar menubar = new JMenuBar(); // menu
		frame.setJMenuBar(menubar);
		JMenu statMenu = new JMenu("Statistics");
		statMenu.add(new statItem(database));
		menubar.add(statMenu);
		JMenu adminMenu = new JMenu("Administration");
		menubar.add(adminMenu);

		frame.pack();

		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Put the window in center
		int w = frame.getSize().width;
		int h = frame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		frame.setLocation(x, y);

		frame.setVisible(true);
	}

	public class UserList extends List implements ItemListener {
		public UserList(int rows) {
			super(rows, false);
			addItemListener(this);
		}

		public void update() {
			removeAll();
			users = database.userList();
			for (int i = 0; i < users.length; i++) {
				String name = users[i].getName();
				int ID = users[i].getID();
				if (ID < 10) {
					add("00" + ID + "   " + name);
				} else if (ID < 100) {
					add("0" + ID + "   " + name);
				} else {
					add(ID + "   " + name);
				}
			}
		}

		public void itemStateChanged(ItemEvent e) {
			User user = users[getSelectedIndex()];
			Bicycle[] bikes = database.bicycleList(user);
			for (int i = 0; i < bikes.length; i++) {
				System.out.print(bikes[i] + "\n");
			}
			bikeList.update(bikes);
		}
	}

	public class BikeList extends List {
		public BikeList(int rows) {
			super(rows, false);
		}

		public void update(Bicycle[] bikes) {
			this.removeAll();
			for (int i = 0; i < bikes.length; i++) {
				if (bikes[i] != null) {
					add(bikes[i].getID());
				}
			}
		}
	}

	private class statItem extends JMenuItem implements ActionListener {

		Database database;

		public statItem(Database database) {
			super("General statistics");
			this.database = database;
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			User[] usrs = (User[]) database.userList();
			JOptionPane.showMessageDialog(null, "Total number of users: "
					+ usrs.length + "\nTotal number of bicycles: "
					+ database.bicycleList().length
					+ "\nBicycles currently in the garage: "
					+ database.getNbrBikesInside()
					+ "\nMaximum number of bicycles per user: "
					+ database.getMaxNbrBikesPerUser()
					+ "\nMaximum number of bicycles in garage: "
					+ database.getMaxNbrBikes());
		}
	}

	public class addUserButton extends JButton implements ActionListener {
		public addUserButton() {
			super("Add user");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog("Name");
			String PIN = JOptionPane.showInputDialog("PIN code");
			database.addUser(name, PIN);
			userList.update();
		}
	}

	public class removeUserButton extends JButton implements ActionListener {
		public removeUserButton() {
			super("Remove user");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			User user = users[userList.getSelectedIndex()];
			database.removeUser(user.getID());
			userList.update();
		}
	}

	public class editUserButton extends JButton implements ActionListener {
		public editUserButton() {
			super("Edit user");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog("New name: ");
			String PIN = JOptionPane.showInputDialog("New PIN code: ");
			User user = users[userList.getSelectedIndex()];
			user.setName(name);
			user.setPIN(PIN);
		}
	}

	public class addBicycleButton extends JButton implements ActionListener {
		public addBicycleButton() {
			super("Add bicycle");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			User user = users[userList.getSelectedIndex()];
			database.addBicycle(user.getID());
		}
	}

	public class removeBicycleButton extends JButton implements ActionListener {
		public removeBicycleButton() {
			super("Remove bicycle");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			User user = users[userList.getSelectedIndex()];
			Bicycle[] bikes = database.bicycleList(user);
			database.removeBicycle(bikes[bikeList.getSelectedIndex()].getID());
		}
	}
}