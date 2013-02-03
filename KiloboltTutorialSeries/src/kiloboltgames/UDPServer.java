//
//  UDPServer.java
//
//  Bhojan Anand
//
package kiloboltgames;

import java.util.*;
import java.net.*;
import java.io.*;

public class UDPServer {
	
	public static void main (String args[]) throws Exception 
	{
		
		//use DatagramSocket for UDP connection
		DatagramSocket socket = new DatagramSocket(9001);
		byte[] incomingBuffer = new byte[1000];
		
		while (true)
		{
			DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, incomingBuffer.length);
			socket.receive(incomingPacket);
			
			// convert content of packet into a string 
			String request = new String(incomingPacket.getData(), 0, 
				incomingPacket.getLength());

			// convert string to uppercase
			String reply = request.toUpperCase();
			
			// convert upper-case string into array of bytes (output
			// buffer)
			byte[] outgoingBuffer = new byte[1000];
			outgoingBuffer = reply.getBytes();

			// create reply packet using output buffer.
			// Note: dest address/port is retrieved from inPkt
			DatagramPacket outgoingPacket = new DatagramPacket(
				outgoingBuffer, outgoingBuffer.length, incomingPacket.getAddress(),
				incomingPacket.getPort());

			// finally, send the packet
			socket.send(outgoingPacket);
		}
	}
}
