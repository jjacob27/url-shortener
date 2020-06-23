package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.interfaces.DistributedRangeStore;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class DistributedRangeStoreImpl implements DistributedRangeStore {

    @Override
    public BigInteger[] getRange() {
        // TODO: Implement this properly
      return new BigInteger[]{
              BigInteger.ONE, BigInteger.valueOf(2l)
      };
    }
}
