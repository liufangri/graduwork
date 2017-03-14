package com.sxy.graduwork.test;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.test.annotation.Rollback;

import com.sxy.graduwork.service.ACMService;

public class TestServiceClass extends SpringContextTest {
	@Resource
	private ACMService acmService;

	@Transactional
	@Rollback(true)
	public void testAcmService() {
		System.out.println(acmService.connectHost());
	}


}
