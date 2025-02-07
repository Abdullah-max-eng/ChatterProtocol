import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class chatter {
    
    private String screenName;
    private String serverIp;
    private int serverPort;
    private Socket tcpSocket;
    private DatagramSocket udpSocket;
    private int udpPort;
    private boolean running = true;
    private List<Member> membersList = new ArrayList<>();





    public chatter(String screenName, String serverIp, int serverPort) {
        this.screenName = screenName;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        
        try {
            tcpSocket = new Socket();
            udpSocket = new DatagramSocket();
            udpPort = udpSocket.getLocalPort();




        } catch (Exception e) {
            System.out.println("Error initializing sockets: " + e.getMessage());
        }
    }








    public void connectWithServer() {
        try {
            // printMessage();
            tcpSocket.connect(new InetSocketAddress(serverIp, serverPort));
            String chatterIP = "localhost";
            String helloMessage = "HELO " + screenName + " " + chatterIP + " " + udpPort + "\n";
            
            // System.out.println(helloMessage);



            OutputStream output = tcpSocket.getOutputStream();
            output.write(helloMessage.getBytes());
            output.flush();


            BufferedReader reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            String response = reader.readLine();
            if (response.startsWith("ACPT")) {
                addMember(response);
            } else if (response.startsWith("RJCT")) {
                System.out.println("Screen name already taken. Exiting...");
                running = false;
            }




        } catch (Exception e) {
            System.out.println("Error connecting with server: " + e.getMessage());
            running = false;
        }
    }




    private void addMember(String response) {
                // System.out.println("Parsing members list: " + response);
            
                // Remove "ACPT" and split members by ":"
                String membersData = response.substring(5).trim(); // Remove "ACPT "
                String[] parts = membersData.split(":");
                for (String part : parts) {
                    String[] details = part.split(" ");
                    
                    if (details.length == 3) {  // Ensure it has name, IP, and port
                        try {
                            String screenName = details[0];
                            String ip = details[1];
                            int port = Integer.parseInt(details[2]);
                            membersList.add(new Member(screenName, ip, port));
                            // System.out.println("Added member: " + screenName + " (" + ip + ":" + port + ")");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid port number in member list: " + part);
                        }
                    } else {
                        System.out.println("Invalid member format: " + part);
                    }
                }
            }
    





    public void userInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You are now connected! Type your message and press Enter to send.");
    
        while (true) {
            System.out.println("You: "); // Display prompt before user input
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("/exit")) {
                //  exitChatRoom();
                 break;
             } 
            
             else {
                sendMessage(userInput);
             }




        }
        scanner.close();
    }
    






    public void sendMessage(String message) {
            String formattedMessage = "MESG " + screenName + ": " + message + "\n";
            // System.out.println("Sendinggggg Message --- "+ formattedMessage);
            // System.out.println("Member list --- "+ membersList);
// COndition here to filter out
            for (Member member : membersList) {
                // System.out.println(member);
                // System.out.println("This is the screen name --------" + screenName);
                if (!member.getScreenName().equals(screenName)) { // do not send it to myself
                    sendUDPMessage(formattedMessage, member.getIp(), member.getPort());
                }                
            }
        }






    private void sendUDPMessage(String message, String ip, int port) {
            // System.out.println("Sending UDP ---------------->> " + message + "to ip = "+ ip + " and port = "+ port );
            try {
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), port);
                udpSocket.send(packet);
            } catch (Exception e) {
                System.out.println("Error sending UDP message: " + e.getMessage());
            }
        }











    
    
    
    
        // public void exitChatRoom() {
        //     try {
        //         OutputStream output = tcpSocket.getOutputStream();
        //         String exitMessage = "EXIT\n";
        //         output.write(exitMessage.getBytes());
        //         output.flush();


        //         System.out.println("Exit request sent to server.");
    
        //         // if (udpListener != null) {
        //         //     udpListener.stopListener();
        //         // }
    
        //         running = false;
        //     } catch (Exception e) {
        //         System.out.println("Error exiting chatroom: " + e.getMessage());
        //     }
        // }









    public void printMessage() {
        System.out.println(screenName + " " + serverIp + " " + serverPort);
    }






    public void run() {
        connectWithServer();
        UDPListenerThread listen = new UDPListenerThread(udpSocket, membersList);
        listen.start();

        if(running == true){
            userInput();
        }



    }







    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java ClientChatter <screen_name> <server_host> <server_port>");
            System.exit(1);
        }
        
        String screenName = args[0];
        String serverIp = args[1];
        int serverPort = Integer.parseInt(args[2]);
        
        chatter chatter = new chatter(screenName, serverIp, serverPort);
        chatter.run();
    }
}
