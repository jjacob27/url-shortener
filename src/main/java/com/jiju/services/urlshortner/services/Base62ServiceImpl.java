package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.interfaces.Base62Service;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class Base62ServiceImpl implements Base62Service {
    private BigInteger b62=new BigInteger("62");

    @Override
    public String getBase62String(BigInteger counter){
        char map[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                .toCharArray();

        StringBuilder sb = new StringBuilder();
        while(BigInteger.ZERO.compareTo(counter)<0){
            BigInteger[] result = counter.divideAndRemainder(b62);
            counter = result[0];

            sb.append(map[result[1].shortValueExact()]);
        }
        return sb.toString();
    }
}
