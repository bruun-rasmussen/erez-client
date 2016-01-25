package dk.es.br.erez;

import com.yawah.erez.pub.rpc.Erez3Locator;
import com.yawah.erez.pub.rpc.Erez3PortType;
import com.yawah.erez.pub.rpc.FileInfo;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author osa
 */
public class Erez3PingTest {

    public Erez3PingTest() {
    }

    private static URL erezPortAddress;

    @BeforeSuite
    public static void initUrl() throws Exception {
        erezPortAddress = new URL("http://images.bruun-rasmussen.dk:8080/erez4/rpc");
    }

    Erez3Locator locator;

    @BeforeTest
    public void setUpMethod() throws Exception {
        locator = new Erez3Locator();
      //  locator.setMaintainSession(true);
    }

    @AfterTest
    public void tearDownMethod() throws Exception {
    }
    /*
     @Test //(threadPoolSize = 3, invocationCount = 100,  timeOut = 10000)
     public void testPing() throws RemoteException {
     FileInfo files[] = erezPort.wsListFiles(userHandle, "/", false);
     Assert.assertNotNull(files);
     //        for (FileInfo f : files)
     //            System.out.println(f.getFileName());
     }
     */

    @Test
    public void testTraversal() throws Exception {
        int n = new EC(locator)._traverse("BRWeb/Arkiv/Online/518");
        Assert.assertEquals(n, 1067);
    }

    private static class EC {

        private Erez3PortType erezPort;
        private String userHandle;

        EC(Erez3Locator locator) throws Exception {
            erezPort = locator.geterez3Port(erezPortAddress);
            userHandle = erezPort.wsLogin("bum", "hej");
            System.out.println("userHandle: " + userHandle);

        }

        private int _traverse(String path) throws Exception {

            FileInfo fileArray[] = erezPort.wsListFiles(userHandle, path, true);
            List<FileInfo> files = Arrays.asList(fileArray);
            Collections.shuffle(files);

            if (path.length() > 1 && !path.endsWith("/"))
                path += "/";
            
            int r = 0;
            for (FileInfo f : files) {
                if (f.isIsDirectory()) {
                    r += _traverse(path + f.getFileName());
                } else {
                    System.out.println(path + f.getFileName());
                    r++;
                }
            }
            return r;
        }
    }
}
