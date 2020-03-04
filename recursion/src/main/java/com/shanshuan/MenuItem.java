package com.shanshuan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by wangzifeng on 2020/3/3.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    private String name;//名称
    private Integer id;//id
    private Integer fid;//父id
    private List<MenuItem> list;
}
