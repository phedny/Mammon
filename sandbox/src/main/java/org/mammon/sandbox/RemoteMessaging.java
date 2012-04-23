package org.mammon.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mammon.messaging.Message;

public class RemoteMessaging {

	private static final Logger LOG = Logger.getLogger(RemoteMessaging.class.getName());

	private final MessagingSystem messaging;

	private final JsonUtil jsonUtil;

	private final String localId;

	private final Thread serverThread;

	private final Map<String, Connection> remotes = Collections.synchronizedMap(new HashMap<String, Connection>());

	public RemoteMessaging(MessagingSystem messaging, JsonUtil jsonUtil, String localId) {
		this.messaging = messaging;
		this.jsonUtil = jsonUtil;
		this.localId = localId;

		serverThread = new Thread() {

			@Override
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(49001);
					serverSocket.setSoTimeout(2000);

					while (true) {
						try {
							new Connection(serverSocket.accept());
						} catch (SocketTimeoutException e) {
							if (Thread.interrupted()) {
								LOG.log(Level.INFO, "Network thread interrupted");
								serverSocket.close();
								return;
							}
						}
					}
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Network socket failure", e);
				}
			}

		};
		serverThread.start();
	}

	public void shutdown() {
		serverThread.interrupt();
	}

	public boolean sendMessage(Message message, String destination, String replyDestination) {
		String[] destinations = destination.split(":");
		if (destinations.length != 2) {
			return false;
		}

		Connection connection = remotes.get(destinations[0]);
		if (connection == null) {
			return false;
		}

		try {
			connection.send(replyDestination, destinations[1], message);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private class Connection {

		private final Socket socket;

		private final BufferedReader in;

		private final OutputStreamWriter out;

		private final Thread inputThread;

		private final IdentityMapper identityMapper = new ConnectionIdentityMapper();

		public Connection(Socket socket) throws IOException {
			this.socket = socket;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");

			inputThread = new Thread() {

				@Override
				public void run() {
					String remoteId = null;
					try {
						// First line is identification
						remoteId = in.readLine();
						remotes.put(remoteId, Connection.this);

						while (true) {
							final String line = in.readLine();
							if (line == null) {
								LOG.info("Connection closed by remote end");
								return;
							}

							int length = Integer.parseInt(line);
							char buffer[] = new char[length];
							int r = 0;
							while (r < length) {
								int s = in.read(buffer);
								if (s < 0) {
									LOG.info("Connection closed by remote end");
									return;
								}
								r += s;
							}

							String json = new String(buffer);
							buffer = null;

							NetworkMessage message = jsonUtil.deserializeObject(json, NetworkMessage.class,
									identityMapper);
							LOG.log(Level.WARNING, message.getSource() + " => " + message.getDestination() + ": "
									+ message.getMessage());
							messaging.sendMessage(message.getMessage(), identityMapper.deserializeIdentity(message
									.getDestination()), remoteId + ":" + message.getSource());
						}
					} catch (IOException e) {
						LOG.log(Level.INFO, "Closing connection due to IOException", e);
						return;
					} finally {
						if (remoteId != null) {
							remotes.remove(remoteId);
						}

						try {
							Connection.this.socket.close();
						} catch (IOException e) {
							LOG.log(Level.SEVERE, "Failed to close connection", e);
						}
					}
				}

			};
			inputThread.start();
		}

		public synchronized void send(String source, String destination, Message message) throws IOException {
			NetworkMessage networkMessage = new NetworkMessage(identityMapper.serializeIdentity(source), destination,
					message);
			final String json = jsonUtil.serializeKnownObject(networkMessage, identityMapper, null);
			StringBuffer output = new StringBuffer();
			output.append(json.length()).append("\n").append(json);
			out.append(output.toString()).flush();
		}

	}

	private class ConnectionIdentityMapper implements IdentityMapper {

		private final Map<String, String> identityMapperOut = new HashMap<String, String>();

		private final Map<String, String> identityMapperIn = new HashMap<String, String>();

		@Override
		public synchronized String deserializeIdentity(String identity) {
			return identityMapperIn.get(identity);
		}

		@Override
		public synchronized String serializeIdentity(String identity) {
			if (identityMapperOut.containsKey(identity)) {
				return identityMapperOut.get(identity);
			}
			String remoteId = UUID.randomUUID().toString();
			identityMapperOut.put(identity, remoteId);
			identityMapperIn.put(remoteId, identity);
			return remoteId;
		}

	}

}
