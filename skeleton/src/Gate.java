/*
* N�v: 			Gate
* T�pus: 		Abstract Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject
* 
*********** Le�r�s **********
* Absztrakt oszt�ly a logikai kapuk (pl. �S, VAGY, Inverter) lesz�rmaztat�s�ra.
* A bemeneti vezet�k(ek)nek lek�rdezi az �rt�k�t, �s kisz�molja a kimenet �j
* �rt�k�t, �s be is �ll�tja azt, az lesz�rmazott oszt�ly igazs�gt�bl�ja szerint

*/

/*  IMPORTOK  */
import java.util.*;

public abstract class Gate extends DigitalObject{
	/*	ATTRIB�TUMOK	*/
	protected int PreviousValue ;
	/* Le�r�s: A legutols� ciklus ( Step() ) eredm�ny�t (kimeneti �rt�k�t) t�rolja,
	 *  a stabilit�sellen�rz�s c�lj�b�l (Count() met�dus).
	*/
	
	private ArrayList<DigitalObject>Feedbacks;
	/* Le�r�s: Ha egy Gate egyik bemenete egy visszacsatol�s kezdete, 
	 * akkor tartalmaz egy Feedbacks t�mb�t, mely referenci�t t�rol az �sszes, 
	 * az adott visszacsatol�sban r�sztvev� DigitalObject-re
	*/
	public Gate(){
		PreviousValue = -1;
	}
	
	/*	MET�DUSOK	*/
	public void AddToFeedbacks(DigitalObject feedback){
	// Le�r�s: Hozz�adja a param�terk�nt kapott DigitalObjectet a Feedbacks t�mbj�hez.
	};	
}
