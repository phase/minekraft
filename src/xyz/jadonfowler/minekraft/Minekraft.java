package xyz.jadonfowler.minekraft;

import xyz.jadonfowler.minekraft.entity.*;


public class Minekraft {
    
    public static Minekraft instance;
    public static EntityPlayer thePlayer;
    
    public Minekraft getInstace(){
        if(instance != null) return instance;
        return instance = new Minekraft();
    }
    
}
