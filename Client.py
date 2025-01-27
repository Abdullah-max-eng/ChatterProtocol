import sys


class ClientChatter:
    def printmessage(self):
        print("Hello wrold")
    





object = ClientChatter() 
object.printmessage()


if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Not sufficiend parameters have been provide Please provide do this:")
        print("Usage: python client.py <screen_name> <server_host> <server_port>")
        sys.exit(1)