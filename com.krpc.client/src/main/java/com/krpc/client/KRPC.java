package com.krpc.client;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.sun.xml.internal.ws.api.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.krpc.client.entity.Address;
import com.krpc.client.entity.ServiceParams;

/**
 
				  _  _______  _____   _____ 
				 | |/ |  __ \|  __ \ / ____|
				 | ' /| |__) | |__) | |     
				 |  < |  _  /|  ___/| |     
				 | . \| | \ \| |    | |____ 
				 |_|\_|_|  \_|_|     \_____|
                            
 
         		    @author yangzhenkun
 
 */



public class KRPC {

	private static Map<String,ServiceParams> serviceCache = new HashMap<>();
	
	/**
	 * 初始化客户端配置文件
	 * 
	 * @param clientPath
	 * @throws Exception 
	 */
	public static void init(String clientPath) throws Exception {

		// 读取该服务的配置文件
		InputStream inputStream = KRPC.class.getResourceAsStream("/client.xml");
		String path =  KRPC.class.getResource(clientPath).getPath();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(path));

		Element root = document.getRootElement();

		List<Element> serviceNodes = root.elements("Service");
		
		for(Element serviceNode:serviceNodes){
			ServiceParams serviceParams = new ServiceParams();
			
			serviceParams.setServiceName(serviceNode.attributeValue("name"));
			
			Element loadBalanceNode = serviceNode.element("Loadbalance");
			Element serverNode = loadBalanceNode.element("Server");
			serviceParams.setTimeout(Integer.parseInt(serverNode.attributeValue("timeout")));
			List<Element> addrNodes = serverNode.elements("addr");
			
			for(Element addrNode : addrNodes){
				Address addr = new Address();
				addr.setName(addrNode.attributeValue("name"));
				addr.setHost(addrNode.attributeValue("host"));
				addr.setPort(Integer.parseInt(addrNode.attributeValue("port")));
				
				serviceParams.addAddress(addr);
			}
			
			serviceCache.put(serviceParams.getServiceName(), serviceParams);
		}
		
	}
	
	/**
	 * 获取服务配置
	 * @param serviceName
	 * @return
	 */
	public static ServiceParams getService(String serviceName){
		
		return serviceCache.get(serviceName);
	}
	
	
	

}
