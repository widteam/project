import java.util.ArrayList;
public class PIN extends Gate{
	/* ATTRIBUTUMOK */
	/** Statikus valtozo az egyedi ID ertekhez */
	private static int PINCounts;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param strCompositName
	 *            A kaput tartalmazo Composit elem neve. Szukseges, hogy a
	 *            kulonbozo compositokban szerepelehssen azonos nevu objektum
	 * @param wirein1
	 *            A kapu egyetlen bemente
	 */
	public PIN(String strCompositName, Wire wirein1) {
		final String strIDDelimiter = "#";
		String strIDNumber = String.valueOf(PINCounts++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ strClassName + strIDNumber;

		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		wireIn.add(wirein1);
	}
	
	public PIN(Wire wireout) {
		final String strIDDelimiter = "#";
		String strIDNumber = String.valueOf(PINCounts++);
		final String strClassName = this.getClass().getName();
		ID = "Comp_input_pin" + strIDDelimiter + strClassName + strIDDelimiter
				+ strClassName + strIDNumber;

		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		wireOut.add(wireout);
	}
	
	public PIN(String strCompositName, String pinName) {
		final String strIDDelimiter = "#";
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ pinName;
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
	}
	/* METODUSOK */
	/**
	 * Kiszamolja egy PIN erteket
	 * 
	 * @return A belso implementalt igazsagtabla tovabba a ket bemenet alapjan
	 *         kapott ertek
	 * @throws ElementHasNoInputsException
	 *             Ha a kapunak nincs bemenete
	 * @throws ElementInputSizeException
	 *             Ha a kapunak nincs meg a megfelelo szamu bemenete (1 darab)
	 * @throws GateNotConnectedException
	 *             Ha a kapu kimenetehez egyetlen tovabbi elem sem csatlakozik
	 */
	public int Count() {
		int Result = 0;
		// Eredmeny kiszamitasa
		if (wireIn == null || wireIn.isEmpty()) {
			// throw ElementHasNoInputsException;
		} else {
			if (wireIn.size() != 1) {
				// throw ElementInputSizeException
			} else {
				return Result = wireIn.get(0).GetValue();
			}
		}
		/*
		 * Az OSSZES kimenetre kiadjuk a kiszamitott eredmenyt. Skeletonnal csak
		 * egyre
		 */
		if (wireOut == null || wireOut.isEmpty()) { // ha nincs csatlakoztatva
													// semmihez, hibat dob
			// throw GateNotConnectedException
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
	public boolean Step() {
		boolean Result = true; // A vegso eredmeny: Stabil-e az aramkor

		PreviousValue = Count(); // Megnezzuk az elso futas erredmenyet
		if (Feedbacks != null && !Feedbacks.isEmpty()) {// Ha nem ures a
														// Feedback tomb
			int NewValue; // Lokalis valtozo
			for (DigitalObject obj : Feedbacks) { // Feedback ossezs elemen
													// vegig
				obj.Count();
			}
			NewValue = Count(); // Megnezzuk ujol az eredmenyt
			Result = (PreviousValue == NewValue); // Elter-e a ketto?( Prev es a
													// mostani)

			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}
			NewValue = Count();
			Result = (PreviousValue == NewValue);

			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}
			NewValue = Count();
			Result = (PreviousValue == NewValue);
		}
		return Result;
	};
}
