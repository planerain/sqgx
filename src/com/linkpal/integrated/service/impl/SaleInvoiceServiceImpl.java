package com.linkpal.integrated.service.impl;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.linkpal.integrated.service.SaleInvoiceService;

/**
 * Description:应收单接口实现类
 *
 * @author lichao
 * @date 2019年12月24日 上午9:40:12
 *
 */
@WebService(serviceName = "SaleInvoiceService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class SaleInvoiceServiceImpl implements SaleInvoiceService {

	@Override
	public String getSaleInvoiceData(String saleInvoiceData) {
		return null;
	}

}
