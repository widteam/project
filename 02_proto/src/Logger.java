import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {
	
	public static enum log_levels{LOW,MEDIUM,HIGH,EXTREME}
	public static enum log_type{ERROR,INFO,USER,DEBUG,ADDITIONAL}
	public static log_levels logging_level=log_levels.LOW;
	public static int log_mode = 0;
	public static String log_file="wid_test_log.log";
	
	//Ha ALL - a logging_level es a 
	public static void Log(log_type prio,String InputForLog){
		if(logging_level == log_levels.LOW){
			if(prio == log_type.ERROR || prio == log_type.INFO)
				if(log_mode == 0){
					WriteToStandardOutput(InputForLog);
				}
				else if(log_mode == 1){
					WriteToFile(InputForLog);
				}
				else if(log_mode == 2){
					WriteToFile(InputForLog);
					WriteToStandardOutput(InputForLog);
				}			
		}else if(logging_level == log_levels.MEDIUM){ 
			if(prio == log_type.ERROR || prio == log_type.INFO || prio == log_type.USER ){
					if(log_mode == 0){
						WriteToStandardOutput(InputForLog);
					}
					else if(log_mode == 1){
						WriteToFile(InputForLog);
					}
					else if(log_mode == 2){
						WriteToFile(InputForLog);
						WriteToStandardOutput(InputForLog);
					}	
			}
		}else if(logging_level == log_levels.HIGH){ 
				if(prio != log_type.USER && (prio == log_type.ERROR || prio == log_type.INFO || prio == log_type.DEBUG)){
						if(log_mode == 0){
							WriteToStandardOutput(InputForLog);
						}
						else if(log_mode == 1){
							WriteToFile(InputForLog);
						}
						else if(log_mode == 2){
							WriteToFile(InputForLog);
							WriteToStandardOutput(InputForLog);
						}	
				}
		}else if(logging_level == log_levels.EXTREME){ 
				if(true){
						if(log_mode == 0){
							WriteToStandardOutput(InputForLog);
						}
						else if(log_mode == 1){
							WriteToFile(InputForLog);
						}
						else if(log_mode == 2){
							WriteToFile(InputForLog);
							WriteToStandardOutput(InputForLog);
						}	
				}
		}
	}
	
	
	private static void WriteToFile(String InputForLog ){
		FileOutputStream fos;
		PrintStream ps;
		PrintStream orig_out = System.out;
		File f = new File(log_file);
		if(!f.exists()){
		      try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			fos = new FileOutputStream(log_file,true);
			ps = new PrintStream(fos);
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		Date date= new Date();
		SimpleDateFormat formatter;		
		formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss:SSS");
		String s = formatter.format(date);

		System.out.println(s + "\t" + InputForLog);
		System.setOut(orig_out);

	}
	private static void WriteToStandardOutput(String InputForLog){
		System.out.println(InputForLog);
	}
}
