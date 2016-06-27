package com.dev.appx.sns.data;

import java.util.List;

import lombok.Data;

/**
 * Lombok will add getters, setters, equals, hasCode. 
 * @author nthusitha
 *
 */

public class BroadcastRequestBody {

	private List<String> broadcastEvents;
	
	   public boolean isValid() {
	       return broadcastEvents != null && !broadcastEvents.isEmpty();
	   }

	public List<String> getBroadcastEvents() {
		return broadcastEvents;
	}

	public void setBroadcastEvents(List<String> broadcastEvents) {
		this.broadcastEvents = broadcastEvents;
	}
	   
	   
}
