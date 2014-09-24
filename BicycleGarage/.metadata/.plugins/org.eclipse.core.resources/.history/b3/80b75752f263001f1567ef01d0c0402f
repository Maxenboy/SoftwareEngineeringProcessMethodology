package bicycleGarage;

import interfaces.*;

import java.util.Date;

public class BicycleGarageManager {
        /**
         * Creates a new BicycleGaragerManager
         */
        BarcodePrinter printer;
        ElectronicLock entryLock;
        ElectronicLock exitLock;
        PinCodeTerminal terminal;
        Database database;
        Unlocker unlocker;

        public BicycleGarageManager(Database database) {
                this.database = database;
                unlocker = new Unlocker();
        }

        /**
         * Specifies the hardware objects
         * 
         * @param printer
         *            the BarCode printer
         * @param entryLock
         *            the electronic lock at the entry door
         * @param exitLock
         *            the electronic lock at the exit door
         * @param terminal
         *            the pin code terminal
         */
        public void registerHardwareDrivers(BarcodePrinter printer,
                        ElectronicLock entryLock, ElectronicLock exitLock,
                        PinCodeTerminal terminal) {
                this.printer = printer;
                this.entryLock = entryLock;
                this.exitLock = exitLock;
                this.terminal = terminal;
        }

        /**
         * Is called whenever the entry barcode reader reads a bar code Determines
         * whether the input ID is valid, and if so, unlocks the door and flags the
         * bicycle as inside
         * 
         * @param bicycleID
         *            the bar code read
         */
        public void entryBarcode(String bicycleID) {
                if (database.enterBicycle(bicycleID)) {
                        unlockEntryDoor(10);
                }
        }

        /**
         * Is called whenever the exit barcode reader reads a bar code Determines
         * whether the input ID is valid, and if so, unlocks the door and flags the
         * bicycle as not inside
         * 
         * @param bicycleID
         *            the bar code read
         */
        public void exitBarcode(String bicycleID) {
                if (database.exitBicycle(bicycleID)) {
                        unlockExitDoor(10);
                }

        }

        /**
         * Is called whenever the PIN code reader reads a digit If a full PIN code
         * is input, checks if it's valid, and if so, unlocks the door
         * 
         * @param c
         *            the character read
         */
        public void entryCharacter(char c) {
                unlocker.addCharacter(c);
        }

        /**
         * Unlocks the exit door
         * 
         * @param duration
         *            the duration in seconds
         */
        public void unlockExitDoor(int duration) {
                exitLock.open(duration);
        }

        /**
         * Unlocks the entry door
         * 
         * @param duration
         *            the duration in seconds
         */
        public void unlockEntryDoor(int duration) {
                entryLock.open(duration);
        }

        class Unlocker {
                private String input;
                private long time;

                public Unlocker() {
                        time = 0;
                }

                public void addCharacter(char c) {
                        Date date = new Date();
                        long oldtime = time;
                        time = date.getTime();
                        if ((time - oldtime) > 5000) {
                                input = "";
                        }
                        input = input + c;
                        if (c == '*') {
                                input = "";
                        }
                        if (input.length() == 4) {
                                if (database.checkPIN(input)) {
                                        terminal.lightLED(terminal.GREEN_LED, 2);
                                        entryLock.open(10);
                                        input = "";
                                } else {
                                        terminal.lightLED(terminal.RED_LED, 2);
                                        input = "";
                                }
                        }
                }
        }
}
