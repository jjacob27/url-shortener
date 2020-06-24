package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.interfaces.DistributedRangeStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

@Service
public class DistributedRangeStoreImpl implements DistributedRangeStore {

    private static final String INIT_MODE = "initMode";
    private static final String ALLOCATED = "Allocated";
    private static final String RANGE_START = "RangeStart";
    private static final String RANGE_END = "RangeEnd";
    private BigInteger eachRange;
    private BigInteger maxRange;
    private BigInteger numRanges;

    enum RangeInitMode {
        STARTED,
        COMPLETED
    }

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private BigInteger rangeStart;
    private BigInteger rangeEnd;

    public DistributedRangeStoreImpl() throws InterruptedException {
        init();
    }

    private synchronized void init() throws InterruptedException {
        this.eachRange = BigInteger.TWO.pow(20);
        this.maxRange = BigInteger.TWO.pow(24);
        this.numRanges =maxRange.divide(eachRange);
    }

    private void sleepWhileRangesAreReady(RangeInitMode initMode) throws InterruptedException {
        while(!RangeInitMode.COMPLETED.equals(initMode))
        {
            Thread.sleep(1000);
            initMode = (RangeInitMode)redisTemplate.opsForValue().get(INIT_MODE);
        }
    }

    private synchronized void performRangeInitialization() {
        redisTemplate.opsForValue().set(INIT_MODE,RangeInitMode.STARTED);

        for (BigInteger currentRange = BigInteger.ONE ; currentRange.compareTo(numRanges)<0; ){
            BigInteger rangeStart = currentRange.subtract(BigInteger.ONE).multiply(eachRange);
            BigInteger rangeEnd = currentRange.multiply(eachRange).subtract(BigInteger.ONE);

            String key = "R"+currentRange.toString();

            redisTemplate.opsForHash().put(key, RANGE_START,rangeStart);
            redisTemplate.opsForHash().put(key, RANGE_END,rangeEnd);
            redisTemplate.opsForHash().put(key, ALLOCATED,"false");
            currentRange = currentRange.add(BigInteger.ONE);
        }

        redisTemplate.opsForValue().set(INIT_MODE,RangeInitMode.COMPLETED);
    }

    @PostConstruct
    void initRanges(){
        RangeInitMode initMode = (RangeInitMode)redisTemplate.opsForValue().get(INIT_MODE);
        if(!RangeInitMode.STARTED.equals(initMode) && !RangeInitMode.COMPLETED.equals(initMode))
        {
            performRangeInitialization();
        }
    }

    @Override
    public BigInteger[] getNewRange() {
        rangeStart = null;
        rangeEnd = null;
        return getRange();
    }

    @Override
    public BigInteger[] getRange() {
        RangeInitMode initMode = (RangeInitMode)redisTemplate.opsForValue().get(INIT_MODE);
        try {
            sleepWhileRangesAreReady(initMode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(rangeStart==null && rangeEnd == null)
        {
            allocateNewRange();
        }
      return new BigInteger[]{
              rangeStart, rangeEnd
      };
    }

    private void allocateNewRange() {
        for (BigInteger currentRange = BigInteger.ONE; currentRange.compareTo(numRanges)<0; ){
            String key = "R"+currentRange.toString();
            Object allocObj=redisTemplate.opsForHash().get(key, ALLOCATED);
            if(allocObj instanceof String && "false".equals(allocObj.toString()))
            {
                redisTemplate.opsForHash().put(key, ALLOCATED,"true");
                rangeStart = new BigInteger(""+redisTemplate.opsForHash().get(key, RANGE_START));
                rangeEnd = new BigInteger(""+redisTemplate.opsForHash().get(key, RANGE_END));
                return;
            }
            currentRange = currentRange.add(BigInteger.ONE);
        }
    }
}
