package com.tobe.fishking.v2.addon;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

/*
	Class Name : KSPaySocketBean

	���� �ۼ����� : 2003/11/24
	���� �ۼ���   : ���Ʊ�
	���� �������� :
	���� ������   :
*/

public class KSPaySocketBean {

	private	Socket				socket;				//IPG_Server(C-Daemon)�� ���� ����
	private DataInputStream		in;
  	private DataOutputStream	out;
  	public  String				IPAddr;
  	public  int					Port;

  	public  String				SendURL;


	/*
	**	��� ���� Private �޽���
	*/

	public KSPaySocketBean(String IPAddr, int Port)
	{
		this.IPAddr = IPAddr;
		this.Port   = Port;
	}

	public KSPaySocketBean()
	{
		this.IPAddr = null;
		this.Port   = 0;
	}

	public boolean setSendURL(String SendURL, String ReceiveMsg) throws IOException
	{
		if(SendURL == null || SendURL.equals(""))
			return true;

        SendURL = SendURL+"?data="+URLEncoder.encode(ReceiveMsg, "KSC5601");

		try
		{
			URL home = new URL(SendURL);

			BufferedReader br;
			String line;
			br = new BufferedReader(new InputStreamReader(home.openStream()));
			while((line = br.readLine()) != null) System.out.println(SendURL+":"+line);
		}
        catch(Exception e) {
			System.out.println("setSendURL ERROR="+e.toString());
			return false;
		}
		return true;
	}

	// IPG_Server�� ������ �δ´�.
	public void ConnectSocket() throws IOException
	{
		try
		{
			socket = new Socket(this.IPAddr, this.Port);
			in     = new DataInputStream(socket.getInputStream());
			out    = new DataOutputStream(socket.getOutputStream());
			System.out.println("addr = [" + this.IPAddr + "]  ,  port = [" + this.Port + "]");
		}
		catch( IOException e )
		{
			throw new IOException("[KSPaySocketBean] cannot connect server : (" + this.IPAddr + " , " + this.Port + ")");
		}
	}

	// IPG_Server�� ����Ÿ�� �����Ѵ�..
	public void write(byte[] msg) throws IOException
	{
		try
		{
			out.write(msg);
			out.flush();
		}
		catch( IOException e )
		{
			throw new IOException("[KSPaySocketBean] cannot write to socket");
		}
	}

	// IPG_Server�� ���� ����Ÿ�� ��´�.
	public byte[] read(int size) throws IOException
	{
		try
		{
			byte[] msg = new byte[size];
			in.read(msg);
			return msg;
		}
		catch( IOException e )
		{
			throw new IOException("[KSPaySocketBean] cannot read from socket");
		}
	}

	// ���� ������ �ݴ´�.
	public void CloseSocket() throws IOException
	{
		try
		{
			socket.close();
		}
		catch( IOException e )
		{
			throw new IOException("[KSPaySocketBean] cannot close socket");
		}
	}

	public String format(String str, int len, char ctype)
	{
		String formattedstr = new String();
		byte[] buff;
		int filllen = 0;

		buff = str.getBytes();

		filllen = len - buff.length;
		formattedstr = "";
		if(ctype == '9'){// ���ڿ��� ���
			for(int i = 0; i<filllen;i++)
			{
				formattedstr += "0";
			}
			formattedstr = formattedstr + str;
		}
		else
		{ // ���ڿ��� ���
			for(int i = 0; i<filllen;i++)
			{
				formattedstr += " ";
			}
			formattedstr = str + formattedstr;
		}
		return formattedstr;
	}

	public String setTrim(String str, int len)
	{
		byte[] subbytes;
		String tmpStr;
		subbytes = new byte[len];

		System.arraycopy(str.getBytes(), 0, subbytes, 0, len);
		tmpStr = new String(subbytes);
		if(tmpStr.length() == 0) {
			subbytes = new byte[len-1];
			System.arraycopy(str.getBytes(), 0, subbytes, 0, len-1);
			tmpStr = new String(subbytes);
		}
		return tmpStr;
	}

	public String setLogMsg(String str)
	{
		String strBuf = "";
		for(int i=0; i < str.length(); i++)
		{
			if(str.substring(i, i+1).equals(" "))
				strBuf = strBuf + "_";
			else
				strBuf = strBuf + str.substring(i, i+1);
		}
		return strBuf;
	}
}