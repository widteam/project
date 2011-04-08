import java.util.ArrayList;

/*
* Név: 			INVERTER
* Típus: 		Class
* Interfacek:	---
* Szülõk		DigitalObject-->Gate
* 
*********** Leírás **********
* Logikai invertert megvalósító objektum. Egyetlen egy bemenettel rendelkezik, 
* és kimenetére ennek inverzét adja, ha annak van értelme. 
* Igazságtáblája a következõ:
* 
* bemenet	kimenet
*	A		  NOT A
* 	0	  		1
*	1	  		0
*	X	  		X
* Jelentések: 0: logikai HAMIS érték | 1: logikai IGAZ érték | X: don’t care

 */
public class INVERTER extends Gate{
	/*  ATTRIBÚTUMOK  */
	private static int INVERTERCounts;
	
	
	/*  KONSTRUKTOR  */
	public INVERTER(String strCompositName,Wire wirein1){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(INVERTERCounts++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		wireIn.add(wirein1);
	}
	
	
	/*	METÓDUSOK	*/
	public int Count(){
		// Leírás: Kiszámolja egy DigitalObject értékét	
			int Result=0;
			// Eredmény kiszámítása
			if(wireIn.size()!=1 ){
				// throw GateInputSizeError
			}else{
				Result = wireIn.get(0).GetValue();
				if(Result == 0)
					Result  = 1;
				if(Result == -1)
					Result  = -1;
				else
					Result = 0;	
			}
			/* Az ÖSSZES kimenetre kiadjuk a kiszámított eredményt. Skeletonnál csak egyre */
			if(wireOut == null || wireOut.isEmpty()){	// ha nincs csatlakoztatva semmihez, hibát dob
				// throw GateNotConnectedWarning
			}else{
				for(Wire OutPut:wireOut){
					OutPut.SetValue(Result);
				}
			}
			return Result;
		};		
		public boolean Step(){
			/* Leírás: Feladata az adott elem értékének kiszámítása, 
			 * ill. annak eldöntése, hogy a DigitalObject stabil-e
			*/
				boolean Result = true;						// A végsõ eredmény: Stabil-e az áramkör

				PreviousValue = Count();					// Megnézzük az elsõ futás erredményét
				if(Feedbacks != null && !Feedbacks.isEmpty()){// Ha nem üres a Feedback tömb
					int NewValue ;						// Lokális változó
					for(DigitalObject obj:Feedbacks){	// Feedback össezs elemén végig
						obj.Count();
					}
					NewValue = Count();					// Megnézzük újól az eredményt
					Result = (PreviousValue==NewValue);	// Eltér-e a kettõ?( Prev és a mostani) 
					
					for(DigitalObject obj:Feedbacks){
						obj.Count();
					}
					NewValue = Count();
					Result = (PreviousValue==NewValue);
					
					for(DigitalObject obj:Feedbacks){
						obj.Count();
					}
					NewValue = Count();
					Result = (PreviousValue==NewValue);
				}
				return Result;
			};
}