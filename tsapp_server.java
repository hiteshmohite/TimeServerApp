/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcnproject1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hitesh Mohite
 */
public class tsapp_server {

    //str[0]-> set time,str[1]-> username or 0,str[2]-> password or 0,str[3]-> u port,
    //str[4]-> t port
    public tsapp_server(String[] args) throws IOException 
    {
        start_working(args);
    }
    
    public void start_working(String[] args) throws IOException
    {
        TIME time = new TIME(args[0]);
        UDP_Server udp = new UDP_Server();
        TCP_Server tcp = new TCP_Server();
        udp.start_server(args, time);
        tcp.start_server(args, time);
    }
}
class UDP_Server extends Thread
{
    DatagramSocket server;
    TIME time;
    String read_query = "", name = "", pwd = "", store = "" ;
    String[] temp;  //temp[0]-->SET, temp[1]-->time to set, temp[2]-->NAME, temp[3]-->Password 
    byte[] input_data;
    byte[] output_data;
    int port = 0;
    
    //str[0]-> set time,str[1]-> username or 0,str[2]-> password or 0,str[3]-> u port,
    //str[4]-> t port
    public void start_server(String[] args, TIME t) throws SocketException
    {
        time = t;
        port = Integer.parseInt(args[4]);
        server = new DatagramSocket(port);
        name = args[1];
        pwd = args[2];
        start();
    }
    public void run()
    {
        try {
            System.out.println("UDP is UP");
            input_data = new byte[1024];
            output_data = new byte[1024];
            DatagramPacket get_data = new DatagramPacket(input_data, input_data.length);
            server.receive(get_data);
            read_query = new String(get_data.getData());
            
            if(read_query.contains("SET"))
            {
                //temp[0]-->SET, temp[1]-->time to set, temp[2]-->NAME, temp[3]-->Password
                temp = read_query.split(" ");
                if(temp[2].equals(name) && temp[3].equals(pwd))
                {
                    store = time.set_get_time(temp[1]);
                    output_data = store.getBytes();
                    DatagramPacket send_data = new DatagramPacket(output_data, output_data.length, get_data.getAddress(), get_data.getPort());
                    server.send(send_data);
                }
                else
                {
                    output_data = "INVALID CREDENTIALS".getBytes();
                    DatagramPacket send_data = new DatagramPacket(output_data, output_data.length, get_data.getAddress(), get_data.getPort());
                    server.send(send_data);
                }
            }
            else
            {
                store = time.set_get_time(read_query);
                output_data = store.getBytes();
                DatagramPacket send_data = new DatagramPacket(output_data, output_data.length, get_data.getAddress(), get_data.getPort());
                server.send(send_data);
            }
            System.out.println(read_query);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
class TCP_Server extends Thread
{
    ServerSocket server;
    TIME time;
    Socket client;
    String read_query = "", name = "", pwd = "", store = "" ;
    String[] temp;  //temp[0]-->SET, temp[1]-->time to set, temp[2]-->NAME, temp[3]-->Password
    DataOutputStream send_data;
    DataInputStream get_data;
    int port = 0; 
    
    //str[0]-> set time,str[1]-> username or 0,str[2]-> password or 0,str[3]-> u port,
    //str[4]-> t port
    public void start_server(String[] args, TIME t) throws IOException
    {
        time = t;
        port = Integer.parseInt(args[4]);
        
        name = args[1];
        pwd = args[2];
        
        start();
    }
    
    public void run()    
    {
        try {
            //System.out.println("TCP is UP");
            server = new ServerSocket(port);
            client = server.accept();
            send_data = new DataOutputStream(client.getOutputStream());
            get_data = new DataInputStream(client.getInputStream());
            read_query = get_data.readUTF();
            System.out.println("TCP is UP");
            System.out.println(read_query);
            
            if(read_query.contains("SET"))
            {
                //temp[0]-->SET, temp[1]-->time to set, temp[2]-->NAME, temp[3]-->Password
                temp = read_query.split(" ");
                if(temp[2].equals(name) && temp[3].equals(pwd))
                {
                    store = time.set_get_time(temp[1]);
                    send_data.writeUTF(store);
                }
                else
                {
                    send_data.writeUTF("INVALID CREDENTIALS");
                }
            }
            else
            {
                store = time.set_get_time(read_query);
                System.out.println(store);
                send_data.writeUTF(store);
            }
        } catch (IOException ex) {
             ex.printStackTrace();
        }
    }
}
class TIME
{
    String format = "";
    int time = 0;

    public TIME(String t) 
    {
        SET_TIME(t);
    }
    
    
    public synchronized String set_get_time(String store)
    {
        format = store;   
        if(format.contains("Z"))
        {
            return UTC_TIME();
        }
        else if(format.contains("E"))
        {
            return EPOCH_TIME();
        }
        else if(format.contains("SET"))
        {
            //have to exclude set keyword while passing format as parameter
            SET_TIME(format);
            return "TIME SET";
        }
        
        return "TIME NOT FOUND";
    }
    
    public void SET_TIME(String t)
    {
        time = Integer.parseInt(t);
        System.out.println("TIME SET");
    }
    
    public String UTC_TIME()
    {
        time = 0400;
        System.out.println(time + "U");
        return time + "U";
    }
    
    public String EPOCH_TIME()
    {
        time = 0400;
        System.out.println(time + "U");
        return time + "E";
    }
}