package com.linkpal.integrated.service;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * Description:应付单接口
 *
 * @author lichao
 * @date 2019年12月19日 上午10:12:54
 *
 */
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public interface PurchaseInvoiceService {
	@WebMethod
	public @WebResult(name = "purchaseInvoiceData") String getPurchaseInvoiceData(String purchaseInvoiceData);
}
