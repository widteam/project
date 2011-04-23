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
	 */
	public int Count() {;return 0;}

	/**
	 * a PIN egy lepesnel nem csinal mast, csak a bemeno drotjanak erteket tovabbitja 
	 * a kimeno drotjanak bemenetere.
	 */
	public boolean Step() {
		// LOGOLAS
		Logger.Log(Logger.log_type.DEBUG, "<" + this.GetName() + " step>");		
			
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
