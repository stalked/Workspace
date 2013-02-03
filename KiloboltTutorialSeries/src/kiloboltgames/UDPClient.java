package kiloboltgames;
//  UDPClient.java
//
//  Bhojan Anand
//
import java.util.*;
import java.net.*;
import java.io.*;

public class UDPClient {
	
	public void connect () throws Exception {
		
		//Use DataGramSocket for UDP connection
		@SuppressWarnings("resource")
		DatagramSocket socket = new DatagramSocket();
		
		// convert string "hello" to array of bytes, suitable for
		// creation of DatagramPacket
		String str = "g";
		
		byte outgoingBuffer[] = str.getBytes();
		// IPv4
		// Now create a packet (with destination addr and port)
		InetAddress addr = InetAddress.getByName("localhost");
		int port = 9001;
		DatagramPacket outgoingPacket = new DatagramPacket(outgoingBuffer, outgoingBuffer.length, addr, port);
		
		socket.send(outgoingPacket);
		
		// create a packet buffer to store data from packets received.
		byte inBuf[] = new byte[1000];
		DatagramPacket incomingPacket = new DatagramPacket(inBuf, inBuf.length);

		// receive the reply from server.
		socket.receive(incomingPacket);

		// convert reply to string and print to System.out
		String reply = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
		System.out.println(reply);
	}
}
