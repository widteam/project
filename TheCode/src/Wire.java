/*
* N�v: 			Wire
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		---
* 
*********** Le�r�s **********
 * Az �ramk�rben tal�lhat� �rt�kek t�rol�s�ra szolg�l� objektum. 
 * DigitalObject t�pus� objektumok k�z�tt teremt kapcsolatot.

*/

/*  IMPORTOK  */
import java.util.*;

public class Wire{
	/*  ATTRIB�TUMOK  */
	private static int WIRECounts;	// Statikus v�ltoz� az egyedi ID �rt�khez
		
	private String ID;
	/* Le�r�s: Egyedi karakteres azonos�t�, mely egy�rtelm�en meghat�roz 
	 * egy Wire objektumot.
	*/
	
	private int Value;
	/* Le�r�s: A Wire objektumok �ltal t�rolt �rt�k. 
	 * A Gate objektumok ezek alapj�n sz�molj�k ki kimenet�ket
	*/
	
	private List<DigitalObject> wireIn;
	/* Le�r�s: DigitalObject objektum-referencia, amely a vezet�k 
	 * bemenet�hez kapcsol�dik.
	*/
	
	private List<DigitalObject> wireOut;
	/* Le�r�s: DigitalObject objektum-referencia, amely a vezet�k 
	 * kimenet�hez kapcsol�dik.
	*/
	
	/*  KONSTRUKTOR  */
	public Wire(){
		ID = "WIRE" + String.valueOf(WIRECounts++);
	}
	/*	MET�DUSOK	*/
	public String GetID(){
	// Le�r�s: A Wire egyedi azonos�t�j�nak lek�rdez�s�re szolg�l� met�dus.
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		return null; 
	
	};
	public int GetValue(){
	// Le�r�s: A Wire objektum �rt�k�nek lek�rdez�s�re szolg�l� met�dus
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		return 0; 
	};
	public void SetValue(int NewValue){
	// Le�r�s: // Le�r�s: A Wire objektum �rt�k�nek be�ll�t�s�ra szolg�l� met�dus
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
	};
	public void SetConnection(DigitalObject ojectWhere, DigitalObject objectWhat){
	/* Le�r�s: Kapcsolatot teremt k�t DigitalObject k�z�tt. 
	 * Be�ll�tja a vezet�k inputj�t (ojectWhere), illetve hozz�adja az output t�mbj�hez 
	 * a DigitalObjectet (objectWhat).
	 */
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
	};
}
