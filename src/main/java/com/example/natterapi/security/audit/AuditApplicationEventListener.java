package com.example.natterapi.security.audit;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class AuditApplicationEventListener {


    @EventListener
    public void onAuditEvent(AuditApplicationEvent event) {
        AuditEvent actualAuditEvent = event.getAuditEvent();

        log.info("On audit application event: timestamp: {}, principal: {}, type: {}, data: {}",
                actualAuditEvent.getTimestamp(),
                actualAuditEvent.getPrincipal(),
                actualAuditEvent.getType(),
                actualAuditEvent.getData()
        );

    }
}