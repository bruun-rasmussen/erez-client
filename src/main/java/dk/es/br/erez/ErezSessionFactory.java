package dk.es.br.erez;

import com.yawah.erez.pub.rpc.Erez3Locator;
import com.yawah.erez.pub.rpc.Erez3PortType;

/**
 *
 * @author osa
 */
public class ErezSessionFactory {

    private ErezSoapContext ctx;

    public ErezSessionFactory(ErezSoapContext ctx) {
        this.ctx = ctx;
    }

    public ErezSession connect() {
        Erez3PortType sc;
        try {
            Erez3Locator e3l = new Erez3Locator();
            //e3l.setMaintainSession(true);
            sc = e3l.geterez3Port(ctx.getSoapUrl());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new ErezSession(sc);
    }

}
