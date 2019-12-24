package com.linkpal.integrated.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * Description:应收单接口
 *
 * @author lichao
 * @date 2019年12月19日 上午10:12:54
 *
 */
@WebService(serviceName = "SaleInvoiceService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public interface SaleInvoiceService {
	@WebMethod
	public @WebResult(name = "saleInvoiceData") String getSaleInvoiceData(@WebParam(name = "saleInvoiceData") String saleInvoiceData);
}
