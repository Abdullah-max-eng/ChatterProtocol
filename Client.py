import sys
import socket
import threading




class ClientChatter:

    def __init__(self, screenName, server_IP, server_port):
        self.screenName = screenName
        self.serverIp = server_IP
        self.serverPort = server_port
        self.activeChatters = {} # we will store screen name, port and, ip
       
       
       
        # socket initialization here
        self.tcpSocket =  socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.udpSocket =  socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.udpSocket.bind(('',0)) # bind to any ava port
        self.udp_port = self.udpSocket.getsockname()[1] # this is the port number
        self.running = True






    def sendMessage(self):
        pass





    def exit_chatRoom(self):
        pass




    def connectWithServer(self):
        try:
            self.printmessage()
            self.tcpSocket.connect((self.serverIp, self.serverPort))
            
            # print("Connected")
            chatterIP = 'localhost'
            helloMessage = f"HELO {self.screenName} {chatterIP} {self.udp_port}\n"
            print(helloMessage)
            self.tcpSocket.sendall(helloMessage.encode())

            response = self.tcpSocket.recv(1024).decode() # this is a blocking call
            print("Response: --->",response)


            
            if response.startswith("ACPT"):
                print(response,"Greeting acknowledged")
                print(response)


        except Exception as e:
            print("Error connecting with server", e)
            self.running = 0

    










    def read_Messages_From_Other_client(self):
        pass
        # while True:
        #     print("Reading client messages")
            




    def useInput(self):
        while self.running:
            userInput = input()
            if userInput.lower() == "/exit":
                self.exit_chatRoom()
                break
            else:
                self.sendMessage(userInput)
        pass



    def run(self):
        # self.printmessage()
        self.connectWithServer()
       
        # if not self.running:
        #     return


        # # Create a thread to listen for messages from other chatters using UDP
        # # 
        # #  
        # threading.Thread(target=self.read_Messages_From_Other_client,daemon=True).start()
        # self.useInput()








    def printmessage(self):
        print(self.screenName,self.serverIp,self.serverPort,)

    







if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Not sufficient parameters have been provided Please provide do this:")
        print("Usage: python client.py <screen_name> <server_host> <server_port>")
        sys.exit(1)
   
    else: 
        # print(sys.argv)
        screenName = sys.argv[1]
        serverIp = sys.argv[2]
        serverPort = int(sys.argv[3])
        chatter = ClientChatter(screenName=screenName, server_IP=serverIp, server_port=serverPort) 
        # chatter.printmessage()
        chatter.run()
 