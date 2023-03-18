package edu.westga.CS4225.Server;

import java.net.*;
import java.io.*;

public class Server {
   public static int PORT = 4225;
   public static void main(String[] args) throws IOException {
      try(ServerSocket serverSocket = new ServerSocket(PORT);) {
         Socket clientSocket = null;
         System.out.println("Server started. Listening to the port "+PORT+"...");
         while (true) {
            try {
               clientSocket = serverSocket.accept();
               System.out.println("Connection established with client: " + clientSocket.getInetAddress().getHostName());

               ClientHandler clientHandler = new ClientHandler(clientSocket);
               Thread thread = new Thread(clientHandler);
               thread.start();
               
            } catch (IOException e) {
               System.err.println("Accept failed.");
               System.exit(1);
            }
         }
      } catch (IOException e) {
         System.err.println("Could not listen on port: "+PORT);
         System.exit(1);
      }
   }
}

class ClientHandler implements Runnable {
   private Socket clientSocket;

   public ClientHandler(Socket socket) {
      this.clientSocket = socket;
   }

   public void run() {
      try {
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

         String inputLine;
         while ((inputLine = in.readLine()) != null) {
            System.out.println("Received message from client: " + inputLine);
            out.println("Server response: " + inputLine);
         }

         out.close();
         in.close();
         clientSocket.close();

      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}

