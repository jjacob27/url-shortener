package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.interfaces.CounterService;
import com.jiju.services.urlshortner.interfaces.DistributedRangeStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CounterServiceImpl implements CounterService {

    @Autowired
    private DistributedRangeStore store;

    private boolean inited =false;

    private BigInteger counterStart;
    private BigInteger counterCurrent;
    private BigInteger counterMax;

    @Override
    public BigInteger getNextCount() {
        if(!inited)
            init();

        if(counterCurrent.compareTo(counterMax)<=0) {
           BigInteger returnValue = BigInteger.ZERO.add(counterCurrent);
           counterCurrent = counterCurrent.add(BigInteger.ONE);
           return returnValue;
        } else {
            // get a new range
            init();
            BigInteger returnValue = BigInteger.ZERO.add(counterCurrent);
            counterCurrent = counterCurrent.add(BigInteger.ONE);
            return returnValue;
        }
    }

    private void init() {
        BigInteger[] res = store.getRange();
        counterStart = res[0];
        counterMax = res[1];
        counterCurrent = counterStart;
        inited = true;
    }

}
