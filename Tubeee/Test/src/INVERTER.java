import java.util.ArrayList;

/**
 * <table border=0>
 * <tr align=left>
 * <th>Nev:</th>
 * <td width=30>&nbsp;&nbsp;INVERTER</td>
 * </tr>
 * <tr align=left>
 * <th>Tipus:</th>
 * <td>&nbsp;&nbsp;Class</td>
 * </tr>
 * <tr align=left>
 * <th>Interface:</th>
 * <td>&nbsp;&nbsp;---</td>
 * </tr>
 * <tr align=left>
 * <th>Szulok:</th>
 * <td>&nbsp;&nbsp;DigitalObject-->Gate</td>
 * </table>
 * <br>
 * Logikai invertert megvalosito objektum. Egyetlen egy bemenettel rendelkezik,
 * es kimenetere ennek inverzet adja, ha annak van ertelme. Igazsagtablaja a
 * kovetkezo:
 * <table border=1>
 * <tr align=center>
 * <th colspan=1>Bemenet</th>
 * <th>Kimenet</th>
 * </tr>
 * <tr align=center>
 * <th colspan>A</th>
 * <th>NOT A</th>
 * </tr>
 * <tr align=center>
 * <td>0</td>
 * <td>1</td>
 * </tr>
 * <tr align=center>
 * <td>1</td>
 * <td>0</td>
 * </tr>
 * <tr align=center>
 * <td>X</td>
 * <td>X</td>
 * </tr>
 * </table>
 * Jelentesek: 0: logikai HAMIS ertek | 1: logikai IGAZ ertek | X: don't care
 */
public class INVERTER extends Gate {
	/* ATTRIBUTUMOK */
	/** Statikus valtozo az egyedi ID ertekhez */
	private static int INVERTERCounts;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param strCompositName
	 *            A kaput tartalmazo Composit elem neve. Szukseges, hogy a
	 *            kulonbozo compositokban szerepelehssen azonos nevu objektum
	 * @param wirein1
	 *            A kapu egyetlen bemente
	 */
	public INVERTER(String strCompositName, Wire wirein1) {
		final String strIDDelimiter = "#";
		String strIDNumber = String.valueOf(INVERTERCounts++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ strClassName + strIDNumber;

		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		wireIn.add(wirein1);
		
		if(DebugMode)
			System.out.println(strClassName+" ("+ID+") has been  created automtic. (Inputs: "+ wirein1.GetID());
	}
	/* METODUSOK */
	/**
	 * Kiszamolja egy INVERTER erteket
	 * 
	 * @return A belso implementalt igazsagtabla tovabba a ket bemenet alapjan
	 *         kapott ertek
	 * @throws ElementHasNoInputsException
	 *             Ha a kapunak nincs bemenete
	 * @throws ElementInputSizeException
	 *             Ha a kapunak nincs meg a megfelelo szamu bemenete (1 darab)
	 * @throws ElementNotConnectedException
	 *             Ha a kapu kimenetehez egyetlen tovabbi elem sem csatlakozik
	 */
	public int Count() throws ConnectionsException{
		if(DebugMode)
			System.out.println("<"+this.GetID()+" Count>");
		int Result = 0;
		// Eredmeny kiszamitasa
		if (wireIn == null || wireIn.isEmpty()) {
			 throw new ElementHasNoInputsException();
		} else {
			if (wireIn.size() != 1) {
				 throw new ElementInputSizeException();
			} else {
				if (wireIn.get(0).GetValue() == 0)
					Result=1;
				else if (wireIn.get(0).GetValue() == -1)
					Result=-1;
				else
					Result=0;
			}
		}
		/*
		 * Az OSSZES kimenetre kiadjuk a kiszamitott eredmenyt. Skeletonnal csak
		 * egyre
		 */
		if (wireOut == null || wireOut.isEmpty()) { // ha nincs csatlakoztatva
													// semmihez, hibat dob
			 throw new ElementNotConnectedException();
		} else {
			for (Wire OutPut : wireOut) {
				OutPut.SetValue(Result);
			}
		}
		return Result;
	};

	/**
	 * Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese, hogy
	 * a DigitalObject stabil-e
	 * 
	 * @return Ha van a kapunak feedbacks tombje, haromszor vegigiteral a
	 *         tombelemen, s minden alkalommal osszehasonlitja a kapott
	 *         kapuerteket az elozo ertekkel. Ha az utolso ketto megegyezik,
	 *         stabil a kapu,{@code true} a visszateresi ertek, kulonben
	 *         {@code false}
	 */
	public boolean Step()throws  UnstableCircuitException, ConnectionsException  {
		System.out.println("<"+this.GetID()+" step>");
		boolean Result = true; // A vegso eredmeny: Stabil-e az aramkor
		PreviousValue = Count(); // Megnezzuk az elso futas erredmenyet

		if (Feedbacks != null && !Feedbacks.isEmpty()) {// Ha nem ures a
														// Feedback tomb
			if(DebugMode)
				System.out.println("FEEDBACK founded. First running result was " + PreviousValue);
			int NewValue; // Lokalis valtozo
			for (DigitalObject obj : Feedbacks) { // Feedback osses elemen vegig
				obj.Count();
			}
			NewValue = Count(); // Megnezzuk ujol az eredmenyt
			Result = (PreviousValue == NewValue); // Elter-e a ketto?( Prev es a
													// mostani)
			PreviousValue=NewValue;	
			if(DebugMode)
				System.out.println("Second running result is equal with the previous? "+ Result);
			
			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}
			NewValue = Count();
			Result = (PreviousValue == NewValue);
			PreviousValue=NewValue;	
			if(DebugMode)
				System.out.println("Third running result is equal with the previous? "+ Result);
			
			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}
			NewValue = Count();
			Result = (PreviousValue == NewValue);
			PreviousValue=NewValue;	
			if(!Result) throw new UnstableCircuitException();
			if(DebugMode)
				System.out.println("Last running result. Gate is stable? "+ Result);
		}
		return Result;
	};
}