package no.oslomet.cs.algdat;


////////////////// class DobbeltLenketListe //////////////////////////////


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import java.util.Iterator;
import java.util.Objects;


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
        this.endringer++;
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
        indeksKontroll(indeks, true);
        endringer++;

        // tom liste
        if (indeks==0 && antall ==0 ){
            hode = new Node<>(verdi,null,null);
            hale = hode;
        }
        //legge inn fra starten
        else if (indeks == 0){
            hode = new Node<>(verdi, null, hode);
            hode.neste.forrige = hode;
        }
        //legge til på slutten
        else if(indeks == antall){
            hale = new Node <>(verdi, hale, null);
            hale.forrige.neste = hale;
        }
        //legger inn i midten
        else {
            Node<T> midten = hode;

            for(int i=0; i<indeks-1; i++){
                midten = midten.neste;
            }
            midten = new Node(verdi, midten, midten.neste);
            midten.neste.forrige = midten;
            midten.forrige.neste = midten;
        }
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
        endringer++;
        return gammelVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) { //returnerer false for null i stede for exception
            return false;
        }
        if (tom()){ //tom() = true når antall= 0
            return false;
        }
        Node<T> current = hode;
        while(current!=null){
            if (current.verdi.equals(verdi)){
                if (antall <= 1){
                    hode = hale =null;
                }
                else if (current.forrige == null){ // hode
                    hode =hode.neste;
                    hode.forrige = null;
                }
                else if (current.neste==null){ //hale
                    hale = hale.forrige;
                    hale.neste = null;
                }
                else{
                    current.forrige.neste = current.neste;
                    current.neste.forrige = current.forrige;
                }
                antall--;
                endringer++;
                return true;
            }

            current = current.neste; //endrer current
        }

        return false; //verdi ikke i listen
    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks,false);
        Node<T> utIndex;

        //--index starten
        if (indeks == 0){ //index0 = hode
            utIndex = hode; // satt til hodet
            if (hode.forrige == null && hode.neste == null){ // tester for om det kun er et element i lista
                hode = null;
                hale = null;
            }

            else {
                hode = hode.neste;
                hode.forrige = null;
            }
        }
        //--index slutten
        else if (indeks == antall-1){ // antall -1 = hale
            utIndex = hale; //satt til hale
            hale = hale.forrige;
            hale.neste = null;
        }
        //
        else {
            utIndex = finnNode(indeks );
            /*utIndex = nodeIndeks.neste; //utverdi satt til 'q'*/
            utIndex.forrige.neste = utIndex.neste; // 'q' satt til 'r'
            utIndex.neste.forrige = utIndex.forrige; //
        }
        antall--;
        endringer++;
        return utIndex.verdi; // verdi av indeksen
    }

    @Override
    public void nullstill() {
        Node current = hode;
        Node next = current.neste;

        for(int i = 0; i < antall-1; i++){
            current.neste = next.forrige = null;
            current.verdi = null;
            current = next;
        }

        hode = hale = null;
        antall = 0;
        endringer++;
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
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);

        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;
            fjernOK = false;
            iteratorendringer = endringer;
        }

        private DobbeltLenketListeIterator(int indeks){
            denne = finnNode(indeks);
            fjernOK = false;
            iteratorendringer = endringer;
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            if(iteratorendringer != endringer){
                throw new ConcurrentModificationException("Listen er endret");
            }
            if(!hasNext()){
                throw new NoSuchElementException("Har ingen neste");
            }

            T currentValue = denne.verdi;
            fjernOK = true;
            denne = denne.neste;

            return currentValue;
        }

        @Override
        public void remove(){
            if(iteratorendringer != endringer){
                throw new ConcurrentModificationException("Listen er endret");
            }

            if(!fjernOK){
                throw new IllegalStateException("Ikke OK å fjerne");
            }

            fjernOK = false;

            if(antall == 1){
                hode = null;
                hale = null;
            }

            else if(denne == null){
                hale = hale.forrige;
                hale.neste = null;
            }

            else if(denne.forrige == hode){
                hode = hode.neste;
                hode.forrige = null;
            }
            else{
                Node next = denne;
                Node prev = denne.forrige.forrige;

                prev.neste = next;
                next.forrige = prev;

            }

            antall--;
            endringer++;
            iteratorendringer++;
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        if(liste.antall() < 2) return;

        for(int j = 0; j < liste.antall(); j++) {
            for (int i = 0; i < liste.antall() - 1; i++) {
                if (c.compare(liste.hent(i), liste.hent(i + 1)) > 0) {
                    T temp = liste.hent(i);
                    liste.oppdater(i, liste.hent(i + 1));
                    liste.oppdater(i + 1, temp);
                }
            }
        }
    }

} // class DobbeltLenketListe


