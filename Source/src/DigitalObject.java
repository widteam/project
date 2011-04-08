/*
* N�v: 			DigitalObject
* T�pus: 		Abstract Class
* Interfacek:	---
* Sz�l�k		---
* 
*********** Le�r�s **********
* A Wire mellett ez a m�sik legfontosabb �p�t�eleme a digit�lis
* �ramk�r�knek. Meghat�rozz�k, megjelen�tik, vagy egy  bels� f�ggv�ny 
* szerint a kimenet�kre csatlakoz� Wire t�pus� objektumok �rt�keit 
* megv�ltoztatj�k. Minden DigitalObject t�pus� objektum egyedi azonos�t�val
* rendelkezik.

*/

/*  IMPORTOK  */
import java.util.*;

public abstract class DigitalObject {
	/*	ATTRIB�TUMOK	*/
	protected String ID;
	// Le�r�s: Egyedi azonos�t�. Tartalmazza az oszt�ly nev�t �s egy sz�mot
	
	protected List<Wire> wireIn;
	/* Le�r�s: Wire objektum-referenci�kb�l �ll� lista azon Wire objektumokr�l, 
	 * melyek az objektum bemeneteihez kapcsol�dnak.
	*/
	
	protected List<Wire> wireOut;
	/* Le�r�s: Wire objektum-referenci�kb�l �ll� lista azon Wire objektumokr�l,
	 * melyek az objektum kimeneteihez kapcsol�dnak	
	*/
	protected List<DigitalObject>Feedbacks;
	/* Le�r�s: Ha egy Gate egyik bemenete egy visszacsatol�s kezdete, 
	 * akkor tartalmaz egy Feedbacks t�mb�t, mely referenci�t t�rol az �sszes, 
	 * az adott visszacsatol�sban r�sztvev� DigitalObject-re
	*/
	
	/*  KONSTRUKTOR  */
	public DigitalObject(){
	}
	/*  MET�DUSOK  */
	abstract public int Count();
	/* LE�r�s: Ha a DigitalObject kapu vagy kimenet, akkor a bemen� vezet�keir�l
	 * lek�rdezi az �rt�ket, kisz�molja a logikai f�ggv�ny�t. 
	 * Ha a DigitalObject kapu vagy bemenet, akkor be�ll�tja a kimen� 
	 * vezet�keinek az �rt�k�t a kor�bbi sz�m�t�si eredm�ny�re vagy a bels�
	 * �rt�k�re.
	*/	
	abstract public boolean Step();
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
	abstract public void AddToFeedbacks(DigitalObject object);
	// Le�r�s: Hozz�adja a param�terk�nt kapott DigitalObjectet a Feedbacks t�mbj�hez.
	
	public String GetID(){
	// Le�r�s: Visszaadja az objektumok egyedi azonos�t�j�t
		return ID;
	}
}
