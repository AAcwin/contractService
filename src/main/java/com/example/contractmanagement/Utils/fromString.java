package com.example.contractmanagement.Utils;

import com.example.contractmanagement.DTO.SignR;

public class fromString {
    public static SignR fromString(String str) {
        str = str.replaceAll("SignR\\(|\\)", ""); // 移除首尾 "SignR(" 和 ")"
        String[] keyValues = str.split(", ");
        SignR signR = new SignR();
        for (String kv : keyValues) {
            String[] parts = kv.split("=");
            if (parts.length != 2) continue;

            String key = parts[0];
            String value = parts[1];
            switch (key) {
                case "code":
                    signR.setCode(value);
                    break;
                case "signtime":
                    signR.setSigntime(value);
                    break;
                case "signlocation":
                    signR.setSignlocation(value);
                    break;
                case "ourrepresentative":
                    signR.setOurrepresentative(value);
                    break;
                case "customerrepresentative":
                    signR.setCustomerrepresentative(value);
                    break;
                case "remarks":
                    signR.setRemarks(value);
                    break;
            }
        }
        return signR;
    }
}
