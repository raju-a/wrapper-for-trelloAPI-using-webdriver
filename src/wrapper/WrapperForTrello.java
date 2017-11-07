package wrapper;

import java.util.ArrayList;
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
		public void createCard( String boardId , String listName , String cardNmae , String description , WebDriver driverr )	throws InterruptedException ,
																																ParseException
			{
				String listId = getListIdWithName( boardId , listName , driverr );
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

		@Override
		public void archive( String boardId , String listName , String cardName , WebDriver driverr ) throws InterruptedException , ParseException
			{
				// TODO Auto-generated method stub
				String cardId = getCardIdWithName( boardId , listName , cardName , driverr );
				String deleteURL = "https://api.trello.com/1/cards/" + cardId;
				delete( deleteURL , driverr );

			}

		public String getCardIdWithName( String boardId , String listName , String cardName , WebDriver driverr )	throws InterruptedException ,
																													ParseException
			{

				String cardId = null;
				String listId = getListIdWithName( boardId , listName , driverr );
				String getCardsURL = "https://api.trello.com/1/lists/" + listId + "/cards?key=" + APIKey + "&token=" + acessToken + "";
				Object cardResponse = get( getCardsURL , driverr );
				JSONArray cardsArray = (JSONArray) new JSONParser().parse( (String) cardResponse );
				for ( int i = 0 ; i < cardsArray.size() ; i++ )
					{

						JSONObject listObject = (JSONObject) cardsArray.get( i );
						String cardsName = (String) listObject.get( "name" );
						if ( cardName.trim().equalsIgnoreCase( cardsName.trim() ) )
							{
								cardId = (String) listObject.get( "id" );
								break;
							}

					}
				return cardId;

			}

		public String getListIdWithName( String boardId , String name , WebDriver driverr ) throws InterruptedException , ParseException
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
								break;
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

		public Object delete( String url , WebDriver dvr ) throws InterruptedException , ParseException
			{

				JavascriptExecutor js = (JavascriptExecutor) dvr;
				dvr.manage().timeouts().setScriptTimeout( 60 , TimeUnit.SECONDS );
				Object response = js.executeAsyncScript( "var data = JSON.stringify(false);" + "var callback = arguments[arguments.length - 1];"
						+ "var xhr = new XMLHttpRequest();" + "xhr.withCredentials = false;" + "xhr.onreadystatechange = function() {"
						+ "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" +

						"xhr.open(\"DELETE\",\"" + url + "\");" + "xhr.send(data);" );

				return response;

			}

		@Override
		public ArrayList <String> getCheckListItem( String boardId , String listName , String cardName , String checkListName , WebDriver driverr )	throws InterruptedException ,
																																					ParseException
			{

				JSONArray checkListItemsArray = null;
				ArrayList <String> allCheckListItem = new ArrayList <String>();
				String cardId = getCardIdWithName( boardId , listName , cardName , driverr );

				String checkListsURL = "https://api.trello.com/1/cards/" + cardId + "/checklists?key=" + APIKey + "&token=" + acessToken + "";
				Object cardResponse = get( checkListsURL , driverr );
				JSONArray checkListArray = (JSONArray) new JSONParser().parse( (String) cardResponse );
				for ( int i = 0 ; i < checkListArray.size() ; i++ )
					{

						JSONObject listObject = (JSONObject) checkListArray.get( i );
						String checkListsName = (String) listObject.get( "name" );

						if ( checkListName.trim().equalsIgnoreCase( checkListsName.trim() ) )
							{

								checkListItemsArray = (JSONArray) listObject.get( "checkItems" );

								break;
							}

					}

				for ( int i = 0 ; i < checkListItemsArray.size() ; i++ )
					{

						JSONObject listObject = (JSONObject) checkListItemsArray.get( i );
						allCheckListItem.add( (String) listObject.get( "name" ) );

					}

				return allCheckListItem;

			}

	}
