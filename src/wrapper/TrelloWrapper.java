package wrapper;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;

public interface TrelloWrapper
	{

		public void setApikeyAndAcessToken( String apiKey , String acessTkn );

		public void creatCard( String listId , String cardNmae , String description , WebDriver driverr )	throws InterruptedException ,
																											ParseException;

		public void addMembers(  String cardId ,String name , WebDriver driverr ) throws InterruptedException , ParseException;

		public void creatCheckList( String cardId , String checkListname , WebDriver driverr ) throws InterruptedException , ParseException;

		public void creatCheckListItem( String checklistId , String name , boolean checkornot , WebDriver driverr )	throws InterruptedException ,
																													ParseException;
        
		public void addComments( String cardId , String comment , WebDriver driverr ) throws InterruptedException , ParseException;
	    
		public void addAttachments(String cardId, String name, String base64,WebDriver driverr );
		
	}
