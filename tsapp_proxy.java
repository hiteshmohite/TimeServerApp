/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcnproject1;

/**
 *
 * @author Hitesh Mohite
 */
public class tsapp_proxy {
    
    public String[] arr; 
    public tsapp_proxy(String[] args) 
    {
        arr  = new String[args.length];
        Start_working(arr);
        
    }
    
    public void Start_working(String[] args)
    {
        //str[0]-> ipaddress,str[1]-> port_u,str[2]-> port_t,str[3]-> type of protocol i.e u or t,
        //str[4]-> server udp, str[5] -> server tcp
        connect_the_client();
        connect_to_server();
    }
    
    public void connect_the_client()
    {
        
    }
    public void connect_to_server()
    {
        
    }
}
