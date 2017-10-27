# Wrapper-for-trelloAPI-using-webdriver
   Wrapper class in java for trello api using webdriver,Which helps to create cards, checklists, checklistItems, comments and attachments 
  
### Getting Started
   Add this files `TrelloWrapper.java` and `WrapperForTrello.java`to your project. You need API key and oauth token. You can 
   get your API key at: `https://trello.com/app-key`. And you need trello boardID, Which you can from `trello.com/b/boardID/my-board-name`
   
   
### Add attachments to your card 
   
   `public void addAttachements(String cardId, String name, String base64, WebDriver driverr )`
   
   | Name |Type | Description |
   | --- | --- | --- |
   | cardId | String | The ID of the card |
   | name | String | The name of the attachment. Max length 256, For eg 'example.png' |
   | base64 | String | 3 OutputTypes provided by getScreenshotAs(In selenium) one of the types is base64.|
   | driverr | WebDriver | Show file differences that haven't been staged |
   
   



