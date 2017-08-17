package com.hugui.emx.snowflake.resolver;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.hugui.emx.snowflake.annotation.GlobalId;

/**
 * 
 * @author hugui
 *
 */

@Component
public class GlobalIdResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(GlobalId.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		InetAddress address = InetAddress.getLocalHost();
		String ipAddress = address.getHostAddress();

		String strShardingSign = (String) ((HttpServletRequest) webRequest.getNativeRequest())
				.getParameter("shardingSign");
		String strMachineId = ipAddress.split("\\.")[3];

		if (strMachineId.length() < 3) {
			for (int i = 0; i < (3 - strMachineId.length()); i++) {
				strMachineId = "0" + strMachineId;
			}
		}

		if (StringUtils.isEmpty(strShardingSign) || StringUtils.isEmpty(strMachineId)) {
			return -1;
		}

		synchronized (strShardingSign) {
			// 4库8表的分库分表信息
			long databaseNum = (Long.valueOf(strShardingSign) / 10) % 4 + 1;
			long tableNum = (Long.valueOf(strShardingSign) % 8) + 1;

			Long timeMillis = System.currentTimeMillis();

			String randomSeq = Math.round(Math.random() * 100) + "";

			if (randomSeq.length() < 2) {
				randomSeq = "0" + randomSeq;
			}

			return databaseNum + "" + tableNum + "" + timeMillis + "" + strMachineId + "" + randomSeq;
		}

	}
}
