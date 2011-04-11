Tubeee_sandbox
Ebben vannak a fajlok - azert uj mappaban mert a masik harom (TheCode, Source, skeleton) mappakba Csomak dolgozott
Mivel a delutan-este folyaman en is nekialltam, vannak valtozasok.

Tubeee_sandbox/
 - teszt1.bhdl  - egyszeru aramkor leirasa
 - teszt2.bhdl  - stabilan visszacsatolt aramkor
 - teszt3.bhdl  - instabil aramkor
 - teszt4.bhdl  - bonyolult aramkor

Tubeee_sandbox/src/
Kodok. 
DigitalBoard.java 
 - Beolvas egy megadott fajlt.
 - Ha kezzel fel van epitve egy aramkor (egy objektumlista es egy wirelist) akkor felepiti a hierarchiat (ComponentList)
   ket aramkort (teszt1, teszt2) epitettem fel kezzel.
DigitalObject.java
 - Egy getType() metodus implementalva. ID alapjan hatarozza meg.



Akarki csinalja, ToDo:
 Hianyzik az AddOutput melle egy AddInput... (szerintem)
 Kulonbozo konstruktorok letrehozasa, ha pl a kodban mygenerator szerepel, legyen az az ID, ne pedig az automatikus