import java.net.*;
import java.util.List;

public class UDPListenerThread extends Thread {
    private DatagramSocket socket;
    private List<Member> membersList;

    public UDPListenerThread(DatagramSocket socket, List<Member> membersList) {
        this.socket = socket;
        this.membersList = membersList;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                if (receivedMessage.startsWith("MESG")) {
                    System.out.println(receivedMessage.substring(5)); // Display chat message
                } else if (receivedMessage.startsWith("JOIN")) {
                    handleJoinMessage(receivedMessage);
                } else if (receivedMessage.startsWith("EXIT")) {
                    handleExitMessage(receivedMessage);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in UDP Listener: " + e.getMessage());
        }
    }







        private void handleJoinMessage(String message) {
            // System.out.println("Received JOIN message: " + message);

            String[] details = message.trim().split(" ");
            if (details.length == 4) {
                String newUser = details[1];
                String ip = details[2];
                
                try {
                    int port = Integer.parseInt(details[3].trim());  // Trim to remove extra \n
                    membersList.add(new Member(newUser, ip, port));
                    System.out.println(newUser + " has joined the chatroom.");
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing port number: " + details[3]);
                }
            } else {
                System.out.println("Invalid JOIN message format: " + message);
            }
        }







    private void handleExitMessage(String message) {
        String[] details = message.split("¤");
        if (details.length == 2) {
            String exitingUser = details[1];
            membersList.removeIf(member -> member.getScreenName().equals(exitingUser));
            System.out.println(exitingUser + " has left the chatroom.");
        }
    }






}

