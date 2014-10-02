package com.forside.android.vending;

public class VerizonVendingService implements IVendingService {

	ILicenseCheck mLicenseCheck;
	
	@Override
	public ILicenseCheck GetLicenseChecker() {
		if(mLicenseCheck == null)
		{
			mLicenseCheck = new VerizonLicenseCheck();
		}
		return mLicenseCheck;
	}

	@Override
	public String GetStoreUrl() {
		// TODO Auto-generated method stub
		return "http://mediastore.verizonwireless.com/onlineContentStore/index.html#";
	}

}
