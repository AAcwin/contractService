package com.example.contractmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ToWeb<T> {
    private String message;
    private T data;

    public static <E> ToWeb<E> success(E data ){
        return new ToWeb<>("success",data);
    }

    public static ToWeb success(){
        return new ToWeb("success",null);
    }

    public static ToWeb error(String message){
        return new ToWeb(message,null);
    }
}
