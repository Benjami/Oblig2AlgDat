package no.oslomet.cs.algdat;

import org.omg.CORBA.INTERNAL;

public class Main {

    public static void main (String [] args){
        //String[] s1 = {}, s2 = {"A"}, s3 = {null, "A", null, "B", null};
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        liste.leggInn(1);
        liste.leggInn(2);
        liste.leggInn(3);
        liste.leggInn(4);
        liste.oppdater(0,-1);
        System.out.println(liste.omvendtString());
        System.out.println(liste.hent(0));
        System.out.println(liste.toString());

    }
}
