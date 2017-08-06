package wrapper;

import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class WrapperForTrello {

	String APIKey="";
	String acessToken="";
	String currentCardId="";
	String currentCardUrl="";
	String currentCheckListId="";


	public void setApikeyAndAcessToken(String apiKey, String acessTkn){
		System.out.print(apiKey);
		
		APIKey=apiKey;
		acessToken=acessTkn;
		System.out.print(acessToken);
	}
	
	public void creatCard(String listId, String cardNmae,WebDriver driverr) throws InterruptedException, ParseException{
	
		String cardUrl="https://api.trello.com/1/cards?name="+cardNmae+"&idList="+listId+"&key="+APIKey+"&token="+acessToken+""; 
		Object cardResponse= post(cardUrl,driverr);
		JSONObject json = (JSONObject) new JSONParser().parse((String) cardResponse);
		currentCardId= (String)json.get("id");
		System.out.println(7+currentCardId);
		currentCardUrl=(String)json.get("url");
		System.out.println(8+currentCardUrl);
	}

	public void creatCheckList(String cardId, String checkListname,WebDriver driverr) throws InterruptedException, ParseException{
		
			String checklistUrl="https://api.trello.com/1/checklists?idCard="+cardId+"&name="+checkListname+"&key="+APIKey+"&token="+acessToken+""; 
	        Object cardResponse=post(checklistUrl,driverr);
			JSONObject json = (JSONObject) new JSONParser().parse((String) cardResponse);
	        currentCheckListId=(String)json.get("id");
		
	}
	
	
	public void creatCheckListItem(String checklistId, String name,boolean checkornot, WebDriver driverr) throws InterruptedException, ParseException{
		
			String checklistItemUrl="https://api.trello.com/1/checklists/"+checklistId+"/checkItems?name="+name+"&checked="+checkornot+"&key="+APIKey+"&token="+acessToken+""; 
			Object cardResponse= post(checklistItemUrl,driverr);
			System.out.println(checklistItemUrl);
			JSONObject json = (JSONObject) new JSONParser().parse((String) cardResponse);
	        currentCheckListId=(String)json.get("id");
		
	}
		
	
	public Object post(String url, WebDriver dvr) throws InterruptedException, ParseException{
		
		
		JavascriptExecutor js = (JavascriptExecutor)dvr;
		dvr.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		Object response= js.executeAsyncScript("var data = JSON.stringify(false);"+
				 "var callback = arguments[arguments.length - 1];" +
                 "var xhr = new XMLHttpRequest();"+
	             "xhr.withCredentials = false;"+ 
	             "xhr.onreadystatechange = function() {" +
	             "  if (xhr.readyState == 4) {" +
	             "    callback(xhr.responseText);" +
	             "  }" +
	             "};" +
          
	              "xhr.open(\"POST\",\""+url+"\");"
                + "xhr.send(data);");
		
		System.out.println("response :::"+response );
	
		return response;
		
	}
		
	
}
