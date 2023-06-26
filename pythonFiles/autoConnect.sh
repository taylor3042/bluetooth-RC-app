#!/bin/bash
bt-adapter --set Powered 1
bt-adapter --set DiscoverableTimeout 0
bt-adapter --set Discoverable 1
bt-adapter --set PairableTimeout 0
bt-adapter --set Pairable 1

coproc myproc {
bluetoothctl
}

#bluetoothctl -- scan on
# send a command to it (echo a)
echo "scan on" >&"${myproc[1]}"

read line <&"${myproc[0]}"

while [[ $line != *"Connected"* ]]
	do 
		read line <&"${myproc[0]}"
		echo $line
		if [[ $line == *"Connected"* ]]; then
			address="${line:90:95}"
			echo "trust ${address:0:17}" >&"${myproc[1]}"
			echo "connect ${address:0:17}" >&"${myproc[1]}"
			#echo "trust ${address:0:17}"
		fi
	done
bluetoothctl -- trust ${address:0:17}
bluetoothctl -- connect ${address:0:17}
