import bluetooth 
import  os
import picamera

import glob
import time

server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM )
camera=picamera.PiCamera()
port = 1
server_sock.bind(("",port)) 

server_sock.listen(100) 

client_sock,address = server_sock.accept() 

print "Accepted connection from ",address  


while True: 
    try:
        recvdata = client_sock.recv(1024) 
        image=camera.capture(stream,'a1.jpeg')
                    
        myfile=open(image,'rb')
        
    except bluetooth.btcommon.BluetoothError as err:
        client_sock,address = server_sock.accept() 
        print "Accepted connection from ",address 
        while True:
            try:
                recvdata = client_sock.recv(1024) 
                print "Received \"%s\" through Bluetooth" % recvdata 
                
            except bluetooth.btcommon.BluetoothError as err:
                client_sock,address = server_sock.accept() 
                print "Accepted connection from ",address 



    
    
