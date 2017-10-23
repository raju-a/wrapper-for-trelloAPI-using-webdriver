package wrapper;

import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class WrapperForTrello implements TrelloWrapper
	{

		String APIKey = "";
		String acessToken = "";
		String currentCardId = "";
		String currentCardUrl = "";
		String currentCheckListId = "";

		public void setApikeyAndAcessToken( String apiKey , String acessTkn )
			{
				System.out.print( apiKey );

				APIKey = apiKey;
				acessToken = acessTkn;
				System.out.print( acessToken );
			}

		public void creatCard( String listId , String cardNmae , String description , WebDriver driverr )	throws InterruptedException ,
																											ParseException
			{

				String cardUrl = "https://api.trello.com/1/cards?name=" + cardNmae + "&desc=" + description + "&idList=" + listId + "&key=" + APIKey
						+ "&token=" + acessToken + "";
				Object cardResponse = post( cardUrl , driverr );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				currentCardId = (String) json.get( "id" );
				System.out.println( 7 + currentCardId );
				currentCardUrl = (String) json.get( "url" );
				System.out.println( 8 + currentCardUrl );
			}

		public void creatCheckList( String cardId , String checkListname , WebDriver driverr ) throws InterruptedException , ParseException
			{

				String checklistUrl = "https://api.trello.com/1/checklists?idCard=" + cardId + "&name=" + checkListname + "&key=" + APIKey
						+ "&token=" + acessToken + "";
				Object cardResponse = post( checklistUrl , driverr );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				currentCheckListId = (String) json.get( "id" );

			}

		public void creatCheckListItem( String checklistId , String name , boolean checkornot , WebDriver driverr )	throws InterruptedException ,
																													ParseException
			{

				String checklistItemUrl = "https://api.trello.com/1/checklists/" + checklistId + "/checkItems?name=" + name + "&checked="
						+ checkornot + "&key=" + APIKey + "&token=" + acessToken + "";
				Object cardResponse = post( checklistItemUrl , driverr );
				System.out.println( checklistItemUrl );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				currentCheckListId = (String) json.get( "id" );

			}

		public void addMembers(  String cardId ,String name , WebDriver driverr ) throws InterruptedException , ParseException
			{

				String userId = getUserId( name , driverr );
				String addMembers = "https://api.trello.com/1/cards/" + cardId + "/idMembers?value=" + userId + "&key=" + APIKey + "&token="
						+ acessToken + "";
				Object cardResponse = post( addMembers , driverr );
				System.out.println( addMembers );
			}

		@Override
		public void addComments( String cardId , String comment , WebDriver driverr ) throws InterruptedException , ParseException
			{
				String addComments = "https://api.trello.com/1/cards/" + cardId + "/actions/comments?text=" + comment + "&key=" + APIKey + "&token="
						+ acessToken + "";
				Object cardResponse = post( addComments , driverr );
			}
		
		@Override
		public void addAttachements( String cardId , String name , String base64 , WebDriver driverr ){
				// TODO Auto-generated method stub

				String attachementURL = "https://api.trello.com/1/cards/" + cardId + "/attachments/";
				JavascriptExecutor js = (JavascriptExecutor) driverr;
				driverr.manage().timeouts().setScriptTimeout( 60 , TimeUnit.SECONDS );
				js.executeScript( "window.b64Data=\"" + base64 + "\"" + ";" + "window.byteCharacters = atob(b64Data);"

				+ "window.byteNumbers = new Array(byteCharacters.length);"

				+ "for (window.i = 0; i < byteCharacters.length; i++) { byteNumbers[i] = byteCharacters.charCodeAt(i);}"

				+ "byteNumbers[i] = byteCharacters.charCodeAt(i);"

				+ "window.byteArray = new Uint8Array(byteNumbers);"

				+ "window.blob = new Blob([byteArray], {type: 'image/png'});"

				+ "window.file = new File([blob], \"" + name + "\"" + "," + "{type:\"" + "image/png" + "\"" + "});"

				+ "window.formData = new FormData();" + "formData.append(\"" + "key" + "\"" + "," + "\"" + APIKey + "\"" + ");"
						+ "formData.append(\"" + "token" + "\"" + "," + "\"" + acessToken + "\"" + ");" + "formData.append(\"" + "file\""
						+ ", file);" + "formData.append(\"" + "name\"" + "," + "\"" + name + "\"" + ");" + "formData.append(\"" + "token" + "\""
						+ "," + "\"" + acessToken + "\"" + ");" );
				System.out.println( "try got it" );

				Object responsee = js.executeAsyncScript( "var data = JSON.stringify(false);" + "var callback = arguments[arguments.length - 1];"
						+ "var xhr = new XMLHttpRequest();" + "xhr.withCredentials = false;" + "xhr.onreadystatechange = function() {"
						+ "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" +

						"xhr.open(\"POST\",\"" + attachementURL + "\");" + "xhr.send(formData);" );

			}

		public String getUserId( String name , WebDriver driverr ) throws InterruptedException , ParseException
			{

				String getname = "https://api.trello.com/1/members/" + name;
				Object cardResponse = get( getname , driverr );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				String id = (String) json.get( "id" );
				return id;

			}

		// https://api.trello.com/1/cards/id/attachments

		public Object post( String url , WebDriver dvr ) throws InterruptedException , ParseException
			{

				JavascriptExecutor js = (JavascriptExecutor) dvr;
				dvr.manage().timeouts().setScriptTimeout( 60 , TimeUnit.SECONDS );
				Object response = js.executeAsyncScript( "var data = JSON.stringify(false);" + "var callback = arguments[arguments.length - 1];"
						+ "var xhr = new XMLHttpRequest();" + "xhr.withCredentials = false;" + "xhr.onreadystatechange = function() {"
						+ "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" +

						"xhr.open(\"POST\",\"" + url + "\");" + "xhr.send(data);" );

				System.out.println( "response :::" + response );

				return response;

			}

		public Object get( String url , WebDriver dvr ) throws InterruptedException , ParseException
			{

				JavascriptExecutor js = (JavascriptExecutor) dvr;
				dvr.manage().timeouts().setScriptTimeout( 60 , TimeUnit.SECONDS );
				Object response = js.executeAsyncScript( "var data = JSON.stringify(false);" + "var callback = arguments[arguments.length - 1];"
						+ "var xhr = new XMLHttpRequest();" + "xhr.withCredentials = false;" + "xhr.onreadystatechange = function() {"
						+ "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" +

						"xhr.open(\"GET\",\"" + url + "\");" + "xhr.send(data);" );

				System.out.println( "response :::" + response );

				return response;

			}

		

	}
