package ruf.exam.fram.service;

import java.util.Base64;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Guard {

    @ConfigProperty(name="cfg.secret") String secret;

    public void check(HttpHeaders headers) {
        List<String> hs = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (hs == null || hs.isEmpty())
            throw new ClientErrorException(401);
        String tk = hs.get(0);
        if ( ! tk.startsWith("Basic ") || tk.length() < 7)
            throw new ClientErrorException(401);
        tk = tk.substring(6);
        byte[] tkbytes = Base64.getDecoder().decode(tk);
        String credential = new String(tkbytes);
        if ( ! ("personia:" + secret).equals(credential))
            throw new ClientErrorException(401);
    }
    
}

