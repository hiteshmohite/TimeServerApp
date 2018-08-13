/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcnproject1;

import java.io.IOException;
import static java.lang.System.exit;

/**
 *
 * @author Hitesh Mohite
 */
public class tsapp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        int length_of_args = args.length;
        
        String trigger_to_execute = args[0];
        
        switch(trigger_to_execute)
        {
            case "-s" :
                Form_Server(args);
                break;
            case "-c" :
                Form_Client(args);
                break;
            case "-p" :
                Form_Proxy(args);
                break;
            default :
                System.out.println("Please check the command you inserted\nThank you");
                exit(0);
        }
        
    }
    
    public static void Form_Server(String[] list_of_commands) throws IOException
    {
        //str[0]-> set time,str[1]-> username or XXXX,str[2]-> password or XXXX,str[3]-> u port,
        //str[4]-> t port
        String options = "", user_name = "", password = "";
        String[] format_to_send = new String[5];
        int length_of_command = list_of_commands.length;
        //TCP_Port
        format_to_send[4] = list_of_commands[length_of_command - 1];
        //UDP_Port
        format_to_send[3] = list_of_commands[length_of_command - 2];
        //set_time
        format_to_send[0] = list_of_commands[2];
        
        if(length_of_command == 5)
        {
            format_to_send[1] = "XXXX";
            format_to_send[2] = "XXXX";
        }
        else if(length_of_command == 9)
        {
            format_to_send[1] = list_of_commands[4];
            format_to_send[2] = list_of_commands[6];
        }
        
        Start_Server(format_to_send);
    }
    
    public static void Start_Server(String[] args) throws IOException
    {
        tsapp_server server= new tsapp_server(args);
        //server.start_servers(args);
    }
    
    public static void Form_Client(String[] list_of_commands) throws IOException
    {
        String options = "",t = "";
        //str[0]-> ipaddress,str[1]-> port,str[2]-> type of protocol i.e u or t,
        //str[3]-> time format i.e Z or E,str[4]-> # of queries,str[5]-> set time T or 0,
        //str[6]-> username,str[7]-> password
        String[] format_to_send = new String[8];
        String time_format = "", user_name = "", password = "";
        int set_time = 0, no_of_queries = 0, temp = 0;
        boolean udp = false, tcp = false;
        int length_of_command = list_of_commands.length;
        format_to_send[0] = list_of_commands[1];
        format_to_send[1] = list_of_commands[length_of_command - 1];//udp
        //format_to_send[2] = list_of_commands[length_of_command - 1];//tcp
        
        for(int i = 2; i < (length_of_command); i++)
        {
            options = options + list_of_commands[i];
            System.out.println(options);
        }
        
        tcp = options.contains("-t")? true : false;
        if(tcp)
        {
            udp = false;
            format_to_send[2] = "t";
        }
        else
        {
            udp = true;
            format_to_send[2] = "u"; 
        }
        
        time_format = options.contains("-z") ? "Z" : "E";
        
        format_to_send[3] = time_format;
        
        if(options.contains("-n"))
        {
            t = "";
            temp = options.indexOf("n");
            ++temp;
            while((options.charAt(temp) != '-') || (temp == options.length()))
            {
                t = t + options.charAt(temp);
                temp++;
            }
            no_of_queries = Integer.parseInt(t);
            format_to_send[4] = t;
        }
        else
        {
            format_to_send[4] = "1";
        }
        
        if(options.contains("-T"))
        {
            t = "";
            temp = options.indexOf("T");
            ++temp;
            while((options.charAt(temp) != '-') || (temp == options.length()))
            {
                t = t + options.charAt(temp);
                temp++;
            }
            set_time = Integer.parseInt(t);
            format_to_send[5] = t;
        }
        else
        {
            format_to_send[5] = "0"; 
        }
        
        if(options.contains("--user"))
        {
            t = "";
            temp = options.indexOf("user");
            ++temp;
            while((options.charAt(temp) != '-') || (temp == options.length()))
            {
                t = t + options.charAt(temp);
                temp++;
            }
            user_name = t;
            format_to_send[6] = t;
        }
        else
        {
            format_to_send[6] = "XXXX";
        }
        
        if(options.contains("--pass"))
        {
            t = "";
            temp = options.indexOf("pass");
            temp = temp + 4;
            while((options.charAt(temp) != '-') || (temp == options.length()))
            {
                t = t + options.charAt(temp);
                temp++;
            }
            password = t;
            format_to_send[7] = t;
        }
        else
        {
            format_to_send[7] = "XXXX";
        }
        Start_Client(format_to_send);
    }
    
    public static void Start_Client(String[] format) throws IOException
    {
        tsapp_client client = new tsapp_client(format);
        //client.start_client(format);
    }
    
    public static void Form_Proxy(String[] list_of_commands) throws IOException
    {
        //str[0]-> ipaddress,str[1]-> port_u,str[2]-> port_t,str[3]-> type of protocol i.e u or t,
        //str[4]-> server udp, str[5] -> server tcp
        String proto = "";
        String[] format_to_send = new String[6];
        format_to_send[0] = list_of_commands[1];
        int length_of_command = list_of_commands.length;
        int TCP_Port = Integer.parseInt(list_of_commands[length_of_command - 1]);
        format_to_send[2] = list_of_commands[length_of_command - 1];
        int UDP_Port = Integer.parseInt(list_of_commands[length_of_command - 2]);
        format_to_send[1] = list_of_commands[length_of_command - 2];
        int udp_port_for_server = 0, tcp_port_for_server = 0;
        
        for(int i = 0; i < length_of_command; i++)
        {
            if(list_of_commands[i].contains("--proxy-udp"))
            {
                format_to_send[4] = list_of_commands[i+1];
            }
            if(list_of_commands[i].contains("--proxy-tcp"))
            {
                format_to_send[5] = list_of_commands[i+1];
            }
            if(list_of_commands[i].contains("-t") || list_of_commands[i].contains("-u") )
            {
                format_to_send[3] = list_of_commands[i];
            }
        }
        Start_Proxy(format_to_send);
    }
    
    public static void Start_Proxy(String[] args) throws IOException
    {
        tsapp_proxy proxy = new tsapp_proxy(args);
        //proxy.start_proxy(args);
    }
}
