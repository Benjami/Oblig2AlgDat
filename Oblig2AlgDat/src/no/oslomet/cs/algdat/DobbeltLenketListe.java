package no.oslomet.cs.algdat;


////////////////// class DobbeltLenketListe //////////////////////////////


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;



public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
//        throw new NotImplementedException();
    }

    public DobbeltLenketListe(T[] a) {
        if(a == null) throw new NullPointerException("Tabellen a er null!"); //FIXME vet ikke om a == null funker må sjekkes med tester
        else if (a.length == 0) return;

        int count = 0;
        while(count < a.length && a[count] == null){
            count++;
        }
        if(count == a.length){
            hode = null;
            hale = null;
            return;
        }

        hode = new Node(a[count], null, null);
        Node current = hode;
        for(int i = count+1; i < a.length; i++){
            if(a[i] != null) {
                current.neste = new Node(a[i], current, null);
                current = current.neste;
            }
        }
        hale = current;
    }

    public Liste<T> subliste(int fra, int til){
        throw new NotImplementedException();
    }

    @Override
    public int antall() {
        Node current = hode;
        int antall = 0;
        while(current != null){
            current = current.neste;
            antall++;
        }
        this.antall = antall;
        return antall;
    }

    @Override
    public boolean tom() {
        if(antall() == 0) return true;
        else return false;
    }

    @Override
    public boolean leggInn(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public T hent(int indeks) {
        throw new NotImplementedException();
    }

    @Override
    public int indeksTil(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new NotImplementedException();
    }

    @Override
    public boolean fjern(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public T fjern(int indeks) {
        throw new NotImplementedException();
    }

    @Override
    public void nullstill() {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        Node current = hode;
        StringBuilder utString = new StringBuilder();
        utString.append("[");

        if(current != null) {
            utString.append(current.verdi);
            current = current.neste;
        }

        while(current != null){
            utString.append(", ").append(current.verdi);
            current = current.neste;
        }
        utString.append("]");
        return utString.toString();
    }

    public String omvendtString() {
        Node current = hale;
        StringBuilder utString = new StringBuilder();
        utString.append("[");

        if(current != null) {
            utString.append(current.verdi);
            current = current.forrige;
        }

        while(current != null){
            utString.append(", ").append(current.verdi);
            current = current.forrige;
        }
        utString.append("]");
        return utString.toString();
    }

    @Override
    public Iterator<T> iterator() {
        throw new NotImplementedException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new NotImplementedException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            throw new NotImplementedException();
        }

        private DobbeltLenketListeIterator(int indeks){
            throw new NotImplementedException();
        }

        @Override
        public boolean hasNext(){
            throw new NotImplementedException();
        }

        @Override
        public T next(){
            throw new NotImplementedException();
        }

        @Override
        public void remove(){
            throw new NotImplementedException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new NotImplementedException();
    }

} // class DobbeltLenketListe


