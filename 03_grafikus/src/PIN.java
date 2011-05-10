/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;PIN</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Tipus:</th>
 * 		<td>&nbsp;&nbsp;Class</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Interface: </th>
 * 		<td>&nbsp;&nbsp;---</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Szulok:</th>
 * 		<td>&nbsp;&nbsp;DigitalObject</td>
 * </table> 
 *<br>
 * Specialis DigitalObject ami egy Compositba beagyazva egyszerre rejti el
 * a kulvilagtol azt, illetve tartja fent a kapcsolatot a kornyezetevel.
 * Egyetlen Wire tipusu bemenetenek erteket minden egyes lepesben 
 * atalakitas nelkul tovabbitja a kimenetere.
 * @author Tamas
*/

public class PIN extends DigitalObject{
	/** A PIN egyetlen nemeno drotja */
	public Wire wireIn;
	/** a PIN egyetlen kimeno drotja */
	public Wire wireOut;
	/** Referencia  PIN-t tartalmazo Compositra */
	public Composit ContainerComposit;
	
	/**
	 * a PIN-is DigitalObject, ezert van Count fugvenye. AZonban
	 * nincsen erteke amit ki lehetne szamitani, ezert a metodus nincs
	 * implementalva.
	 * @return 0, a funkcio nincs implementalva
	 */
	public int Count() {;return 0;}

	/**
	 * a PIN egy lepesnel nem csinal mast, csak a bemeno drotjanak erteket tovabbitja 
	 * a kimeno drotjanak bemenetere.
	 */
	public boolean Step() {
		// LOGOLAS
		Logger.Log(Logger.log_type.DEBUG, "<" + this.GetName() + " Step>");		
			
		wireOut.SetValue(wireIn.GetValue());
		return true;
	}

	/**
	 * Egy PIN nem is lehet visszacsatolas resze, ez sincs implementalva
	 */
	public void AddToFeedbacks(DigitalObject object) {;}
	
	/**
	 * KONSTRUKTOR
	 * Letrehoz egy PIN-t adott nevvel
	 * @param composit a PIN-t magaba agyazo Composit
	 * @param PINName a PIN kivant neve
	 */
	public PIN(Composit composit, String PINName) {
		String strCompositName = composit.GetName();
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+PINName;
		ContainerComposit = composit;

		// LOGOLAS
		Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created automtic.");
	}

}
