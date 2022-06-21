package project3_ver1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Game extends Rental {

    /** represents the name of the game */
    private String nameGame;

    /**
     * Represents the player the person rented to play the game,
     * null if no console was rented.
     */
    private Console console;

    public Game() {
    }

    public Game(String nameOfRenter,
                GregorianCalendar rentedOn,
                GregorianCalendar dueBack,
                GregorianCalendar actualDateReturned,
                String nameGame,
                Console console) {
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);
        this.nameGame = nameGame;
        this.console = console;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    @Override
    public double getCost(GregorianCalendar dueBack) {

        // Do not use this approach
        //String dateBeforeString = "2017-05-24";
        //	String dateAfterString = "2017-07-29";
        //
        //	//Parsing the date
        //	LocalDate dateBefore = LocalDate.parse(dateBeforeString);
        //	LocalDate dateAfter = LocalDate.parse(dateAfterString);
        //
        //	//calculating number of days in between
        //	long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
        //
        //	//displaying the number of days
        //	System.out.println(noOfDaysBetween);
        //

        double cost = 0;
        if (console != null) {
            Console temp = new Console(this.nameOfRenter, rentedOn, this.dueBack, actualDateReturned, console.getConsoleType());
            cost += temp.getCost(dueBack);
        }

        GregorianCalendar gTemp = new GregorianCalendar();
        cost += 3;

        //        Date d = dueBack.getTime();
        //        gTemp.setTime(d);
        gTemp = (GregorianCalendar) dueBack.clone();     //       gTemp = dueBack;  does not work!!

        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//        System.out.println(formatter.format(gTemp.getTime()));

        for (int days = 0; days < 7; days++)
            gTemp.add(Calendar.DATE, -1);

//        System.out.println(formatter.format(gTemp.getTime()));
//        System.out.println(formatter.format(rentedOn.getTime()));

        while (gTemp.after(rentedOn)) {
            cost += .5;
            gTemp.add(Calendar.DATE, -1);

        }
        return cost;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + nameGame + '\'' +
                ", player=" + console + super.toString() +
                '}';
    }
}
