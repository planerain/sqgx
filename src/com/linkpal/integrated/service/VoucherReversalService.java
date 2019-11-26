package com.linkpal.integrated.service;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月26日 下午1:54:24
 *
 */
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public interface VoucherReversalService {
	@WebMethod
	public @WebResult(name = "voucherReversalData") String getVoucherReversalData(String voucherReversalData);
}
