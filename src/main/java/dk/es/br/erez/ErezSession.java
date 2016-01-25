package dk.es.br.erez;

import com.yawah.erez.pub.rpc.Erez3PortType;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author osa
 */
public class ErezSession {

    private final static Logger LOG = LoggerFactory.getLogger(ErezSession.class);

    private final Erez3PortType sc;
    private String m_userHandle;

    ErezSession(Erez3PortType sc) {
        this.sc = sc;
    }

    
    public ErezJob transform(String src, String dst, String template, ErezImageRect rect)
      throws IOException
    {
      try
      {
        float top    = rect.getTopOffsetFraction();
        float left   = rect.getLeftOffsetFraction();
        float bottom = rect.getBottomOffsetFraction();
        float right  = rect.getRightOffsetFraction();
        float theta  = rect.getImageRotation();
        long jobId = sc.wsAddJob(m_userHandle, src, dst, template, top, left, bottom, right, theta);
        LOG.info(this + ": eRez transformation job " + jobId + " started");
        return new ErezJob(sc, src + " -> " + dst, jobId);
      }
      catch (Exception ex)
      {
        throw new IOException(this + ": failed to initialize ERez job\n" + ex.getMessage());
      }
    }
    
    
    public void move(String src, String dst)
            throws IOException {
        try {
            long t1 = System.currentTimeMillis();
            if (!sc.wsFileCopy(m_userHandle, src, dst)) {
                throw new IOException("'" + src + "': failed to copy image to '" + dst + "'");
            }
            long t2 = System.currentTimeMillis();
            LOG.info("'" + src + "': copied image to '" + dst + "' (" + (t2 - t1) + "ms)");
            if (!sc.wsFileDelete(m_userHandle, src)) {
                throw new IOException("'" + src + "': failed to delete image");
            }
            long t3 = System.currentTimeMillis();
            LOG.info("'" + src + "': deleted image ' (" + (t3 - t2) + "ms)");
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException("'" + src + "': failed to move/rename image to '" + dst + "'\n" + ex.getMessage());
        }
    }

}
