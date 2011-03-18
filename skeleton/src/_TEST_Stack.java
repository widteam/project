
public class _TEST_Stack {
	public  StackTraceElement[] stack  =new Throwable().fillInStackTrace().getStackTrace();
	public  String ClassName = stack[1].getClassName();
	public  String MethodName  = stack[1].getMethodName();
	public  int TreeDepth  = stack.length -3 ;
	public  String CallerClassName = stack[2].getClassName();
	public  String CallerMethodName = stack[2].getMethodName();

	public void PrintHeader(String Parameters, String Returnvalue){
		Parameters  = "strFIlePAth:String";	// Paraméterek, kézzel álíltandó
		Returnvalue = "";	// Visszatérési érték, kézzel állítandó
		for(int i=0;i<TreeDepth;i++) System.out.print('\t');
		System.out.print(CallerClassName + "-->");
		System.out.print(ClassName+"|"+MethodName+"("+Parameters+")"+"CALLED \n" );		
	}
	
	public void PrintTail(String Parameters, String Returnvalue){
		
		Parameters  = "strFIlePAth:String";	// Paraméterek, kézzel álíltandó
		Returnvalue = "";	// Visszatérési érték, kézzel állítandó
		
		for(int i=0;i<TreeDepth;i++) System.out.print('\t');
		System.out.print(CallerClassName + "<--");
		System.out.print(ClassName+"|"+MethodName+Parameters+" RETURNED[" + Returnvalue +"]\n" );	
	}	

}
