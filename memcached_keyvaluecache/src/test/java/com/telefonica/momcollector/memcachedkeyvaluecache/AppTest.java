package com.telefonica.momcollector.memcachedkeyvaluecache;

import java.util.HashMap;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	
	private class mockKeyValueCache {	
		private HashMap<String, String> kv=new HashMap<String, String>();	
		public mockKeyValueCache() {}
		
		public void putValue(String k, String v) {
			kv.put(k,v);
		}
		public String getValue(String k) {
			return kv.get(k);
		}
	}
	
    
	mockKeyValueCache keyValue;
    //KeyValueCache keyValue;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
                Properties config=new Properties();
        config.setProperty("memcached.servers","localhost:11211");
        config.setProperty("memcached.local_cache_capacity","100");
        /* keyValue=KeyValueCacheFactory.getKeyValueCache(
                "com.telefonica.momcollector.memcachedkeyvaluecache.MemCachedKeyValueCacheFactory",
                 config);*/
        keyValue = new mockKeyValueCache();

    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
    public void testBasic() {
      
        keyValue.putValue("key1","value1");
        keyValue.putValue("key2","value2");
        keyValue.putValue("key3", "value3");
    }
    
    public void testFullCapacity() {
        
        for(int i=0;i<100;i++)
              keyValue.putValue("key"+i, "value"+i);
        assert(keyValue.getValue("key0")!=null);
        keyValue.putValue("key100","value100");
        assert(keyValue.getValue("key100").equals("value100"));
        assert(keyValue.getValue("key0")!=null);
        
    }
}
   
