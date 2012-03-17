package com.bigrez.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;

public class PropertyRatesForm {
	
	private List<RoomType> roomTypes;
	private Map<String,List<Rate>> ratesByRoomType;

	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List<RoomType> pRoomTypes) {
		roomTypes = pRoomTypes;
	}

	public Map<String, List<Rate>> getRatesByRoomType() {
		return ratesByRoomType;
	}

	public void setRatesByRoomType(Map<String, List<Rate>> pRatesByRoomType) {
		ratesByRoomType = pRatesByRoomType;
	}
	
	public void addRateListToMap(RoomType roomType, List<Rate> rates)
	{
		if (ratesByRoomType == null)
		{
			ratesByRoomType = new HashMap<String,List<Rate>>();
		}
		ratesByRoomType.put(roomType.getExternalIdentity(), rates);
	}
}
