package com.shanshuan.mapper;

import com.shanshuan.bo.RedBag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface RedBagMapper extends Mapper<RedBag> {

    void updateReduce(@Param("id") Integer id);

    RedBag selectForupdateByid(@Param("id")int id);

    Integer updateReduceByVersion(@Param("id")Integer id, @Param("version")Integer version);
}
