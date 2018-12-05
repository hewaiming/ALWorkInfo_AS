package com.hewaiming.ALWorkInfo.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import android.util.Log;
import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;
import bean.RequestAction;
public abstract class SocketTransceiver implements Runnable {

	private static final String TAG = "SocketTransceiver";
	protected Socket socket;
	protected InetAddress addr;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected ObjectInputStream objectInputStream;
	private boolean runFlag;	

	public SocketTransceiver(Socket socket) {
		this.socket = socket;
		this.addr = socket.getInetAddress();
	}

	public InetAddress getInetAddress() {
		return addr;
	}

	public void start() {
		runFlag = true;
		new Thread(this).start();
	}
	
	public void stop() {
		runFlag = false;
		try {
			socket.shutdownInput();
			in.close();
		} catch (Exception e) {
			Log.e(TAG,"SOCKET closing error");
		}
	}
	public boolean send(RequestAction action) {
		if (out != null) {
			try {
				out.writeInt(action.getActionId());					
				out.writeUTF(action.getPotNo_Area());
				out.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG,"SOCKET send data error "+e.getMessage());
			}
		}
		return false;
	}
	@Override
	public void run() {
		try {
			in = new DataInputStream(this.socket.getInputStream());
			out = new DataOutputStream(this.socket.getOutputStream());
			objectInputStream = new ObjectInputStream(this.socket.getInputStream());			
		} catch (IOException e) {
			Log.e(TAG,"SOCKET RUNing error ");
			runFlag = false;
		}
		while (runFlag) {
			try {
				if (objectInputStream != null) {
					int actionId = objectInputStream.readInt();
					if (actionId == 1) {
						final RealTime rTime = (RealTime) objectInputStream.readObject();
						if (rTime != null) {							
							this.onReceive(addr, rTime);
						}
					} else if (actionId == 2) {
						final PotStatusDATA pList = (PotStatusDATA) objectInputStream.readObject();
						if (pList != null) {							
							this.onReceive(addr, pList);
						}
					}
				}
			} catch (IOException e) {
				Log.e(TAG,"SOCKET receive data error");

			} catch (ClassNotFoundException e) {
				Log.e(TAG,"SOCKET receive data error");
			}
		}
		// 断开连接
		try {
			in.close();
			out.close();
			socket.close();
			in = null;
			out = null;
			socket = null;
		} catch (IOException e) {
			Log.e(TAG,"SOCKET offline error");
		}
		this.onDisconnect(addr);
	}

	// 接受服务端发送过来的实时曲线数据
	public abstract void onReceive(InetAddress addr, RealTime rTime);

	// 接受服务端发送过来的槽状态数
	public abstract void onReceive(InetAddress addr, PotStatusDATA potStatus);

	public abstract void onDisconnect(InetAddress addr);
	
}
