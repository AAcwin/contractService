package com.example.contractmanagement.Utils;

import java.util.Map;
import java.util.Objects;

public class ThreadLocalUtil {
    private static final ThreadLocal TL = new ThreadLocal();
    public static void setTL(String user){
        TL.set(user);
    }
    public static String getTL(){
        return (String) TL.get();
    }
    public static void remove(){
        TL.remove();
    }
}
