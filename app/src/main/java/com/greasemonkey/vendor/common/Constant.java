package com.greasemonkey.vendor.common;

/**
 * Created by dell on 12/13/2017.
 */
public class Constant {
    public static final int GET=0;
    public static final int POST=1;

    //public static final String strHost="http://157.245.99.66:9000/";
    public static final String strHost="http://157.245.99.66:9001/";

    public static final String loginAPI=strHost+"api/vendorLogin";

    public static final String mobileRegistrationAPI = strHost+"api/mobileVerification";

    public static final String otpValidationAPI=strHost+"api/otpVerification";

    public static final String vendorPersonalDetail=strHost+"api/vendorPersonalDetail";

    public static final String garageAddressDetail=strHost+"api/garageAddressDetail";

    public static final String stateAPI=strHost+"masters/state";

    public static final String cityAPI=strHost+"masters/city";

    public static final String servicesListAPI=strHost+"masters/serviceType";

    public static final String manufacturerAPI=strHost+"masters/manufacture";

    public static final String registerServicesAPI=strHost+"api/providedServices";

    public static final String registerManufacturereAPI=strHost+"api/providedManufactures";

    public static final String bankDetailAPI=strHost+"api/addBankDetail";

    public static final String getServiceRequest=strHost+"api/userOrdersByVendor";

    public static final String getOrderDetail = strHost+"api/userBookingDetailsByOrder";

    public static final String sendOrderStatus = strHost+"users/updateOrderStatus";

    public static final String getOrderByStatus = strHost+"api/statusWiseOrder";

    public static final String getVendorDetail = strHost+"api/vendorDetails";

    public static final String getBankDetail = strHost+"api/bankDetailByVendor";

    public static final String getLabourCharges = strHost+"masters/labourCharges";

    public static final String sendBikeServicingBill = strHost+"users/updateUserOrderCharges";

    public static final String setShopOnlineStatus = strHost+"api/vendorShopStatus";

    public static final String setPickUpDropStatus = strHost+"api/vendorPickupDropStatus";

    public static final String sndPartChangeRequest = strHost+"api/bikePartChangeDetail";

    public static final String getBikePartChangeCharges = strHost+"api/bikePartChangeDetailByOrder";

    public static final String forgotPassword = strHost+"api/forgotPassword";

    public static final String setShopTommarowsStatus = strHost+"api/setShopTommarowsStatus";

    public static final String getOrderHistory = strHost+"users/showOrderHistory";

    public static final String getInvoiceByOrderId = strHost+"users/getInvoiceByOrderId";
}
