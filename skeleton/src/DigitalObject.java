/*
* N�v: 			DigitalObject
* T�pus: 		Abstract Class
* Interfacek:	iComponent
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

public abstract class DigitalObject implements iComponent {
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
	
	/*  KONSTRUKTOR  */
	public DigitalObject(){
	}
	/*  MET�DUSOK  */
	public String GetID(){
	// Le�r�s: Visszaadja az objektumok egyedi azonos�t�j�t
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", ID + ":String");	/* TEST */		
		stack.PrintTail(ID,"", ID + ":String"); 	/* TEST */
		return null;
	}
}
