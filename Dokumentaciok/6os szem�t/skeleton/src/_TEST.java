/**
 * 
 * Skeleton tesztelését segítõ osztály.
 *
 */
public class _TEST {
	public static int DebugMode = 0; //0 - System.out   1 - Filestream
	public  StackTraceElement[] stack  =new Throwable().fillInStackTrace().getStackTrace();
	public  String ClassName = stack[1].getClassName();
	public  String MethodName  = stack[1].getMethodName();
	public  int TreeDepth  = stack.length -3 ;
	public  String CallerClassName = stack[2].getClassName();
	public  String CallerMethodName = stack[2].getMethodName();

	//class hívás trace
	public void PrintHeader(String CallerObjectName, String Parameters, String Returnvalue){
		for(int i=0;i<TreeDepth;i++) System.out.print('\t');
		System.out.print(CallerClassName + "-->");
		System.out.print(CallerObjectName+":"+ClassName+"|"+MethodName+"("+Parameters+")"+" CALLED \n" );		
	}
	
	//class return trace
	public void PrintTail(String ObjectName, String Parameters, String Returnvalue){		
		for(int i=0;i<TreeDepth;i++) System.out.print('\t');
		System.out.print(CallerClassName + "<--");
		System.out.print(ObjectName+":"+ClassName+"|"+MethodName+"("+Parameters+")"+" RETURNED[" + Returnvalue +"]\n" );	
	}	

}
