package com.shanshuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanshuan.DO.ManageAccountDO;
import com.shanshuan.VO.CompanyAccountVo;
import com.shanshuan.mapper.ManageAccountMapper;
import com.shanshuan.service.serciceinterface.IManagerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangzifeng on 2020/5/11.
 */
@Service
public class ManagerAccountServiceImpl implements IManagerAccountService {
    @Autowired
    private ManageAccountMapper manageAccountMapper;
    public Page<CompanyAccountVo> getUserListByCompanyAndPageAndSearch(Page<CompanyAccountVo> page, Integer companyId, String search, String encryptSearch) {
        QueryWrapper query = Wrappers.query().eq("ma.company_id", companyId).eq("ma.is_delete", 0).and(StringUtils.isNotBlank(search), i -> i.like("ma.user_name", search)
                .or().like("ma.nickname", search).or().like("ma.phone", encryptSearch));
        return manageAccountMapper.getUserListByCompanyAndPageAndSearch(page, query);
    }

    public List<ManageAccountDO> listByUserId(List<Integer> idList) {
        List<ManageAccountDO> manageAccountDOs = manageAccountMapper.selectList(Wrappers.<ManageAccountDO>lambdaQuery().in(CollectionUtils.isNotEmpty(idList), ManageAccountDO::getId, idList)
                .eq(ManageAccountDO::getIsValid,1));
        return manageAccountDOs;
    }
}
