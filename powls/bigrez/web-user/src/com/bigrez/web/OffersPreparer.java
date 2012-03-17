package com.bigrez.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;

import weblogic.logging.LoggingHelper;

import com.bigrez.domain.Offer;
import com.bigrez.service.EntityNotFoundException;
import com.bigrez.service.PropertyServices;

public class OffersPreparer extends ViewPreparerSupport {

	private PropertyServices propertyServices;
	private int maximumOffers;
	final Logger logger = LoggingHelper.getServerLogger();
	
	@Override
	public void 
	execute(TilesRequestContext tilesContext, AttributeContext attributeContext)
	throws PreparerException
	{
		logger.log(Level.INFO,"OffersPreparer::execute()");
		Map<String,Object> lSessionMap = tilesContext.getSessionScope();
		Map<String,Object> lRequestMap = tilesContext.getRequestScope();
		ReservationInfo rezinfo = (ReservationInfo) lSessionMap.get("rezinfo");
		try
		{
			List<Offer> offers =
				propertyServices.getOffersForDisplay(rezinfo.getProperty(), 
						rezinfo.getLastSearchCity(), rezinfo.getLastSearchState(), getMaximumOffers());
			lRequestMap.put("offers", offers);
		}
		catch (EntityNotFoundException e)
		{
			lRequestMap.put("offers", new ArrayList<Offer>());
		}
		logger.log(Level.INFO,"OffersPreparer::execute() complete");
		super.execute(tilesContext, attributeContext);
	}

	public PropertyServices getPropertyServices() {
		return propertyServices;
	}

	public void setPropertyServices(PropertyServices pPropertyServices) {
		propertyServices = pPropertyServices;
	}

	public int getMaximumOffers() {
		return maximumOffers;
	}

	public void setMaximumOffers(int pMaximumOffers) {
		maximumOffers = pMaximumOffers;
	}
	
}
