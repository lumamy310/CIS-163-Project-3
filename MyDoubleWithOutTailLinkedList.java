package project3_ver1;

import labLinkedListSingle.Node;

import java.io.Serializable;
import java.util.Random;

/**********************************************************************
 * This class is responsible for the creation and modification of
 * the list of games and consoles. In this case,
 * the list is a double linked list without a tail.
 * The list is orderd by Games first (ordered by dueDate)
 * and by Consoles second (ordered by dueDate). If two dates are
 * the same then they are sorted by renter’s name.
 * @author Lucas Myers - Computer Science
 * @version Winter 2022
 **********************************************************************/
public class MyDoubleWithOutTailLinkedList implements Serializable {

    /** A temporary node that acts as a pointer */
    private DNode top;

    /**********************************************************************
     * Constructor that creates the empty double linked list
     * without a tail.
     **********************************************************************/
    public MyDoubleWithOutTailLinkedList() {
        top = null;
    }

    /**********************************************************************
     * Method that goes up and down the list, counting each node,
     * and returning the size of the list.
     * @return The size of the list
     **********************************************************************/
    public int size() {
        if (top == null)
            return 0;

        int total = 0;
        DNode temp = top;
        while (temp != null) {
            total++;
            temp = temp.getNext();
        }

        int totalBack = 0;
        temp = top;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }

        while (temp != null) {
            totalBack++;
            temp = temp.getPrev();
        }

        if (total != totalBack) {
            System.out.println("next links " + total +
                    " do not match prev links " + totalBack);
            throw new RuntimeException();
        }

        return total;

    }

    /**********************************************************************
     * Method that clears the entire list.
     **********************************************************************/
    public void clear() {
        Random rand = new Random(13);
        while (size() > 0) {
            int number = rand.nextInt(size());
            remove(number);
        }
    }

    /**********************************************************************
     * Method that adds instances of games and consoles to the list.
     * Orders the list by Games first (ordered by dueDate) and
     * by Consoles second (ordered by dueDate). If two dates are
     * the same then they are sorted by renter’s name.
     * @param s All the information pertaining to the instance of the
     *          rental. (date rented, name, ect.)
     **********************************************************************/
    public void add(Rental s) {
        DNode temp = top;

        // no list
        if (top == null) {
            top = new DNode(s, null, null);
            return;
        }

        // s is a Game, and s goes on top
        if (s instanceof Game && top.getData().getDueBack().after(s.dueBack)) {
            top = new DNode(s, top, null);
            top.getNext().setPrev(top);
            return;
        }

        // s is a Game, only consoles in list, and s goes on top
        if (s instanceof Game && top.getData() instanceof Console) {
            top = new DNode(s, top, null);
            top.getNext().setPrev(top);
            return;
        }

        //s is a console, only consoles in list, s goes on top
        if(s instanceof Console && top.getData() instanceof Console &&
                top.getData().getDueBack().after(s.dueBack)){
            top = new DNode(s, top, null);
            top.getNext().setPrev(top);
            return;
        }

        //s is a game, only games in list, s goes on bottom
        while(temp.getNext() != null){
            temp = temp.getNext();
        }
        if(s instanceof Game && temp.getData() instanceof Game &&
                temp.getData().getDueBack().before(s.dueBack)){
            temp.setNext(new DNode(s, null, temp));
            return;
        }

        //s is a console, only consoles in list, s goes on bottom
        temp = top;
        while(temp.getNext() != null){
            temp = temp.getNext();
        }
        if(s instanceof Console && temp.getData() instanceof Console &&
                temp.getData().getDueBack().before(s.dueBack)){
            temp.setNext(new DNode(s, null, temp));
            return;
        }

        //s is a console, only games in list, s goes on bottom
        temp = top;
        while(temp.getNext() != null){
            temp = temp.getNext();
        }
        if(s instanceof Console && temp.getData() instanceof Game){
            temp.setNext(new DNode(s, null, temp));
            return;
        }

        //s is a game, only games in list, s goes between
        // (works when list has games and consoles too)
        temp = top;
        //temp is on the node that is due after
        while(temp.getData() instanceof Game &&
                temp.getData().getDueBack().before(s.dueBack) &&
                temp.getNext() != null){
            temp = temp.getNext();
        }
        if(temp.getPrev() != null && s instanceof Game &&
                temp.getData().getDueBack().after(s.dueBack) &&
                temp.getPrev().getData().getDueBack().before(s.dueBack)){
            temp.setPrev(new DNode(s, temp, temp.getPrev()));
            temp.getPrev().getPrev().setNext(temp.getPrev());
            return;
        }

        //s is a console, only consoles in list, s goes between
        temp = top;
        while(temp.getData() instanceof Console &&
                temp.getData().getDueBack().before(s.dueBack) &&
                temp.getNext() != null){
            temp = temp.getNext();
        }
        if(temp.getPrev() != null && s instanceof Console &&
                temp.getData().getDueBack().after(s.dueBack) &&
                temp.getPrev().getData().getDueBack().before(s.dueBack)){
            temp.setPrev(new DNode(s, temp, temp.getPrev()));
            temp.getPrev().getPrev().setNext(temp.getPrev());
            return;
        }

        //s is a console, consoles and games in list, s goes between
        temp = top;
        while(temp.getData() instanceof Game && temp.getNext() != null){
            temp = temp.getNext();
        }
        while(temp.getData() instanceof Console &&
                temp.getData().getDueBack().before(s.dueBack) &&
                temp.getNext() != null){
            temp = temp.getNext();
        }
        if(temp.getPrev() != null && s instanceof Console &&
                temp.getData().getDueBack().after(s.dueBack) &&
                temp.getPrev().getData().getDueBack().before(s.dueBack)){
            temp.setPrev(new DNode(s, temp, temp.getPrev()));
            temp.getPrev().getPrev().setNext(temp.getPrev());
            return;
        }

        //s is a game, only games in list, s is equal
        //(should work with games and consoles in list)
        temp = top;
        //should set temp to the first node that equals s
        while(temp.getData() instanceof Game && temp.getNext() != null){
            if(temp.getData().getDueBack().equals(s.dueBack))
                break;
            temp = temp.getNext();
        }
        if(s instanceof Game && temp.getData() instanceof Game &&
                temp.getData().getDueBack().equals(s.dueBack)){
            //if the name of s comes before the name of temp
            if(s.getNameOfRenter().compareTo(temp.getData().getNameOfRenter()) < 0){
                if(temp == top){
                    top = new DNode(s, top, null);
                    top.getNext().setPrev(top);
                    return;
                }
                temp.setPrev(new DNode(s, temp, temp.getPrev()));
                temp.getPrev().getPrev().setNext(temp.getPrev());
                return;
            }
            if(s.getNameOfRenter().compareTo(temp.getData().getNameOfRenter()) >= 0){
                while(temp.getNext()!= null &&
                        s.getNameOfRenter().compareTo(temp.getNext().getData().getNameOfRenter())
                                >= 0 && temp.getNext().getData().getDueBack().equals(s.dueBack)){
                    temp = temp.getNext();
                }
                if(temp.getNext() == null){
                    temp.setNext(new DNode(s, null, temp));
                    return;
                }
                temp.setNext(new DNode(s, temp.getNext(), temp));
                temp.getNext().getNext().setPrev(temp.getNext());
                return;
            }
        }

        //s is a console, only consoles in list, s is equal
        temp = top;
        //should set temp to the first node that is equal
        while(temp.getData() instanceof Console && temp.getNext() != null){
            if(temp.getData().getDueBack().equals(s.dueBack))
                break;
            temp = temp.getNext();
        }
        if(s instanceof Console && temp.getData() instanceof Console &&
                temp.getData().getDueBack().equals(s.dueBack)){
            //the name of s comes before the name of temp
            if(s.getNameOfRenter().compareTo(temp.getData().getNameOfRenter()) < 0){
                if(temp == top){
                    top = new DNode(s, top, null);
                    top.getNext().setPrev(top);
                    return;
                }
                temp.setPrev(new DNode(s, temp, temp.getPrev()));
                temp.getPrev().getPrev().setNext(temp.getPrev());
                return;
            }
            //the name of s goes after the name of temp
            if(s.getNameOfRenter().compareTo(temp.getData().getNameOfRenter()) >= 0){
                while(temp.getNext()!= null &&
                        s.getNameOfRenter().compareTo(temp.getNext().getData().getNameOfRenter())
                                >= 0 && temp.getNext().getData().getDueBack().equals(s.dueBack)){
                    temp = temp.getNext();
                }
                if(temp.getNext() == null){
                    temp.setNext(new DNode(s, null, temp));
                    return;
                }
                temp.setNext(new DNode(s, temp.getNext(), temp));
                temp.getNext().getNext().setPrev(temp.getNext());
                return;
            }
        }

        //s is a console, consoles and games in list, s is equal
        while(temp.getData() instanceof Game && temp.getNext() != null){
            temp = temp.getNext();
        }
        while(temp.getData() instanceof Console && temp.getNext() != null){
            if(temp.getData().getDueBack().equals(s.dueBack))
                break;
            temp = temp.getNext();
        }
        if(s instanceof Console && temp.getData() instanceof Console &&
                temp.getData().getDueBack().equals(s.dueBack)){
            if(s.getNameOfRenter().compareTo(temp.getData().getNameOfRenter()) < 0){
                temp.setPrev(new DNode(s, temp, temp.getPrev()));
                temp.getPrev().getPrev().setNext(temp.getPrev());
                return;
            }
            if(s.getNameOfRenter().compareTo(temp.getData().getNameOfRenter()) >= 0){
                while(temp.getNext()!= null &&
                        s.getNameOfRenter().compareTo(temp.getNext().getData().getNameOfRenter())
                                >= 0 && temp.getNext().getData().getDueBack().equals(s.dueBack)){
                    temp = temp.getNext();
                }
                if(temp.getNext() == null){
                    temp.setNext(new DNode(s, null, temp));
                    return;
                }
                temp.setNext(new DNode(s, temp.getNext(), temp));
                temp.getNext().getNext().setPrev(temp.getNext());
                return;
            }
        }

        //s is a game, consoles and games in list, s goes on bottom
        temp = top;
        while(temp.getNext().getData() instanceof Game){
            temp = temp.getNext();
        }
        if(s instanceof Game && temp.getNext().getData() instanceof Console &&
                temp.getData().getDueBack().before(s.dueBack)){
            temp.setNext(new DNode(s, temp.getNext(), temp));
            temp.getNext().getNext().setPrev(temp.getNext());
            return;
        }

        //s is a console, consoles and games in list, s goes on top
        temp = top;
        while(temp.getNext().getData() instanceof Game){
            temp = temp.getNext();
        }
        if(s instanceof Console && temp.getNext().getData() instanceof Console &&
                temp.getNext().getData().getDueBack().after(s.dueBack)){
            temp.setNext(new DNode(s, temp.getNext(), temp));
            temp.getNext().getNext().setPrev(temp.getNext());
            return;
        }



    }

    /**********************************************************************
     * Method that removes a specific element from the list,
     * specified by the index.
     * @param index The index of the element to be deleted.
     * @return The deleted element of the list.
     **********************************************************************/
    public Rental remove(int index) {


        //no list
        if (top == null)
            return null;

        //invalid index
        if(index < 0 || index >= size())
            throw new IndexOutOfBoundsException();

        //one element in list
        if(top.getNext() == null && index == 0){
            Rental temp = top.getData();
            top = null;
            return temp;
        }

        //delete from top
        if(index == 0 && top.getNext() != null){
            Rental temp = top.getData();
            top = top.getNext();
            top.setPrev(null);
            return temp;
        }

        //delete from bottom
        if(index == size() - 1){
            DNode temp = top;
            while(temp.getNext() != null){
                temp = temp.getNext();
            }
            Rental q = temp.getData();
            temp.getPrev().setNext(null);
            return q;
        }

        //delete from middle
        DNode temp = top;
        for(int i=0; i<index-1; i++){
            temp = temp.getNext();
        }
        Rental q = temp.getNext().getData();
        temp.setNext(temp.getNext().getNext());
        temp.getNext().setPrev(temp);
        return q;
    }

    /**********************************************************************
     * Method that returns the data of the node specified by
     * the index.
     * @param index The index of the node to be returned.
     * @return The data of the node.
     **********************************************************************/
    public Rental get(int index) {

        //no list
        if (top == null)
            return null;

        //invalid index
        if(index < 0 || index >= size())
            throw new IndexOutOfBoundsException();

        //top element
        if(index == 0)
            return top.getData();

        //bottom element
        if(index == size() - 1){
            DNode temp = top;
            while(temp.getNext() != null){
                temp = temp.getNext();
            }
            return temp.getData();
        }

        //middle element
        DNode temp = top;
        for(int i=0; i<index; i++){
            temp = temp.getNext();
        }
        return temp.getData();
    }

    /**********************************************************************
     * Method that prints the entire list.
     **********************************************************************/
    public void display() {
        DNode temp = top;
        while (temp != null) {
            System.out.println(temp.getData());
            temp = temp.getNext();
        }
    }

    /**********************************************************************
     * Method that turns the list into an easily readable string.
     * @return A string representing the list.
     **********************************************************************/
    public String toString() {
        return "LL {" +
                "top=" + top +
                ", size=" + size() +
                '}';
    }

}