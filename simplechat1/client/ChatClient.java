// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  private String loginID ="";

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   * @param loginID variable for login id.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
    sendToServer("#login"+":"+loginID);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if(message.startsWith("#")){
      String [] input = message.split(" ");
      String command = input[0];

      switch(command){

        case "#quit":
          quit();
          break;

        case "#logoff":
          try{
            closeConnection();
            System.exit(1);
          }catch(IOException e) {
            System.out.println("Logoff was Unsuccessful");
          }
          break;

        case"#setHost":
          if(isConnected()){
            System.out.println("Cannot set host because you are already connected");
          }else{
            setHost(input[1]);
          }
          break;

        case"#setPort":
          if(isConnected()){
            System.out.println("Cannot set port because you are already connected");
          }else{
            setPort(Integer.parseInt(input[1]));
          }
          break;

        case"#login":
          if(isConnected()){
            System.out.println("Already connected. Cannot login again");
          }
          else{
            try{
              openConnection();
            }catch(IOException e){
              System.out.println("Login was Unsuccessful");
            }
          }
          break;

        case"#getHost":
          System.out.println("Current host :"+getHost());
          break;

        case"#getPort":
          System.out.println("Current port :"+getPort());
          break;

        default:
          System.out.println("Invalid command");
          break;
      }

    }
    else{
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.  Terminating client.");
        quit();
      }
    }   
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
