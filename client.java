import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class client { //Client Class!

    //basic initialization of socket, input and output.
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;


    //main client constructor that takes in the port and the address from the main method, an exception is added to ward againts any input errors
    public client(String address, int port) throws IOException{
        try{
            /* 
            We establish the main socket along with the input and an output stream. This input Stream connects to the user input
            There is also welcome message for the user.
            */
            socket = new Socket(address, port);
            System.out.println("Welcome to KeyValue Service Client");
            System.out.print("Enter the port number: ");
            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());
        }
        //exception catching
        catch(UnknownHostException u){
            System.out.println(u);
        }
        catch(IOException i){
            System.out.println(i);
        }

        /*
        declaring and initializing the message line that goes in and out. 
        furthermore there is a buffered reader for the inputstream as it negates too fast of messages. This is coming from the server side.
        declaring fromserver to hold any string from the server.
        */
        String outline = "";
        String inline = "";
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String fromServer = "";

        while(!outline.equals("5000")){ //making sure the port is correct
            try{
                outline = input.readLine(); //read user input and check if it is 5000
                if(!outline.equals("5000")){
                    System.out.println("Incorrect Port, Try Again!"); //error message
                }
                else{
                    try{
                        //connecting message with 10 second delay
                        System.out.println("Please Wait till I connect you......");
                        Thread.sleep(7000);
                        System.out.println("Welcome to KeyValue Service");
                    }
                    catch(InterruptedException ex){ //exception catcher
                            ex.printStackTrace();
                        }
                }
            }
            catch(IOException i){ //another exception catcher
                System.out.println(i);
            }
            
        }
        
        
        while(!outline.equals("bye")){ //listens for the end of the conversation between server and client
            
            try{
                outline = input.readLine(); //reads the user input
                out.writeUTF(outline); //convert to UTF and send to server
                
                fromServer = stdIn.readLine(); //reads the server output and then sets it to a variable
                System.out.println(fromServer.substring(2)); //print out the server output
                /* SHOULD BE IN READ ME
                    The reason there is substring(2) is because for somereason it outputs a special character. If the error points back to this, restart the program.
                */
            }
            catch(IOException i){
                System.out.print(i);
            }
        }
        try{ //closing the client
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i){
            System.out.print(i);
        }
    }
    public static void main(String args[]) throws IOException
    {
        client client = new client("127.0.0.1", 5000); //new client made and then sent to server
    }
}
