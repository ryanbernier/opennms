package org.opennms.web.rest;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.opennms.netmgt.dao.NotificationDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsNotification;
import org.opennms.netmgt.model.OnmsNotificationCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.spi.resource.PerRequest;

@Component
@PerRequest
@Scope("prototype")
@Path("notifications")
public class NotificationRestService extends OnmsRestService {
    @Autowired
    private NotificationDao m_notifDao;
    
    @Context 
    UriInfo m_uriInfo;

    @Context
    SecurityContext m_securityContext;
    
    @GET
    @Produces("text/xml")
    @Path("{notifId}")
    @Transactional
    public OnmsNotification getNotification(@PathParam("eventId") String notifId) {
    	OnmsNotification result= m_notifDao.get(new Integer(notifId));
    	return result;
    }
    
    @GET
    @Produces("text/plain")
    @Path("count")
    @Transactional
    public String getCount() {
    	return Integer.toString(m_notifDao.countAll());
    }

    @GET
    @Produces("text/xml")
    @Transactional
    public OnmsNotificationCollection getNotifications() {
    	MultivaluedMap<java.lang.String,java.lang.String> params=m_uriInfo.getQueryParameters();
		OnmsCriteria criteria=new OnmsCriteria(OnmsNotification.class);

    	setLimitOffset(params, criteria);
    	addFiltersToCriteria(params, criteria);

        return new OnmsNotificationCollection(m_notifDao.findMatching(criteria));
    }
    
    @PUT
    @Path("{notifId}")
    @Transactional
    public void updateEvent(@PathParam("notifId") String notifId, @FormParam("ack") Boolean ack) {
    	OnmsNotification notif=m_notifDao.get(new Integer(notifId));
    	if(ack==null) {
    		throw new  IllegalArgumentException("Must supply the 'ack' parameter, set to either 'true' or 'false'");
    	}
       	if(ack) {
       		notif.setRespondTime(new Date());
       		notif.setAnsweredBy(m_securityContext.getUserPrincipal().getName());
    	} else {
    		notif.setRespondTime(null);
    		notif.setAnsweredBy(null);
    	}
       	m_notifDao.save(notif);
    }
}

