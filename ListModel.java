package project3_ver1;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;


public class ListModel extends AbstractTableModel {

    /**
     * holds all the rentals
     */
    private MyDoubleWithOutTailLinkedList listOfRentals;

    /**
     * holds only the rentals that are to be displayed
     */
    private MyDoubleWithOutTailLinkedList fileredListRentals;

    /**
     * current screen being displayed
     */
    private ScreenDisplay display = ScreenDisplay.CurrentRentalStatus;

    private String[] columnNamesCurrentRentals = {"Renter\'s Name", "Est. Cost",
            "Rented On", "Due Date ", "Console", "Name of the Game"};

    private String[] columnNamesforRented = {"Renter\'s Name", "rented On Date",
            "Due Date", "Actual date returned ", "Est. Cost", " Real Cost"};

    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public ListModel() {
        display = ScreenDisplay.CurrentRentalStatus;
        listOfRentals = new MyDoubleWithOutTailLinkedList();
        fileredListRentals = new MyDoubleWithOutTailLinkedList();
        UpdateScreen();
        createList();
    }

    public void setDisplay(ScreenDisplay selected) {
        display = selected;
        UpdateScreen();
    }

    private void UpdateScreen() {
        switch (display) {
            case CurrentRentalStatus:
                fileredListRentals.clear();
                for (int i = 0; i < listOfRentals.size(); i++)
                    if (listOfRentals.get(i).getActualDateReturned() == null)
                        fileredListRentals.add(listOfRentals.get(i));
                break;

            case RetendItems:
                fileredListRentals.clear();
                for (int i = 0; i < listOfRentals.size(); i++)
                    if (listOfRentals.get(i).getActualDateReturned() != null)
                        fileredListRentals.add(listOfRentals.get(i));
                break;


            default:
                throw new RuntimeException("upDate is in undefined state: " + display);
        }
        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int col) {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals[col];
            case RetendItems:
                return columnNamesforRented[col];
        }
        throw new RuntimeException("Undefined state for Col Names: " + display);
    }

    @Override
    public int getColumnCount() {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals.length;
            case RetendItems:
                return columnNamesforRented.length;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int getRowCount() {
        return fileredListRentals.size();     // returns number of items in the arraylist
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (display) {
            case CurrentRentalStatus:
                return currentParkScreen(row, col);
            case RetendItems:
                return rentedOutScreen(row, col);
        }
        throw new IllegalArgumentException();
    }

    private Object currentParkScreen(int row, int col) {
        switch (col) {
            case 0:
                return (fileredListRentals.get(row).nameOfRenter);

            case 1:
                return (fileredListRentals.get(row).getCost(fileredListRentals.
                        get(row).dueBack));

            case 2:
                return (formatter.format(fileredListRentals.get(row).rentedOn.getTime()));

            case 3:
                if (fileredListRentals.get(row).dueBack == null)
                    return "-";

                return (formatter.format(fileredListRentals.get(row).dueBack.getTime()));

            case 4:
                if (fileredListRentals.get(row) instanceof Console)
                    return (((Console) fileredListRentals.get(row)).getConsoleType());
                else {
                    if (fileredListRentals.get(row) instanceof Game)
                        if (((Game) fileredListRentals.get(row)).getConsole() != null)
                            return ((Game) fileredListRentals.get(row)).getConsole().getConsoleType();
                        else
                            return "";
                }

            case 5:
                if (fileredListRentals.get(row) instanceof Game)
                    return (((Game) fileredListRentals.get(row)).getNameGame());
                else
                    return "";
            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    private Object rentedOutScreen(int row, int col) {
        switch (col) {
            case 0:
                return (fileredListRentals.get(row).nameOfRenter);

            case 1:
                return (formatter.format(fileredListRentals.get(row).rentedOn.
                        getTime()));
            case 2:
                return (formatter.format(fileredListRentals.get(row).dueBack.
                        getTime()));
            case 3:
                return (formatter.format(fileredListRentals.get(row).
                        actualDateReturned.getTime()));

            case 4:
                return (fileredListRentals.
                        get(row).getCost(fileredListRentals.get(row).dueBack));

            case 5:
                return (fileredListRentals.
                        get(row).getCost(fileredListRentals.get(row).
                        actualDateReturned
                ));

            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    public void add(Rental a) {
        listOfRentals.add(a);
        UpdateScreen();
    }

    public Rental get(int i) {
        return fileredListRentals.get(i);
    }

    public void upDate(int index, Rental unit) {
        UpdateScreen();
    }

    public void saveDatabase(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
              os.writeObject(listOfRentals);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    public void loadDatabase(String filename) {
        listOfRentals.clear();

        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            listOfRentals = (MyDoubleWithOutTailLinkedList) is.readObject();
            UpdateScreen();
            is.close();
        } catch (Exception ex) {
            throw new RuntimeException("Loading problem: " + display);

        }
    }


    public void createList() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();
        GregorianCalendar g7 = new GregorianCalendar();
        GregorianCalendar g8 = new GregorianCalendar();

        try {
            Date d1 = df.parse("1/20/2020");
            g1.setTime(d1);
            Date d2 = df.parse("12/22/2020");
            g2.setTime(d2);
            Date d3 = df.parse("12/20/2019");
            g3.setTime(d3);
            Date d4 = df.parse("7/02/2020");
            g4.setTime(d4);
            Date d5 = df.parse("1/20/2010");
            g5.setTime(d5);
            Date d6 = df.parse("9/29/2020");
            g6.setTime(d6);
            Date d7 = df.parse("7/25/2020");
            g7.setTime(d7);
            Date d8 = df.parse("7/29/2020");
            g8.setTime(d8);

            Console console1 = new Console("Person1", g4, g6, null, ConsoleTypes.PlayStation4);
            Console console2 = new Console("Person2", g5, g3, null, ConsoleTypes.PlayStation4);
            Console console3 = new Console("Person5", g4, g8, null, ConsoleTypes.SegaGenesisMini);
            Console console4 = new Console("Person6", g4, g7, null, ConsoleTypes.SegaGenesisMini);
            Console console5 = new Console("Person1", g5, g4, g3, ConsoleTypes.XBoxOneS);

            Game game1 = new Game("Person1", g3, g2, null, "title1",
                    new Console("Person1", g3, g2, null, ConsoleTypes.PlayStation4));
            Game game2 = new Game("Person1", g3, g1, null, "title2",
                    new Console("Person1", g3, g1, null, ConsoleTypes.PlayStation4));
            Game game3 = new Game("Person1", g5, g3, null, "title2",
                    new Console("Person1", g5, g3, null, ConsoleTypes.SegaGenesisMini));
            Game game4 = new Game("Person7", g4, g8, null, "title2", null);
            Game game5 = new Game("Person3", g3, g1, g1, "title2",
                    new Console("Person3", g3, g1, g1, ConsoleTypes.XBoxOneS));
            Game game6 = new Game("Person6", g4, g7, null, "title1",
                    new Console("Person6", g4, g7, null, ConsoleTypes.NintendoSwich));
            Game game7 = new Game("Person5", g4, g8, null, "title1",
                    new Console("Person5", g4, g8, null, ConsoleTypes.NintendoSwich));

//            add(game1);
//            add(game4);
//            add(game5);
//            add(game2);
//            add(game3);
//            add(game6);
//            add(game7);
//
//            add(console1);
//            add(console2);
//            add(console5);
//            add(console3);
//            add(console4);


               // These commented out code is to help with debugging for step 2 and Step 3
                
//                add(game1);
//            add(game4);
//            add(console1);
//            listOfRentals.remove(0);
//            add(console4);
//            add(game5);
//            add(game2);
//            listOfRentals.remove(listOfRentals.size()-1);
//            listOfRentals.remove(2);
//            add(game3);
//            add(console5);
//            add(game6);
//            add(console3);
//            listOfRentals.remove(listOfRentals.size()-1);
//            add(game7);
//            add(console2);
//            for (int i = 0; i < listOfRentals.size(); i++)
//                System.out.println(listOfRentals.get(i).toString());
                
            

            // create a bunch of them.
            int count = 0;
            Random rand = new Random(69);
            String guest = null;

            while (count < 300) {
                Date date = df.parse("7/" + (rand.nextInt(10) + 2) + "/2020");
                GregorianCalendar g = new GregorianCalendar();
                g.setTime(date);
                if (rand.nextBoolean()) {
                    guest = "Game" + rand.nextInt(5);
                    Game game;
                    if (count % 2 == 0)
                        game = new Game(guest, g4, g, null, "title2",
                                new Console(guest, g1, g, null, getOneRandom(rand)));
                    else
                        game = new Game(guest, g4, g, null, "title2", null);
                    add(game);

                } else {
                    guest = "Console" + rand.nextInt(5);
                    date = df.parse("7/" + (rand.nextInt(20) + 2) + "/2020");
                    g.setTime(date);
                    Console console = new Console(guest, g4, g, null, getOneRandom(rand));

                    add(console);


                }

                count++;
                //debugging
//                if(count == 32)
//                    System.out.print("line found begin debugging");
            }
            System.out.println(listOfRentals.size());


        } catch (ParseException e) {
            throw new RuntimeException("Error in testing, creation of list");
        }
    }

    public ConsoleTypes getOneRandom(Random rand) {

        int number = rand.nextInt(ConsoleTypes.values().length - 1);
        switch (number) {
            case 0:
                return ConsoleTypes.PlayStation4;
            case 1:
                return ConsoleTypes.XBoxOneS;
            case 2:
                return ConsoleTypes.PlayStation4Pro;
            case 3:
                return ConsoleTypes.NintendoSwich;
            default:
                return ConsoleTypes.SegaGenesisMini;
        }
    }
}




