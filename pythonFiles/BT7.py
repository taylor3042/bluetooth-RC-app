import bluetooth 
import  os
import time

server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM )

port = 1
server_sock.bind(("",port)) 

server_sock.listen(100) 

client_sock,address = server_sock.accept() 

print "Accepted connection from ",address  

while True:
        client_sock.send("number 1")
        time.sleep(5)
        client_sock.send("number 2")
        time.sleep(5)
        client_sock.send("number 3")
        time.sleep(5)
        client_sock.send("number 4")
        time.sleep(5)
        
        
