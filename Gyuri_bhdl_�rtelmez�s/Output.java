package bhdlparser;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;Output</td>
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
* A bemenet megjelenitesere szolgalo objektum/osztaly
*/
public abstract class Output extends DigitalObject{
	/*  ATTRIBUTUMOK  */
	/**
	 * Az adott Output objektum erteket tarolja
	 */
	protected int Value;
		
	/*	METODUSOK	*/
	/**
	 * Az Output osztalynak nincs feedback tombje, igy  a metodus nincs implementalva
	 */
	public void AddToFeedbacks(DigitalObject object) {
		;		// Outputnak nincs feedbackje.
	};
}
