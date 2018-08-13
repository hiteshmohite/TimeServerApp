/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcnproject1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Hitesh Mohite
 */
public class tsapp_client {

    String[] use_arr;
    
    public tsapp_client(String[] args) throws UnknownHostException, IOException 
    {
        use_arr = new String[args.length]; 
        initialize(args);
    }
    
    public void initialize(String[] args) throws UnknownHostException, IOException
    {
        use_arr = args;
        for(int i = 0; i < use_arr.length; i++)
        {
            System.out.println(use_arr[i]);
        }
        start_working();
    }
    
    //str[0]-> ipaddress,str[1]-> port,str[2]-> type of protocol i.e u or t,
    //str[3]-> time format i.e Z or E,str[4]-> # of queries,str[5]-> set time T or 0,
    //str[6]-> username,str[7]-> password
    public void start_working() throws UnknownHostException, IOException
    {
        if(use_arr[2] == "u")
        {
            UDP_Client U = new UDP_Client(use_arr);
        }
        else if(use_arr[2] == "t")
        {
            TCP_Client T = new TCP_Client(use_arr);
        }
    }
}
class UDP_Client
{
    //str[0]-> ipaddress,str[1]-> port,str[2]-> type of protocol i.e u or t,
    //str[3]-> time format i.e Z or E,str[4]-> # of queries,str[5]-> set time TIME_TO_SET or 0,
    //str[6]-> username,str[7]-> password
    InetAddress IP;
    byte[] send_data, get_data;
    int port, counter = 0, query_limit = 0, TIME_TO_SET = 0;
    Character format;
    String read = "", set_time = "", name = "", pwd = "";
    DatagramPacket send_pack, receive_pack;
    
    public UDP_Client(String[] data) throws UnknownHostException, IOException 
    {
        IP = InetAddress.getByName(data[0]);
        port = Integer.parseInt(data[1]);
        format = data[3].charAt(0);
        query_limit = Integer.parseInt(data[4]);
        set_time = data[5];
        TIME_TO_SET = Integer.parseInt(data[5]);
        name = data[6];
        pwd = data[7];
        start_process();
    }
    
    public void start_process() throws SocketException, IOException
    {
        DatagramSocket socket_udp = new DatagramSocket(port);
        send_data = new byte[1024];
        get_data = new byte[1024];
        
        //check if the user wants to know or set the time
        
        if(TIME_TO_SET == 0)
        {
            //queries no of times the client mentions
            while(counter < query_limit)
            {       
                //checks for the time format
                if(format == 'Z')
                {
                    read = "Z";
                    send_data = read.getBytes();
                    send_pack = new DatagramPacket(send_data, send_data.length, IP, port);
                    socket_udp.send(send_pack);
                    receive_pack = new DatagramPacket(get_data, get_data.length);
                    socket_udp.receive(receive_pack);
                    read = new String(receive_pack.getData());
                    System.out.println(read);
                }
                else if(format == 'E')
                {
                    read = "E";
                    send_data = read.getBytes();
                    send_pack = new DatagramPacket(send_data, send_data.length, IP, port);
                    socket_udp.send(send_pack);
                    receive_pack = new DatagramPacket(get_data, get_data.length);
                    socket_udp.receive(receive_pack);
                    read = new String(receive_pack.getData());
                    System.out.println(read);
                }
                counter++;
            }
        }
        else if(TIME_TO_SET != 0)
        {
            read = "SET " + set_time + " " + name + " " + pwd ;
            send_data = read.getBytes();
            send_pack = new DatagramPacket(send_data, send_data.length, IP, port);
            socket_udp.send(send_pack);
        }
    }
}
class TCP_Client
{
    //str[0]-> ipaddress,str[1]-> port,str[2]-> type of protocol i.e u or t,
    //str[3]-> time format i.e Z or E,str[4]-> # of queries,str[5]-> set time TIME_TO_SET or 0,
    //str[6]-> username,str[7]-> password
    InetAddress IP;
    Socket socket_tcp;
    DataOutputStream send_data;
    DataInputStream receive_data;
    BufferedReader get_line; 
    int port, counter = 0, query_limit = 0, TIME_TO_SET = 0;
    Character format;
    String read = "", set_time = "", name = "", pwd = "";
    
    public TCP_Client(String[] data) throws UnknownHostException, IOException 
    {
        IP = InetAddress.getByName(data[0]);
        port = Integer.parseInt(data[1]);
        format = data[3].charAt(0);
        query_limit = Integer.parseInt(data[4]);
        set_time = data[5];
        TIME_TO_SET = Integer.parseInt(data[5]);
        name = data[6];
        pwd = data[7];
        start_process();
    }
    
    public void start_process() throws IOException
    {
        socket_tcp = new Socket(IP, port);
        send_data = new DataOutputStream(socket_tcp.getOutputStream());
        receive_data = new DataInputStream(socket_tcp.getInputStream());
        get_line = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("in start process");
        //check if the user wants to know or set the time
        
        if(TIME_TO_SET == 0)
        {
            //queries no of times the client mentions
            while(counter < query_limit)
            {
                System.out.println("in while loop");
                //checks for the time format
                if(format == 'Z')
                {
                    System.out.println("in Z");
                    send_data.writeUTF("Z");
                    read = receive_data.readUTF();
                    System.out.println(read);
                }
                else if(format == 'E')
                {
                    System.out.println("in E loop");
                    send_data.writeUTF("E");
                    System.out.println("After sending");
                    read = receive_data.readUTF();
                    System.out.println("After receiving");
                    System.out.println(read);
                }
                counter++;
            }
        }
        else if(TIME_TO_SET != 0)
        {
            read = "SET " + set_time + " " + name + " " + pwd ;
            send_data.writeUTF(read);
        }
    }
}