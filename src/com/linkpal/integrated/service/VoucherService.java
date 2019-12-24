package com.linkpal.integrated.service;

import java.text.ParseException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月8日 下午5:41:45
 *
 */
@WebService(serviceName = "VoucherService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public interface VoucherService {
	@WebMethod
	public @WebResult(name = "voucherData") String getVoucherData(@WebParam(name = "voucherData") String voucherData)  throws ParseException;
}
