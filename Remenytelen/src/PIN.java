public class PIN extends DigitalObject{
	private static int PINCounts;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void AddToFeedbacks(DigitalObject object) {
		// TODO Auto-generated method stub
		
	}
	public PIN(Composit composit) {
		String strCompositName = composit.GetName();
		final String strIDDelimiter = "#";
		String strIDNumber = String.valueOf(PINCounts++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+strClassName +  strIDNumber;
		ContainerComposit = composit;
		/**
		 * DEBUG
		 */
		System.out.println(strClassName+" ("+strClassName + strIDNumber+") has been  created automtic.");
	}

}
