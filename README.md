# url-shortner

## Aim
Get a URL shortner of 8 characters. 

## Strategy 
Each character can be either of:
* a-z (26 possibilities)
* A-Z (26 possibilities)
* 0-9 (10 possibilities)
* Total (62 possibilities)
8 characters means we have "62^8", i.e. greater than 200 trillion possibilities. 
This means if our service gets a request rate of 1000 requests/second for generating new short-url's, we can service this for 6923 years.

## Storage model
We can choose either a relational database or a No-SQL database for storing. It does not matter.
Each entry can be of the form
```
    longURL : 2048 characters
    shortURL : 8 characters    
```
So about 2.1 KB in size for each entry.  

## Strategy Continued - Generating the shortURL's
* We will use a counter to generate numbers
* We will then generate Base 62 of each of these number
* We will use this as the short URL code
* We will persist this to the database.

## Strategy Continued - Eliminating conflicts.
* Generating numbers using counters with multiple App servers can create conflicts, i.e. these AppServers may use the same value generated from the counter.
* To avoid this, we will split the entire number space from 1 to 200 trillion into different ranges.
* And we will maintain all these ranges in a distributed store.
* Each AppServer upon startup will be provided with a initial number range from distributed-store
* On exhausing this number range, the app-server will again talk to distributed-store to get a new range.
* Ranges provided to each AppServer will be exclusive to it.
* If an AppServer exits before exhausting the pool, we will let go of the rest of the range, we will not worry about reclaiming it.

## Strategy Continued - Abandoned choices.
After consideration, we have abandoned generating MD5, or random number based URL generation, to rule out conflicts.

## Strategy Continued for Getting Ranges
Next, we will need to calculate the number of bits needed to represent the whole range of possibilities.
The equation to solve is 
"2^n" = "62^8"
because, we represent in binary, i.e. in powers of 2.
```java
log 2^n = log 62^8
n log 2 = log 62^8
n = (log 62^8)/log 2
i.e n = 47.6
Rounding off we need n=48
```
So we need 48 bits to represent all the different combinations. 
So total ranges will be from 2^0 to 2^48

We will divide the range in distributed-store in multiples of 2^20 (about >1 million)
So if A1, A2, A3, ... are app servers, then:
* A1 will get 0 - (2^20 -1)
* A2 will get 2^20 - 2(2^20)-1
* A3 will get 2(2^20) - 3(2^20) -1
* ... etc.

## Read vs Write considerations
The write operation (i.e. generating a short-url) could be slightly slower, but the read operations (i.e. get the long-URL for a short-URL) needs to be extremely fast.
So once we generate the short-url and long-url combination, in addition to persisting this in a database, we will write this immediately to a cache.

## References
https://www.baeldung.com/java-zookeeper
