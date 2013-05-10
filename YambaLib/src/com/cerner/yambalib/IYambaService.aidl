package com.cerner.yambalib;

import com.cerner.yambalib.YambaStatus;

interface IYambaService {
	boolean postStatus( in String message, double latitude, double longitude);
	List<YambaStatus> getTimeline(int max);
}