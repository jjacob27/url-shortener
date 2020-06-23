package com.jiju.services.urlshortner.interfaces;

import java.math.BigInteger;

public interface DistributedRangeStore {
    BigInteger[] getRange();
}
