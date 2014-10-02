package com.forside.android.vending;

public class VendingProvider {
	
	static IVendingService mVendingService;
	
	private static IVendingService GetProperVendingService() {
		//TODO:get proper vending service by installer file name.
		return new VerizonVendingService();
	}
	public static IVendingService GetVendingService() {
		if(mVendingService == null)
			mVendingService = GetProperVendingService();
		return mVendingService;
	}
}
