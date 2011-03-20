/*
* N�v: 			iComponent
* T�pus: 		Interface
* Interfacek:	---
* Sz�l�k		---
* 
*********** Le�r�s **********
* Interf�sz, amely a DigitalBoard oszt�ly 
 * sz�m�ra egys�ges hozz�f�r�si fel�letet 
 * biztos�t a k�l�nb�z� DigitalObject 
 * objektumokhoz

*/

public interface iComponent {
	
	/*	MET�DUSOK	*/
	public int Count();
	/* LE�r�s: Ha a DigitalObject kapu vagy kimenet, akkor a bemen� vezet�keir�l
	 * lek�rdezi az �rt�ket, kisz�molja a logikai f�ggv�ny�t. 
	 * Ha a DigitalObject kapu vagy bemenet, akkor be�ll�tja a kimen� 
	 * vezet�keinek az �rt�k�t a kor�bbi sz�m�t�si eredm�ny�re vagy a bels�
	 * �rt�k�re.
	*/
	
	public String GetID();
	// Le�r�s: Visszat�r az objektum egyedi azonos�t�j�val
	
	public boolean Step();
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, ill. 
	 * annak eld�nt�se, hogy a DigitalObject stabil-e. Ezt �gy teszi,
	 * hogy kisz�molja az �rt�k�t Count()-tal, ha egy visszacsatol�s 
	 * k�vetkezik a kimenet�n, akkor v�gigmegy a t�mb�n �s mindegyikre 
	 * kisz�moltatja az adott elem �rt�k�t. Ezt megism�teli m�g k�tszer 
	 * �gy, hogy minden alkalommal, amikor v�gzett a Feedbacks t�mb�n 
	 * val� sz�mol�ssal kisz�molja az �rt�k�t �jb�l �s megn�zi, 
	 * hogy a saj�t kor�bbi �rt�k�vel megegyez� �rt�ket sz�molt ki. 
	 * Ha a harmadik Count() ut�n megegyezik a k�t �rt�k, 
	 * akkor stabil a kapu �s true-val t�r vissza egy�bk�nt false-al.
	*/
}
