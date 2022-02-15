//imports for server connection and basic method loadings
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

//the server class
public class server {
    //initializing socket, server, input and output.
    private Socket socket = null;
    private DataInputStream in = null;
    private ServerSocket server = null;
    private DataOutputStream out = null;

    public server(int port){
        try{
            server = new ServerSocket(port); //declaring a new server

            //welcome messages for server
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            socket = server.accept(); //accepting client request

            System.out.println("Client Accepted"); //accept message

            in = new DataInputStream((socket.getInputStream())); //input stream coming from client
            
            out = new DataOutputStream(socket.getOutputStream()); //output stream going to client

            String inline = "--"; //incoming message line
            String outline = "--"; //outgoing message line
            String msgSent; //the actual message sent to client
            String str; //hold string

            Hashtable<String, Integer> keyValues = new Hashtable<>(100); //true HASHTABLE for the server

            Enumeration keys; //keys for hashtable such that they can be accessed and declared using the get method
            Enumeration values; //values for hashtable such that they can be accessed and declared using the get method
 
            // reads message from client until "bye" is sent
            while (!inline.equals("bye"))
            {
                //initial try
                try 
                {
                    inline = in.readUTF(); //incoming client message
                    msgSent = ""; //hold message

                    //this chunk of code is determining what the client message wanted 

                    //help command
                    if(inline.toLowerCase().equals("help")){
                        msgSent = "help| get (key)| put (key, value)| values| keyset| mappings| bye";
                    }
                    //mappings command
                    else if(inline.toLowerCase().equals("mappings")){
                        msgSent = "Mapping of KeyValues: " + keyValues + "";
                    }
                    //keyset command
                    else if(inline.toLowerCase().equals("keyset")){
                        //accessing hashtable and looping to make a string of keys
                        keys = keyValues.keys();
                        while(keys.hasMoreElements()){
                           str = (String) keys.nextElement();
                           msgSent = msgSent + str + ", ";
                        }
                    }
                    //values command
                    else if(inline.toLowerCase().equals("values")){
                        //accessing the values of each key and then looping to create a string
                        values = (Enumeration) keyValues.elements();
                        while(values.hasMoreElements()){
                            str = values.nextElement().toString();
                            msgSent = msgSent + str + ", ";
                        }
                    }
                    else{
                        /*we break down the rest of the command with the String Tokenizer and get each String token
                        ArrayList is used to store each string token 
                        Each object in the arraylist is then put or declared to either a command, key or value.

                        */
                        String userInput = inline;
                        ArrayList<Object> hold = new ArrayList<Object>();
                        String command;
                        String key;
                        String value; 

                        StringTokenizer token = new StringTokenizer(userInput, " "); //String Tokenizer

                        //splicing the String and putting it in the Arraylist
                        while(token.hasMoreTokens()){
                            hold.add(token.nextElement());
                        }
                        
                        command = hold.get(0).toString();
                        //checking if it is a put or get method
                        if(command.equals("put") | command.equals("get")){
                            //for put command
                            if(hold.size() == 3){
                                key = hold.get(1).toString(); 
                                value = hold.get(2).toString();
                                }
                            else{
                                //for get command
                                key = hold.get(1).toString(); 
                                value = null;
                                }
                                //get command
                            if(command.toLowerCase().equals("get")){
                                msgSent = keyValues.get(key).toString() + ".";
                                }
                            //put command
                            else if(command.toLowerCase().equals("put")){
                                keyValues.put(key, Integer.parseInt(value));
                                msgSent = "Ok";
                                }
                        }
                        //If its not one of the designated command
                        else{
                            msgSent = "Incorrect Command";
                        }
                    }
                    /*
                    This is output back to the client along with a server check to make sure the output is proper.
                    */
                    System.out.println(msgSent);
                    out.writeUTF(msgSent + " \n");
                }
                //any random input exceptions
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection"); //end connection
 
            // close connection
            socket.close();
            in.close();
            out.close();
        }
        catch(IOException i) //any io issues while closing
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        server server = new server(5000); //just a random server on a random port
    }

}
