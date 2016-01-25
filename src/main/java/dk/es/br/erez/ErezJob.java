package dk.es.br.erez;

import com.yawah.erez.pub.rpc.Erez3PortType;
import com.yawah.erez.pub.rpc.JobInfo;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author osa
 */
public class ErezJob {
  private final static Logger LOG = LoggerFactory.getLogger(ErezJob.class);
    
    private final static int WAITING = 1;
    private final static int PROCESSING = 2;
    private final static int PACKING = 3;
    private final static int DONE = 4;
    private final static int FAILED = 5;
    private final static int CANCELED = 6;
    private final static int IN_TRANSIT = 8;

    /*
    public static final int kEscapeNone = 0;
    public static final int kEscapeHTML = 1;
    public static final int kEscapeXML = 2;
    public static final int kJobStatusWaiting = 1;
    public static final int kJobStatusProcessing = 2;
    public static final int kJobStatusPacking = 3;
    public static final int kJobStatusDone = 4;
    public static final int kJobStatusFailed = 5;
    public static final int kJobStatusCanceled = 6;
    public static final int kJobStatusInTransit = 8;
    */
    private final Erez3PortType sc;
    private final String jobName;
    private final long m_jobId;
    
    private long m_jobInfoValidUntil;
    private int m_jobInfoStatus;
    private int m_jobInfoPercentCompleted;
    private String m_jobInfoMessage;

    ErezJob(Erez3PortType sc, String name, long jobId)
    {
        this.sc = sc;
        this.jobName = name;
        this.m_jobId = jobId;
    }

    private void _refresh()
      throws IOException
    {
      if (m_jobInfoValidUntil != 0 && System.currentTimeMillis() < m_jobInfoValidUntil)
        // Last result is still valid. Let's not bother the server again just yet.
        return;

      try
      {
        JobInfo info = sc.wsGetJobStatus(m_jobId);
        if (m_jobInfoStatus != info.getStatus()) {
          LOG.info("Job " + m_jobId + " status " + m_jobInfoStatus + " -> " + info.getStatus());
          m_jobInfoStatus = info.getStatus();            
        }
        if (m_jobInfoPercentCompleted != info.getPercentCompleted()) {
          LOG.info("Job " + m_jobId + " completion " + m_jobInfoPercentCompleted + "% -> " + info.getPercentCompleted() + "%");
          m_jobInfoPercentCompleted = info.getPercentCompleted();
        }
        m_jobInfoMessage = info.getMessage();
        m_jobInfoValidUntil = System.currentTimeMillis() + 1999L;
      }
      catch (Exception ex)
      {
        throw new IOException(this + " (" + m_jobId + "): failed to retrieve ERez job status\n" + ex.getMessage());
      }
    }

    public int getProgress()
      throws IOException
    {
      _refresh();
      return m_jobInfoPercentCompleted;
    }

    public String getMessage()
      throws IOException
    {
      _refresh();
      return m_jobInfoMessage;
    }

    public boolean isFailed()
      throws IOException
    {
      _refresh();
      return m_jobInfoStatus == FAILED;
    }

    public boolean isCancelled()
      throws IOException
    {
      _refresh();
      return m_jobInfoStatus == CANCELED;
    }

    public boolean isDone()
      throws IOException
    {
      _refresh();
      return m_jobInfoStatus == DONE;
    }
    
}
