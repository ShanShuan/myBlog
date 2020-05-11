package com.shanshuan.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanshuan.VO.CompanyAccountVo;
import com.shanshuan.service.serciceinterface.IManagerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangzifeng on 2020/5/11.
 */
@RestController
public class ManagerAccountController {

    @Qualifier("managerAccountServiceImpl")
    @Autowired
    private IManagerAccountService managerAccountService;

    @GetMapping("/test")
    private Page<CompanyAccountVo> test(){
        Page<CompanyAccountVo> page=new Page<CompanyAccountVo>(1,10);
        Page<CompanyAccountVo> wzf = managerAccountService.getUserListByCompanyAndPageAndSearch(page, 1, "wzf", "1231231");
        return wzf;
    }
}
