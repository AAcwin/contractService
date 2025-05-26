package com.example.contractmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ToWeb<T> {
    private int code;
    private String message;
    private T data;

    public static <E> ToWeb<E> success(E data ){
        return new ToWeb<>(0,"success",data);
    }

    public static ToWeb success(){
        return new ToWeb(0,"success",null);
    }

    public static ToWeb error(String message){
        return new ToWeb(1,message,null);
    }
}
