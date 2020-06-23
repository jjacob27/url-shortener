package com.jiju.services.urlshortner.interfaces;

import java.math.BigInteger;

public interface Base62Service {
    String getBase62String(BigInteger counter);
}
