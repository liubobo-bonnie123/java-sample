package com.forside.android.vending;

import com.verizon.vcast.apps.LicenseAuthenticator;

public class VerizonLicenseCheck implements ILicenseCheck {

	LicenseAuthenticator mAuthenticator;
	
	VendingContext mContext;
	
	@Override
	public void ValidateLicense(VendingContext vendingContext) {
		mAuthenticator = new LicenseAuthenticator(vendingContext.getContext());
		mContext = vendingContext;
		//comment the license checker temporarily for testing other features.
//		 new Thread(new Runnable() {
//		        public void run() {
//		        	//use the package name as keyword.
//		    		int retval = mAuthenticator.checkLicense(mContext.getContext().getPackageName());
//		    		
//		    		  switch (retval) {
//		    		  case LicenseAuthenticator.LICENSE_TRIAL_OK:
//		    			case LicenseAuthenticator.LICENSE_OK:
//		    				//License was validated.
//		    				//Proceed with regular application launch.
//		    				mContext.allow();
//		    				break;
//		    				
//		    			default:
//		    				mContext.dontAllow();
//		    		}
//		        }
//		    }).start();
		mContext.allow();

	}

	@Override
	public void Destory() {
//		if(mAuthenticator != null)
//		{
//			mAuthenticator.
//		}
	}
}