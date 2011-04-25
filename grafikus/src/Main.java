public class Main {
	public static void main(String[] args) {
		Controller c = new Controller();
	}

}
/*				
				// setFrequency [sfreq]
				if (command.equalsIgnoreCase("setFrequency") || command.equalsIgnoreCase("sfreq")) {
					try {
						Board.SetFrequency(Integer.parseInt(param2), param1);
					} catch (NumberFormatException e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: Wrong Parameter: /" + param2 + "/");
					} catch (ExceptionObjectNotFound e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e1.ItemID + "/");
					}catch(ExceptionWrongParameter e){
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: WrongParameter: A frekvencianak pozitiv szamnak kell lennie!");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
					}
				} 
				// setSample [ssampl]
				else if (command.equalsIgnoreCase("setSample") || command.equalsIgnoreCase("ssampl")) {
					try {
						Board.SetSample(Integer.parseInt(param2), param1);
					} catch (NumberFormatException e2) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: Wrong Parameter: /" + param2 + "/");
					} catch (ExceptionObjectNotFound e2) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e2.ItemID + "/");
					}catch(ExceptionWrongParameter e){
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: WrongParameter: A minta meretenek pozitiv szamnak kell lennie!");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
					}
				}
				
				// setOutput
				else if (command.equalsIgnoreCase("setOutput")) {
					Logger.Log(Logger.log_type.INFO, "Output mode is set to "
							+ param1);
					Logger.log_mode = Integer.parseInt(param1);
				} 
				// setLogfile
				/*else if (command.equalsIgnoreCase("setLogfile")) {
					Logger.Log(Logger.log_type.INFO, "Log file is set to"
							+ param1);
					Logger.log_file = param1;
				
				}*/ 
				// setInterval [sint]
				/*else if (command.equals("setInterval") || command.equals("sint")) {
					Logger.Log(Logger.log_type.INFO,
							"Boards interval is set to " + param1);
					timer.setDelay(Integer.parseInt(param1));
				
				// toggleSwitch [ts] [toggle]
				} else if (command.equalsIgnoreCase("toggleSwitch") || command.equalsIgnoreCase("toggle") || command.equalsIgnoreCase("ts")) {
					try {
						Board.Toggle(param1);

					} catch (ExceptionObjectNotFound e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e1.ItemID + "/");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
					}
					
					

				// setSequence [sseq]
				} else if (command.equalsIgnoreCase("setSequence") || command.equalsIgnoreCase("sseq")) {
					try {
						Board.SetSequence(param2, param1);
					} catch (ExceptionObjectNotFound e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e1.ItemID + "/");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
					}

	}*/

