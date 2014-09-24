package bicycleGarage;

import testDrivers.*;
import interfaces.*;

/**
* Runs the application
*/
public class BicycleGarage {
        public BicycleGarage() {
                ElectronicLock entryLock = new ElectronicLockTestDriver("Entry lock");
                ElectronicLock exitLock = new ElectronicLockTestDriver("Exit lock");
                BarcodePrinter printer = new BarcodePrinterTestDriver();
                PinCodeTerminal terminal = new PinCodeTerminalTestDriver();
                                
                Database db = new Database(printer);
                BicycleGarageManager manager = new BicycleGarageManager(db);
                
                manager.registerHardwareDrivers(printer, entryLock, exitLock, terminal);
                
                terminal.register(manager);
                
                BarcodeReader readerEntry = new BarcodeReaderEntryTestDriver();
                BarcodeReader readerExit = new BarcodeReaderExitTestDriver();
                readerEntry.register(manager);
                readerExit.register(manager);
                
                ManagerGUI gui = new ManagerGUI(db, manager);
        }
        
        public static void main(String[] args) {
                new BicycleGarage();
        }
}
