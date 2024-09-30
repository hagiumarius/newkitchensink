package com.globallogic.newkitchensink.service;

import com.globallogic.newkitchensink.controllers.MemberEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Class used to process member events (create, update)
 */

@Component
public class MemberEventsListener {

    Logger logger = LoggerFactory.getLogger(MemberEventsListener.class);

    @EventListener
    public void handleMemberEvent(MemberEvent me) {
        logger.info("Processing a member event of type: {}", me.getOperation());
    }
}