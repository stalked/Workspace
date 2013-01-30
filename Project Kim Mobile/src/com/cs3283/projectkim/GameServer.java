/* CS3283 Project
 * 
 * Game Server: feedback to client's requests through UDP connections
 * 
 */

package com.cs3283.projectkim;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class GameServer {
	
	private static EventHandler eventHandler;

	public static void main(String[] args) throws Exception {

		System.out.println("Game Server starting up... [GamerServer.java]");
		
		/* *** Initialization *** */
		String reply = ""; // stores the reply info
		eventHandler = new EventHandler();

		//use DatagramSocket for UDP connection
		@SuppressWarnings("resource")
		DatagramSocket socket = new DatagramSocket(9001);
		byte[] incomingBuffer = new byte[1000];

		// Constantly receiving incoming packets
		while (true)
		{
			DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, incomingBuffer.length); 

			socket.receive(incomingPacket);

			// convert content of packet into a string 
			String request = new String(incomingPacket.getData(), 0, incomingPacket.getLength() );

			/* ----------------------------------------------------- */
			// pass client request to event handler to compute results
			
			reply = eventHandler.computeEventsReply(request);
			
			/* ----------------------------------------------------- */
			
			// convert reply into array of bytes (output buffer)
			byte[] outputBuffer = new byte[1000];
			outputBuffer = reply.getBytes();

			// create reply packet using output buffer.
			// Note: destination address/port is retrieved from incomingPacket
			DatagramPacket outPacket = new DatagramPacket(outputBuffer, outputBuffer.length, incomingPacket.getAddress(), incomingPacket.getPort());

			// finally, send the packet
			socket.send(outPacket);
			System.out.println("Sent reply: " + reply + " [GamerServer.java]");
		}

	}

}
