package wrapper;

import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class WrapperForTrello implements TrelloWrapper
	{
		static String trelloURL = "https://api.trello.com";
		String APIKey = "";
		String acessToken = "";
		String currentCardId = "";
		String currentCardUrl = "";
		String currentCheckListId = "";

		@Override
		public void setApikeyAndAcessToken( String apiKey , String acessTkn )
			{
				APIKey = apiKey;
				acessToken = acessTkn;
			}

		@Override
		public void createCard( String boardId ,String listName, String cardNmae , String description , WebDriver driverr )	throws InterruptedException ,
																											ParseException
			{
				String listId= getListIdWithName(boardId,listName,driverr);
				String cardUrl = trelloURL + "/1/cards?name=" + cardNmae + "&desc=" + description + "&idList=" + listId + "&key=" + APIKey
						+ "&token=" + acessToken + "";
				Object cardResponse = post( cardUrl , driverr );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				currentCardId = (String) json.get( "id" );
				currentCardUrl = (String) json.get( "url" );

			}

		@Override
		public void createCheckList( String cardId , String checkListname , WebDriver driverr ) throws InterruptedException , ParseException
			{

				String checklistUrl = trelloURL + "/1/checklists?idCard=" + cardId + "&name=" + checkListname + "&key=" + APIKey + "&token="
						+ acessToken + "";
				Object cardResponse = post( checklistUrl , driverr );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				currentCheckListId = (String) json.get( "id" );

			}

		@Override
		public void createCheckListItem( String checklistId , String name , boolean checkornot , WebDriver driverr ) throws InterruptedException ,
																													ParseException
			{

				String checklistItemUrl = trelloURL + "/1/checklists/" + checklistId + "/checkItems?name=" + name + "&checked=" + checkornot
						+ "&key=" + APIKey + "&token=" + acessToken + "";
				Object cardResponse = post( checklistItemUrl , driverr );
				System.out.println( checklistItemUrl );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				currentCheckListId = (String) json.get( "id" );

			}

		@Override
		public void addMembers( String cardId , String name , WebDriver driverr ) throws InterruptedException , ParseException
			{

				String userId = getUserId( name , driverr );
				String addMembers = trelloURL + "/1/cards/" + cardId + "/idMembers?value=" + userId + "&key=" + APIKey + "&token=" + acessToken + "";
				Object cardResponse = post( addMembers , driverr );
				System.out.println( addMembers );
			}

		@Override
		public void addComments( String cardId , String comment , WebDriver driverr ) throws InterruptedException , ParseException
			{
				String addComments = trelloURL + "/1/cards/" + cardId + "/actions/comments?text=" + comment + "&key=" + APIKey + "&token="
						+ acessToken + "";
				Object cardResponse = post( addComments , driverr );
			}

		@Override
		public void addAttachments( String cardId , String name , String base64 , WebDriver driverr )
			{
				// TODO Auto-generated method stub

				String attachementURL = trelloURL + "/1/cards/" + cardId + "/attachments/";
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

				Object responsee = js.executeAsyncScript( "var data = JSON.stringify(false);" + "var callback = arguments[arguments.length - 1];"
						+ "var xhr = new XMLHttpRequest();" + "xhr.withCredentials = false;" + "xhr.onreadystatechange = function() {"
						+ "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" +

						"xhr.open(\"POST\",\"" + attachementURL + "\");" + "xhr.send(formData);" );

			}

		public String getListIdWithName(  String boardId ,String name , WebDriver driverr ) throws InterruptedException , ParseException
			{

				String id = "";
				String result = "";
				String getListIdURL = "https://api.trello.com/1/boards/" + boardId + "/lists?key=" + APIKey + "&token=" + acessToken + "";
				Object cardResponse = get( getListIdURL , driverr );
				JSONArray listArray = (JSONArray) new JSONParser().parse( (String) cardResponse );
				for ( int i = 0 ; i < listArray.size() ; i++ )
					{

						JSONObject listObject = (JSONObject) listArray.get( i );
						String listName = (String) listObject.get( "name" );
						if ( listName.trim().equalsIgnoreCase( name.trim() ) )
							{
								id = (String) listObject.get( "id" );

							}

					}
				if ( id.isEmpty() )
					{
						result = "Please check your list name";
					}
				else
					{
						result = id;
					}

				return result;
			}

		public String getUserId( String name , WebDriver driverr ) throws InterruptedException , ParseException
			{

				String getname = trelloURL + "/1/members/" + name;
				Object cardResponse = get( getname , driverr );
				JSONObject json = (JSONObject) new JSONParser().parse( (String) cardResponse );
				String id = (String) json.get( "id" );
				return id;

			}

		public Object post( String url , WebDriver dvr ) throws InterruptedException , ParseException
			{

				JavascriptExecutor js = (JavascriptExecutor) dvr;
				dvr.manage().timeouts().setScriptTimeout( 60 , TimeUnit.SECONDS );
				Object response = js.executeAsyncScript( "var data = JSON.stringify(false);" + "var callback = arguments[arguments.length - 1];"
						+ "var xhr = new XMLHttpRequest();" + "xhr.withCredentials = false;" + "xhr.onreadystatechange = function() {"
						+ "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" +

						"xhr.open(\"POST\",\"" + url + "\");" + "xhr.send(data);" );

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

				return response;

			}

	}
