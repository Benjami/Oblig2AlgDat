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
        //FIXME ikke siker på om den tome konstruktøren skal være slik
        antall = 0;
        endringer = 0;
    }

    public DobbeltLenketListe(T[] a) {
        antall = 0;
        endringer = 0;
        if(a == null) throw new NullPointerException("Tabellen a er null!");
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
        antall++;
        Node current = hode;
        for(int i = count+1; i < a.length; i++){
            if(a[i] != null) {
                current.neste = new Node(a[i], current, null);
                current = current.neste;
                antall++;
            }
        }
        hale = current;
        endringer = 0;
    }


    public boolean fratilKontroll(int fra, int til){ //FIXME Må kanskje endres virker som han beskriver en velig spesefik methode i oppgaven, men jeg fant den ikke
        indeksKontroll(fra , true);
        indeksKontroll(til, true);
        if(fra > til) throw new IllegalArgumentException("Fra kan ikke være støre en til");
        return true;
    }

    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(fra,til);
        Object[] array = new Object[til-fra];
        int j = 0;
        Node current = finnNode(fra);

        for(int i = fra; i < til; i++){
            array[j++] = current.verdi;
            current = current.neste;
        }

        return new DobbeltLenketListe<T>((T[])array); //FIXME constructor er kanskje ikke lov her
    }

    @Override
    public int antall() {
        /*Node current = hode;
        int antall = 0;
        while(current != null){
            current = current.neste;
            antall++;
        }
        this.antall = antall;
        return antall;*/
        return antall;
    }

    @Override
    public boolean tom() {
        if(antall() == 0) return true;
        else return false;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi);
        this.antall++;
        this.endringer++;//        throw new NotImplementedException();
        if(hode == null){
            hode = new Node<>(verdi,null,null);
            hale = new Node<>(verdi,null,null);
            return true;
        }
        hale.neste = new Node(verdi,hale,null);
        hale = hale.neste;
        if(hode.neste == null){
            hode.neste = hale;
            hale.forrige = hode;
        }
        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Verdien er her satt til NULL!!!!");
        indeksKontroll(indeks,true); // true?

        Node<T>nyV = new Node<>(verdi,null,null);
        Node<T>nyIn =finnNode(indeks);
        //TODO: ifs: Antall==0/index, nyIn.forrige ==null, osv

        antall++;
    }

    @Override
    public boolean inneholder(T verdi) {
        if(indeksTil(verdi) == -1) return false;
        return true;
    }


    public Node<T> finnNode(int index){
        Node current;
        if(index < antall/2 || index == 0){
            current = hode;
            for(int i = 0; i < index; i++){
                current = current.neste;
            }
        }else{
            current = hale;
            for(int i = antall-1; i > index; i--){
                current = current.forrige;
            }
        }

        return  current;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);
        T t = finnNode(indeks).verdi;
        return t;
    }

    @Override
    public int indeksTil(T verdi) {
        Node current = hode;
        int indeks = 0;
        while (current != null){
            if(current.verdi.equals(verdi)) return indeks;
            indeks++;
            current = current.neste;
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        indeksKontroll(indeks, false);
        if(nyverdi == null) throw new NullPointerException("Nyverdi kan ikke være null");
        Node current = finnNode(indeks);
        T gammelVerdi = (T)current.verdi;
        current.verdi = nyverdi;
        if(indeks == 0) hode = current;
        if(indeks == antall-1) hale = current;
        return gammelVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) {
            return false;
        }
        if (tom()){ //tom() = true når antall= 0
            return false;
        }

        Node<T> current = hode;
        




        return false;
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


