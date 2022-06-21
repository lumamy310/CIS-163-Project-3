package project3_ver1;


import java.io.Serializable;

public class DNode implements Serializable {
    private Rental data;
    private DNode next;
    private DNode prev;

    public DNode(Rental data, DNode next, DNode prev) {
        super();
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public DNode() {
    }

    public Rental getData() {
        return data;
    }

    public void setData(Rental data) {
        this.data = data;
    }

    public DNode getNext() {
        return next;
    }

    public void setNext(DNode next) {
        this.next = next;
    }

    public void setPrev(DNode prev) {
        this.prev = prev;
    }

    public DNode getPrev() {
        return prev;
    }

    @Override
    public String toString() {
        return "DNode{" +
                "data=" + data +
                ", next=" + next +
                              '}';
    }
}
