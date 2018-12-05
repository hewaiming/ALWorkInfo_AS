package com.hewaiming.ALWorkInfo.socket;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;
import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;
public abstract class TcpClient implements Runnable {

	private int port;
	private String hostIP;
	private boolean connect = false;
	private SocketTransceiver transceiver;

	public void connect(String hostIP, int port) {
		this.hostIP = hostIP;
		this.port = port;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket(hostIP, port);
			transceiver = new SocketTransceiver(socket) {			

				@Override
				public void onDisconnect(InetAddress addr) {
					connect = false;
					TcpClient.this.onDisconnect(this);
				}

				@Override
				public void onReceive(InetAddress addr, RealTime rTime) {
					TcpClient.this.onReceive(this, rTime);  //import
				
					
				}
				@Override
				public void onReceive(InetAddress addr, PotStatusDATA potStatus) {
					TcpClient.this.onReceive(this, potStatus);  //import
				}
			};
			transceiver.start();
			connect = true;
			this.onConnect(transceiver);
		} catch (Exception e) {
			Log.e("TcpClient","连接SOCKET服务端时出错");	
			this.onConnectFailed();
		}
	}
	public void disconnect() {
		if (transceiver != null) {
			transceiver.stop();
			transceiver = null;
		}
	}
	public boolean isConnected() {
		return connect;
	}
	public SocketTransceiver getTransceiver() {
		return isConnected() ? transceiver : null;
	}
	public abstract void onConnect(SocketTransceiver transceiver);
	public abstract void onConnectFailed();
	public abstract void onReceive(SocketTransceiver transceiver, RealTime realTime);
	public abstract void onReceive(SocketTransceiver transceiver, PotStatusDATA potStatus);
	public abstract void onDisconnect(SocketTransceiver transceiver);	
}