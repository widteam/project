public class PIN extends DigitalObject{
	public Wire wireIn;
	public Wire wireOut;
	public Composit ContainerComposit;
	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean Step() {
		if(DebugMode)
			System.out.println("<" + this.GetName() + " step>");

		wireOut.SetValue(wireIn.GetValue());
		return true;
	}

	@Override
	public void AddToFeedbacks(DigitalObject object) {
		// TODO Auto-generated method stub
		
	}
	public PIN(Composit composit, String PINName) {
		String strCompositName = composit.GetName();
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+PINName;
		ContainerComposit = composit;

		if (DebugMode)
			System.out.println(strClassName+" ("+PINName+") has been  created automtic.");
	}

}
