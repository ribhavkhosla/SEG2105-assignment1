// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    String message = msg.toString();

    if(message.startsWith("#")){
      String[] par = message.split(" ");
      if(par[0].equals("#login") && client.getInfo("loginID") == null){
        try{
          System.out.println("login ID is invalid. Please send a valid login ID.");
          client.close();
        }catch(IOException e){}
      }else if(par[0].equals("#login") && client.getInfo("loginID") != null){
        client.setInfo("loginID",par[1]); 
      }
    }
      
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(client.getInfo("loginID")+" -> "+ message);
      
    
  }

  /**
   * This method handles any messages received from the server.
   *
   * @param message The message received from the server.
   */
  public void handleMessageFromServerConsole(String message){

    if(message.startsWith("#")){
      String [] input = message.split(" ");
      String command = input[0];

      switch(command){

        case "#quit":
          try{
            close();
          }catch(IOException e){
            System.exit(1);
          }
          System.exit(0);
          break;

        case "#stop":
          stopListening();
          break;

        case"#close":
          try{
            close();
          }catch(IOException E){
            System.out.println("Closing was Unsuccessful");
          }
          break;

        case"#setPort":
          if(getNumberOfClients()==0 && !isListening()){
            setPort(Integer.parseInt(input[1])); 
          }else{
            System.out.println("Cannot set port");
          }
          break;


        case"#start":
          if(isListening()){
            System.out.println("The server is already started");
          }else{
            try{
              listen();
            }catch(IOException e){}
          }
          break;

        case"#getPort":
          System.out.println("Current port :"+getPort());
          break;

        default:
          System.out.println("Invalid command");
          break;
      }

    }





  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  /**
   * This method overrides the method in the superclass.
   * Called when the client is conected to the server.
   *
   * @param client the connection connected to client.
   */
  
  protected void clientConnected(ConnectionToClient client) {
    System.out.println(client + " is Connected");
  }


  /**
   * This method overrides the method in the superclass.
   * Called when the client gets disconnected from the server.
   *
   * @param client the connection connected to client.
   */
  
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println(client + " is Disconnected");
  }


  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

}
//End of EchoServer class
