package bicycleGarage;
import interfaces.BarcodePrinter;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
* Manages data
*/
public class Database implements Serializable {
        
        private BarcodePrinter printer;
        private TreeMap<User, ArrayList<Bicycle>> map;
        private int[] globalValues;
        
        private static final int SLOT_MAX_BIKES = 0;
        private static final int SLOT_BIKES_PER_USER = 1;
        private static final int SLOT_LAST_USER_ID = 2;
        private static final int SLOT_NEXT_BIKE_ID = 3;
        private static final int MAX_NBR_OF_USERS = 500;
        
        public static final int SUCCESS = 0;
        public static final int GLOBAL_BIKE_LIMIT_REACHED = 1;
        public static final int USER_BIKE_LIMIT_REACHED = 2;

        private static final  Object[] opts = {"Yes", "No"};
        private static final int YES = 0;
        private static final int NO = 1;
         //Choices for dialogs
        
        private final boolean MAX_LUKTAR = true;
        
        /**
         * Creates a new database and loads stored data from a storage device
         * 
         * @param manager
         *            The bicycle garage manager managing the system
         */
        public Database(BarcodePrinter printer) {
                this.printer = printer;
                try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                                        new File("DB")));
                        map = (TreeMap<User, ArrayList<Bicycle>>) in.readObject();
                        in.close();
                        in = new ObjectInputStream(new FileInputStream(new File("GV")));
                        globalValues = (int[]) in.readObject();
                        in.close();
                } catch (Exception e) {
                        // Failure to retrieve newest database.
                                loadBkp();
                }
        }
        
        private void loadBkp() {
                try {
                        // look for bkp instead
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                                        new File("DB.bkp")));
                        map = (TreeMap<User, ArrayList<Bicycle>>) in.readObject();
                        in.close();
                        in = new ObjectInputStream(new FileInputStream(new File("GV.bkp")));
                        globalValues = (int[]) in.readObject();
                        in.close();
                } catch (Exception e) {
                        // Failed to retrieve backup. Try temp file from last save. (Unlikely.)
                        loadTmp();
                }
        }
        
        private void loadTmp() {
                try {
                        // look for tmp instead
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                                        new File("DB.tmp")));
                        map = (TreeMap<User, ArrayList<Bicycle>>) in.readObject();
                        in.close();
                        in = new ObjectInputStream(new FileInputStream(new File("GV.tmp")));
                        globalValues = (int[]) in.readObject();
                        in.close();
                } catch (Exception e) {
                        int answer = JOptionPane.showOptionDialog(null, "Database not found. " 
                                        + "Start with empty database?", "File not found", JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null, opts, opts[1]);
                        if (answer == YES) {
                                map = new TreeMap<User, ArrayList<Bicycle>>();
                                globalValues = new int[4];
                                globalValues[SLOT_MAX_BIKES] = 700;
                                globalValues[SLOT_BIKES_PER_USER] = 3;
                                 //Create empty database
                                try {
                                        File database = new File("DB");
                                        File array = new File("GV");

                                        ObjectOutputStream out = new ObjectOutputStream(
                                                        new FileOutputStream(database));
                                        out.writeObject(map);
                                        out.close();
                                        out = new ObjectOutputStream(new FileOutputStream(array));
                                        out.writeObject(globalValues);
                                        out.close();
                                }
                                catch (Exception exc) {
                                        JOptionPane.showMessageDialog(null, "Unable to create empty database."
                                            + "Information will not be saved.", "Error",
                                            JOptionPane.PLAIN_MESSAGE);                                 
                                }
                        } else {
                                JOptionPane.showMessageDialog(null, "Please recover data manually. "
                                                + "Click OK to exit.", "Exiting...", JOptionPane.PLAIN_MESSAGE);
                                System.exit(1);
                        }
                }
        }
        
        /**
         * Saves the data
         */   
        public void save() {
                /* Implementation based on
                 * http://javacook.darwinsys.com/new_recipes/10saveuserdata.jsp
                 * 1) Create tmp
                 * 2) Write to tmp, catching exceptions
                 * 3) Delete old bkp if it exists
                 * 4) Rename cur -> bkp
                 * 5) Rename tmp -> cur
                 */

                File database = new File("DB");
                File array = new File("GV");
                saveTmp(database, array);
        }

/* 
         * private method, starts saving process by saving the new data in
         * temp file. Prompts user whether to retry if failed. If yes, calls
         * save() again. If no, do nothing.
         */
        private void saveTmp(File database, File array) {
                File tmpDatabase = new File(database.getAbsolutePath() + ".tmp");
                File tmpArray = new File(array.getAbsolutePath() + ".tmp");
                tmpDatabase.deleteOnExit();
                tmpArray.deleteOnExit();
                // Hopefully, try/catch is not necessary above. Otherwise we will
                // need yet another sub-method.
                try {
                        ObjectOutputStream out = new ObjectOutputStream(
                                        new FileOutputStream(tmpDatabase));
                        out.writeObject(map);
                        out.close();
                        out = new ObjectOutputStream(new FileOutputStream(tmpArray));
                        out.writeObject(globalValues);
                        out.close();
                        saveFinish(database, array, tmpDatabase, tmpArray);
                } catch (Exception e) {
                        int ans = JOptionPane.showOptionDialog(null, "Could not write new data. Retry?",
                                        "Error", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                        null, opts, opts[YES]);
                        if (ans == YES) {
                                save();
                        }
                }
        }
        
        /* 
         * Private method, finishes saving process by creating backup file from
         * current file and renaming temp to current. Prompts upon failure as
         * above. 
         */
        private void saveFinish(File database, File array, File tmpDatabase, File tmpArray) {
                try {
                        File curDatabase = new File(database.getAbsolutePath());
                        File curArray = new File(array.getAbsolutePath());
                        // Above, save original filenames in temp objects
                        File bkpDatabase = new File(database.getAbsolutePath() + ".bkp");
                        bkpDatabase.delete();
                        File bkpArray = new File(array.getAbsolutePath() + ".bkp");
                        bkpArray.delete();
                        if (!database.canWrite()) {
                                throw new IOException("You smell");
                        }
                        if (!database.renameTo(bkpDatabase)) {
                                throw new IOException("Could not backup old data");
                        }
                        if (!array.renameTo(bkpArray)) {
                                throw new IOException("Could not backup old settings");
                        }
                        if (!tmpDatabase.renameTo(curDatabase)) {
                                throw new IOException("Could not finish saving process");
                        }
                        if (!tmpArray.renameTo(curArray)) {
                                throw new IOException("Could not finish saving process");
                        }
                } catch (Exception e) {
                                e.printStackTrace();
                        int ans = JOptionPane.showOptionDialog(null, e.getMessage() + '\n' + "Retry?",
                                        "Error", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                        null, opts, opts[YES]);
                        if (ans == YES) {
                                save();
                        }
                }
        }

 /** Checks whether a PIN is valid
        * @param PIN the pin code
        * @return true if valid, false if not
        */
       public boolean checkPIN(String PIN) {
                User[] users = userList();
                for (User user : users)
                        if (user.getPIN().equals(PIN)) {
                                return true;
                        }
                return false;
        }

        /**
         * Flags a bicycle as being inside the garage
         * 
         * @param bicycleID
         *            the ID of the bicycle to enter
         * @return true if successful, false if the bicycle does not exist in
         *         database
         */
        public boolean enterBicycle(String bicycleID) {
                Bicycle[] bList = bicycleList();
                for (Bicycle b : bList) {
                        if (b.getID().equals(bicycleID)) {
                                b.setInside(true);
                                b.setAdmissionDate(Calendar.getInstance());
                                save();
                                return true;
                        }
                }
                return false;
        }

        /**
         * Flags a bicycle as not being inside the garage
         * 
         * @param bicycleID
         *            the ID of the bicycle to exit
         * @return true if successful, false if the bicycle does not exist in
         *         database
         */
        public boolean exitBicycle(String bicycleID) {
                Bicycle[] bList = bicycleList();
                for (Bicycle b : bList) {
                        if (b.getID().equals(bicycleID)) {
                                b.setInside(false);
                                save();
                                return true;
                        }
                }
                return false;
        }

        /**
         * Returns the user with the specified ID
         * 
         * @param userID the ID of the user
         * @return the corresponding user
         */
        public User getUser(int userID) {
                User[] ul = userList();
                User uRef = new User(null, null, userID);
                for (User u : ul) {
                        if (u.equals(uRef)) {
                                return u;
                        }
                }
                return null;
        }
        
/** Returns the bicycle with the specified ID
        * @param bicycle ID the ID of the bicycle
        * @return the corresponding bicycle
        */
        public Bicycle getBicycle(String bicycleID) {
                Bicycle[] bl = bicycleList();
                Bicycle bRef = new Bicycle(null, bicycleID);
                for (Bicycle b : bl) {
                        if (b.equals(bRef)) {
                                return b;
                        }
                }
                return null;
        }

        /**
         * Returns an array containing the bicycles of a specified owner
         * 
         * @param userID
         *            the ID of the owner to list for
         * @return an array of the bicycles belonging to this owner
         */
        public Bicycle[] bicycleList(String userID) {
             User user = new User("", "", Integer.parseInt(userID));
             ArrayList<Bicycle> bl = map.get(user);
             Bicycle[] bikeArray = new Bicycle[bl.size()];
             for (int i = 0; i < bl.size(); i++) {
                 bikeArray[i] = bl.get(i);
             }
             return bikeArray;
        }
        
        /**
         * Returns an array containing all bicycles in the database
         * 
         * @return an array of all bicycles
         */
        public Bicycle[] bicycleList() {
                Collection bikeColl = map.values();
                ArrayList<Bicycle> bikeList = new ArrayList<Bicycle>();
                for (Object a: bikeColl) {
                        bikeList.addAll((ArrayList<Bicycle>) a);
                }
                Bicycle[] bl = new Bicycle[bikeList.size()];
                int index = 0;
                for (Bicycle b : bikeList) {
                        bl[index] = b;
                        index++;
                }
                return bl;
        }

        /**
         * Returns an array containing all bicycles in the garage,
         * sorted with the last admitted bicycle at the top
         * 
         * @return a sorted array of the bicycles in the garage
         */
        public Bicycle[] bicyclesInsideList(){
                ArrayList<Bicycle> bList = new ArrayList<Bicycle>();
                Collection bikeColl = map.values();
                for (Object list : bikeColl) {
                        for (Bicycle b : (ArrayList<Bicycle>) list) {
                                if (b.isInside()) {
                                        bList.add(b);
                                }
                        }
                }
                Collections.sort(bList, new ByDateBicycleComparator());
                Bicycle[] bikeArray = new Bicycle[bList.size()];
                for (int i = 0; i < bList.size(); i++) {
                        bikeArray[i] = bList.get(i);
                }
                return bikeArray;
        }

        /**
         * Returns an array containing all users in the database
         * 
         * @return an array of all users
         */
        public User[] userList() {
                Object[] ol = map.keySet().toArray();
                User[] ul = new User[ol.length];
                for (int i = 0; i < ol.length; i++) {
                        ul[i] = (User) ol[i];
                }
                return ul;
        }

        /**
         * Adds a user to the database
         * @param name the name of the user
         * @param PIN the PIN code of the user
         * @return the assigned ID number of the new user, else -1
         */
        public int addUser(String name, String PIN) {
                int currentUsers = getNbrUsers();
                int IDNbr = globalValues[SLOT_LAST_USER_ID];
                if (currentUsers < MAX_NBR_OF_USERS) {
                        // Sparar alla användares ID i en arraylist
                        ArrayList<Integer> IDHolder = new ArrayList<Integer>();
                        User[] userList = userList();
                        for (User tempUser : userList) {
                                IDHolder.add(tempUser.getID());
                        }
                        //Kollar igenom Arraylistan och kollar efter ett ID som inte finns. Hittar den
                        //ett ID så skapar den en ny användare.
                        int index = 0;
                        while (index <= IDHolder.size()) {
                                if (!IDHolder.contains(IDNbr)) {
                                        map.put(new User(name, PIN, IDNbr), new ArrayList<Bicycle>());
                                        save();
                                        return index;
                                }
                                IDNbr++;
                                if (IDNbr == 10000) {
                                        IDNbr = 1;
                                }
                                index++;
                        }
                }
                return -1;
        }

        /**
         * Adds a new bicycle to the system, assigns a new ID to the bicycle and
         * prints it on the barcode printer
         * 
         * @param userID the ID of the bicycle's owner
         * @return either SUCCEEDED, GLOBAL_BIKE_LIMIT_REACHED or USER_BIKE_LIMIT_REACHED
         */
        public int addBicycle(String userID) {
                if (getNbrBikes() >= globalValues[SLOT_MAX_BIKES]) {
                        return GLOBAL_BIKE_LIMIT_REACHED;
                } else {
                        User owner = new User("", "", Integer.parseInt(userID));
                        ArrayList<Bicycle> userBicycles = map.get(owner);
                        if (userBicycles.size() < globalValues[SLOT_BIKES_PER_USER]) {
                                int bicycleIDNbr = globalValues[SLOT_NEXT_BIKE_ID];
                                Bicycle[] bicycleList = bicycleList();
                                ArrayList<String> bicycleIDHolder = new ArrayList<String>();
                                for (Bicycle tempBicycle : bicycleList) {
                                        bicycleIDHolder.add(tempBicycle.getID());
                                }

                                int index = 0;
                                boolean found = false;
                                while (index <= bicycleIDHolder.size() && !found) {
                                        if (!bicycleIDHolder.contains(bicycleIDNbr)) {
                                                String sIndex = Integer.toString(bicycleIDNbr);
                                                StringBuilder sb = new StringBuilder(sIndex);
                                                while (sb.length() < 5) {
                                                        sb.insert(0, '0');
                                                }
                                                sIndex = sb.toString();
                                                userBicycles.add(new Bicycle(owner, sIndex));
                                                printer.printBarcode(sIndex);
                                                found = true;
                                        }
                                        bicycleIDNbr++;
                                        if (bicycleIDNbr == 100000) {
                                                bicycleIDNbr = 1;
                                        }
                                        index++;
                                }
                                map.put(owner, userBicycles);
                                globalValues[SLOT_NEXT_BIKE_ID] = bicycleIDNbr;
                                save();
                                return SUCCESS;
                        }
                        return USER_BIKE_LIMIT_REACHED;
                }
        }

        /**
         * Removes a bicycle from the system
         * 
         * @param bicycleID
         *            the ID of the bicycle to remove
         * @return true if the bicycle was removed, else false
         */
        public boolean removeBicycle(String bicycleID) {
                User[] users = userList();
                for (User user : users) {
                        ArrayList<Bicycle> bicycleList = new ArrayList<Bicycle>();
                        bicycleList = map.get(user);
                        for (Bicycle bicycle : bicycleList) {
                                if (bicycle.getID().equals(bicycleID)) {
                                        bicycleList.remove(bicycle);
                                        save();
                                        return true;
                                }
                        }
                }
                return false;
        }

        /**
        * Removes a user from the system
        * @param userID ID of user to remove
        * @return true if the user was removed, else false
        */
        public boolean removeUser(String userID) {
                User user = new User("", "", Integer.parseInt(userID));
                if (map.remove(user) != null) {
                        save();
                        return true;
                }
                return false;
        }


        /**
         * Get the total number of users
         * 
         * @return number of users
         */
        public int getNbrUsers() {
                return map.size();
        }

        /**
         * Get the total number of bicycles
         * 
         * @return number of bicycles
         */
        public int getNbrBikes() {
                int nbr = 0;
                Collection bikeColl = map.values();
                for (Object list : bikeColl) {
                        nbr += ((ArrayList<Bicycle>) list).size();
                }
                return nbr;
        }

        /**
         * Get the number of bicycles currently inside the garage
         * 
         * @return number of bicycles inside
         */
        public int getNbrBikesInside() {
                int nbr = 0;
                Collection bikeColl = map.values();
                for (Object list : bikeColl) {
                        for (Bicycle b : (ArrayList<Bicycle>) list) {
                                if (b.isInside()) {
                                        nbr++;
                                }
                        }
                }
                return nbr;
        }

        /**
         * Get the maximum allowed number of bicycles per user
         * 
         * @return number of bicycles
         */
        public int getMaxNbrBikesPerUser() {
                return globalValues[SLOT_BIKES_PER_USER];
        }

        /**
         * Get the maximum number of bicycles in the garage
         * 
         * @return number of bicycles
         */
        public int getMaxNbrBikes() {
                return globalValues[SLOT_MAX_BIKES];
        }

        /**
         * Sets the maximum allowed number of bicycles per user
         * 
         * @param max
         *            maximum number
         */
        public void setMaxNbrBikesPerUser(int max) {
                globalValues[SLOT_BIKES_PER_USER] = max;
                save();
        }

        /**
         * Sets the maximum allowed number of bicycles inside the garage
         * 
         * @param max
         *            maximum number
         */
        public void setMaxNbrBikes(int max) {
                globalValues[SLOT_MAX_BIKES] = max;
                save();
        }

        /*
         * Nested (I think?) comparator used by method bicyclesInsideList()
         */
        private static class ByDateBicycleComparator implements Comparator<Bicycle> {
                public int compare(Bicycle arg0, Bicycle arg1) {
                        return arg0.getAdmissionDate().compareTo(arg1.getAdmissionDate());
                }               
        }
}